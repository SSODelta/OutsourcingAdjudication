import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a payment function based on a list of files.
 * Intended to be used for the minimal payment function, whose solution is solved using linear programming (and placed in payments/)
 */
public class ListPayments implements PaymentFunction {

    /**
     * Parameters of the list payments.
     */
    private List<Double> vals = new ArrayList<>();

    /**
     * Instantiates the list payment based on the file specified by 'x'.
     * Specifically, it will use the file payments/0_x.csv to specify the payment function.
     * For the minimal payment function, x goes from [51,100)
     * @param x Parameter for the list payment.
     */
    public ListPayments(int x){
        try {
            for(String p : Files.readAllLines(Paths.get("payments/0_"+x+".csv"))){
                vals.add(Double.parseDouble(p));
            }
        } catch (IOException e) {
            e.printStackTrace();
            vals.add(0.0);
        }
    }

    @Override
    public double payment(double x) {
        return vals.get((int) Math.floor((x-0.001) * vals.size()));
    }
}
