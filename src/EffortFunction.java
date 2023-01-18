import java.util.Random;

public interface EffortFunction {

    Random r = new Random();

    /**
     * Returns the quality of the vote, given the effort 'effort'
     * @param effort The effort>=0 to put in
     * @return A number [0,1], with quality(0)=0.5
     */
    double quality(double effort);

    /**
     * Samples a signal given to agent i according to the quality function, assuming an effort of 'effort'.
     * @param effort
     * @return
     */
    default boolean signal(double effort){
        double q = quality(effort);
        return r.nextDouble() <= q;
    }

    EffortFunction EXP = x -> 1 - 0.5*Math.exp(-x);
}
