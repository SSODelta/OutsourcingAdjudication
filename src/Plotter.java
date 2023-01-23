import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * Estimates correctness of the adjudication game in a grid where two parameters are varied (e.g. fraction of well-informed and size of payment).
 * The result is output to a .csv file in data/ and a .png file (as a heatmap) in imgs/.
 *
 * The last part requires python to be installed.
 */
public class Plotter {

    /**
     * Multithreading.
     */
    private ExecutorService es = Executors.newFixedThreadPool(4);

    /**
     * The stored values.
     */
    private final double[][] Qs;

    /**
     * Size of the image.
     */
    private int x, y;

    /**
     * Maps (x,y) to a Simulator to use (gives the variation).
     */
    private final BiFunction<Integer, Integer, Simulator> f;

    /**
     * Creates a new plotter to create heatmaps of adjudication correctness.
     * @param x Number of data points along the x-axis.
     * @param y Number of data points along the y-axis.
     * @param f Maps each point (i,j) to a Simulator instance to produce to variation (from i=0...x-1 and j=0...y-1).
     */
    public Plotter(int x, int y, BiFunction<Integer,Integer,Simulator> f){
        this.x = x;
        this.y = y;
        this.f = f;
        Qs = new double[x][y];
    }

    /**
     * Size of progress bar (why not 100?)
     */
    private int progress_bar_len = 100;

    /**
     * Runs the plot.
     * @param out Name of the .csv file to output locally (usually just 'out.csv').
     * @param samples The number of samples to use for each data point (is a bit jagged for samples<100)
     */
    public void plot(String out, int samples){
        long start = System.currentTimeMillis();
        int totalwork = x*y*samples;
        final int[] work = { 0 };

        // Multithreading accounting
        for(int i=0; i<x; i++)
        for(int j=0; j<y; j++)
        for(int k=0; k<samples; k++){
            int finalI = i;
            int finalJ = j;

            // Add the task to the ExecutorService.
            // This function looks a bit complicated only because of the progress bar.
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
