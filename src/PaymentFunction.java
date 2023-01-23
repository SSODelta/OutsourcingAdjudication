/**
 * A payment function relates the fraction of agents that agree with a given agent to the reward they receive.
 */
public interface PaymentFunction {
    /**
     * Gives the payment to agent i when an x-fraction of agents vote the same as agent i.
     * @param x The fraction of agents who agree with the vote of agent i.
     * @return The payment to agent i.
     */
    double payment(double x);

    /**
     * Computes the expected payment an agent i receives when voting for the ground truth, compared to voting for the opposite alternative.
     * @param mi The number of agents different from agent i that voted for the ground truth.
     * @param n The total number of agents.
     * @return Expected payment from switching from voting against ground truth to voting in favor of it.
     */
    default double Q(int mi, int n){
        return payment((1.0+mi)/n) - payment((n - 1.0*mi)/n);
    }
}
