import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public class Plotter {

    ExecutorService es = Executors.newFixedThreadPool(4);

    private final double[][] Qs;
    private int x, y;
    private final BiFunction<Integer, Integer, Simulator> f;

    public Plotter(int x, int y, BiFunction<Integer,Integer,Simulator> f){
        this.x = x;
        this.y = y;
        this.f = f;
        Qs = new double[x][y];
    }

    private int progress_bar_len = 100;
    public void plot(String out, int samples){
        long start = System.currentTimeMillis();
        int totalwork = x*y*samples;
        final int[] work = { 0 };

        for(int i=0; i<x; i++)
        for(int j=0; j<y; j++)
        for(int k=0; k<samples; k++){
            int finalI = i;
            int finalJ = j;
            es.execute(() -> {
                Simulator s = f.apply(finalI,finalJ);
                double c = s.simulate();
                synchronized (Qs){
                    Qs[finalI][finalJ] += c;
                    if(++work[0] % (totalwork/progress_bar_len) == 0) {
                        long end =  System.currentTimeMillis();

                        // If we are drawing first dot
                        if(work[0] <= totalwork/progress_bar_len){
                            Date enddate = new Date(end + (end - start)*(progress_bar_len-1));
                            System.out.print(Main.timestamp()+"Expecting to finish at " + new SimpleDateFormat("HH.mm.ss").format(enddate)+"\n|");
                            for(int d=0; d<progress_bar_len; d++)
                                System.out.print(".");
                            System.out.print("|\n|");
                        }

                        // Now draw a dot
                        System.out.print(".");
                    }
                }
            });
        }
        es.shutdown();
        try {
            if(es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)){
                PrintWriter pw = new PrintWriter(out);
                for(int j=0; j<y; j++) {
                    for(int i=0; i<x; i++) {
                        pw.print(Qs[i][y-1-j] / (double) samples + (i < x - 1 ? ", " : ""));
                    }
                    pw.println();
                }
                pw.close();
            } else {
                System.out.println("it timed out :(");
            }
        } catch (InterruptedException e) {
            System.out.println("was interrupted :(");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("io error :(");
            e.printStackTrace();
        }
    }
}
