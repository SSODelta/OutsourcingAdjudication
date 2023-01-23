/**
 * Implements the award/loss sharing payment function, defined as follows:
 *
 * p(x) = w / (x*n) if x>=0.5, and      [share the reward in the majority]
 * p(x) = - w / (x*n) otherwise         [share the cost in the minority]
 */
public class AwardLossSharing implements PaymentFunction {

    private double w;
    private int n;

    /**
     * Instantiate the award/loss sharing payment function
     * @param w The total reward to split.
     * @param n The number of agents.
     */
    public AwardLossSharing(double w, int n){
        this.w = w;
        this.n = n;
    }

    @Override
    public double payment(double x) {
        return x >= 0.5 ? w / (x*n) : -w/(x*n);
    }
}
