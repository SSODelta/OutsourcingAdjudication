/**
 * The threshold payment function.
 *
 * It pays w to any agent in the majority, and 0 otherwise.
 */
public class Threshold implements PaymentFunction{

    private double w;

    /**
     * Instantiates a new threshold payment function
     * @param w Size of the reward to agents in the majority.
     */
    public Threshold(double w){
        this.w = w;
    }
    @Override
    public double payment(double x) {
        return x>=0.5 ? w : 0;
    }
}
