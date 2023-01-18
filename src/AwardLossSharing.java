public class AwardLossSharing implements PaymentFunction {

    private double w;
    private int n;

    public AwardLossSharing(double w, int n){
        this.w = w;
        this.n = n;
    }

    @Override
    public double payment(double x) {
        return x >= 0.5 ? w / (x*n) : -w/(x*n);
    }
}
