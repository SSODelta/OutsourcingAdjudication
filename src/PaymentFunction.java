public interface PaymentFunction {
    /**
     * Gives the payment to agent i when an x-fraction of agents vote the same as agent i.
     * @param x The fraction of agents who agree with the vote of agent i.
     * @return The payment to agent i.
     */
    double payment(double x);

    default double Q(int mi, int n){
        return payment((1.0+mi)/n) - payment((n - 1.0*mi)/n);
    }
}
