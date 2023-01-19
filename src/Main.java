import java.io.IOException;
import java.text.SimpleDateFormat;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println();

        listPayments(1);
    }

    public static void threshold(int samples) throws IOException {
        System.out.println(timestamp()+"Starting work on the threshold payment function, with "+samples+" samples.");
        Plotter p = new Plotter(100,100,(x,y) -> new Simulator(100,50,5.0*x/100, y/100.0, new Threshold(3)));
        p.plot("out.csv", samples);
        Runtime.getRuntime().exec("python grapher_threshold.py");
        System.out.println("|\n"+timestamp()+"Finished processing.");
    }

    public static void awardLossSharing(int samples) throws IOException {
        System.out.println(timestamp()+"Starting work on the award/loss sharing payment function, with "+samples+" samples.");
        Plotter p = new Plotter(100,100,(x,y) -> new Simulator(100,50,1, y/100.0, new AwardLossSharing(100*x/100.0,100)));
        p.plot("out.csv", samples);
        Runtime.getRuntime().exec("python grapher_awardloss.py");
        System.out.println("|\n"+timestamp()+"Finished processing.");
    }

    public static void listPayments(int samples) throws IOException {
        System.out.println(timestamp()+"Starting work on the optimal payment function, with "+samples+" samples.");
        Plotter p = new Plotter(100,100,(x,y) -> new Simulator(100,x,1, y/100.0, new ListPayments(75)));
        p.plot("out.csv", samples);
        Runtime.getRuntime().exec("python grapher_list.py");
        System.out.println("|\n"+timestamp()+"Finished processing.");
    }

    public static String timestamp(){
        return "[" + new SimpleDateFormat("HH.mm.ss").format(new java.util.Date()) + "] ";
    }
}
