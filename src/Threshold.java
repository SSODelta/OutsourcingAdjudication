public class Threshold implements PaymentFunction{
    private double w;
    public Threshold(double w){
        this.w = w;
    }
    @Override
    public double payment(double x) {
        return x>=0.5 ? w : 0;
    }
}
