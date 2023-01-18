import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ListPayments implements PaymentFunction {

    private List<Double> vals = new ArrayList<>();

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
