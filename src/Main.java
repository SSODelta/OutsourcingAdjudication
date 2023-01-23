import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Repeat the experiments from our paper
 */
public class Main {

    /**
     * Repeats the experiments Fig. 2(a), Fig. 2(b), and Fig. 2(d).
     * To perform the remaining experiments, slight changes in the lambda expressions in below function definitions are needed.
     *
     * In order to output the data as a .png, python 3 needs to be installed in PATH. Currently, it uses 'python3' as alias for python, but this can be changed in this file.
     *
     * An image is output in imgs/HH.mm.ss.png with corresponding data in data/HH.mm.ss.csv where HH.mm.ss is the current time.
     * @param args Not used.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println();

        // Run the threshold payment function
        threshold(1000);

        // Run the award/loss sharing payment function
        awardLossSharing(1000);

        // Run the minimal payment function
        minimalPayments(1000);
    }

    /**
     * Simulate a number of sequential games with 50 rounds, n=100 jurors, starting effort epsilon=1, using the threshold payment function.
     * @param samples The number of samples for each data point
     * @throws IOException
     */
    public static void threshold(int samples) throws IOException {
        System.out.println(timestamp()+"Starting work on the threshold payment function, with "+samples+" samples.");
        Plotter p = new Plotter(100,100,(x,y) -> new Simulator(100,50,1, y/100.0, new Threshold(5.0*x/100)));
        p.plot("out.csv", samples);
        Runtime.getRuntime().exec("python3 grapher_threshold.py");
        System.out.println("|\n"+timestamp()+"Finished processing.");
    }

    /**
     * Simulate a number of sequential games with 50 rounds, n=100 jurors, starting effort epsilon=1, using the award/loss sharing payment function.
     * @param samples The number of samples for each data point
     * @throws IOException
     */
    public static void awardLossSharing(int samples) throws IOException {
        System.out.println(timestamp()+"Starting work on the award/loss sharing payment function, with "+samples+" samples.");
        Plotter p = new Plotter(100,100,(x,y) -> new Simulator(100,50,1, y/100.0, new AwardLossSharing(100*x/100.0,100)));
        p.plot("out.csv", samples);
        Runtime.getRuntime().exec("python3 grapher_awardloss.py");
        System.out.println("|\n"+timestamp()+"Finished processing.");
    }

    /**
     * Simulate a number of sequential games with 50 rounds, n=100 jurors, starting effort epsilon=1, using the minimal payment function.
     * @param samples The number of samples for each data point
     * @throws IOException
     */
    public static void minimalPayments(int samples) throws IOException {
        System.out.println(timestamp()+"Starting work on the minimal payment function, with "+samples+" samples.");
        Plotter p = new Plotter(48,100,(x,y) -> new Simulator(100,50,1, y/100.0, new ListPayments(51+x)));
        p.plot("out.csv", samples);
        Runtime.getRuntime().exec("python3 grapher_list.py");
        System.out.println("|\n"+timestamp()+"Finished processing.");
    }

    /**
     * Computes a time stamp to use for logging.
     * @return Time stamp of the current time (In HH.mm.ss format).
     */
    public static String timestamp(){
        return "[" + new SimpleDateFormat("HH.mm.ss").format(new java.util.Date()) + "] ";
    }
}
