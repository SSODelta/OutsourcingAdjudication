import java.util.Random;

/**
 * This class simulates the adjudication game described in [CS23]
 */
public class Simulator {

    private Random r = new Random();
    private EffortFunction f = EffortFunction.EXP;
    private PaymentFunction p;
    private double epsilon, rho;
    private int n, rounds;

    public Simulator(int n, int rounds, double epsilon, double rho, PaymentFunction p){
        this.epsilon = epsilon;
        this.rho = rho;
        this.n = n;
        this.rounds = rounds;
        this.p = p;
    }

    public boolean well_informed(int i){
        return i < rho*n;
    }

    public boolean signal(int i, double effort){
        return well_informed(i) == f.signal(effort);
    }

    public int mi(int i, boolean[] votes){
        int c = 0;
        for(int j=0; j<votes.length; j++)
            if(j!=i && votes[j])c++;
        return c;
    }

    public boolean correct(boolean[] votes){
        int c = 0;
        for(int j=0; j<votes.length; j++)
            if(votes[j])c++;
        if(c==votes.length/2)
            return r.nextBoolean();
        return c>votes.length/2;
    }

    public double freq(boolean[] votes){
        int c = 0;
        for(int j=0; j<votes.length; j++)
            if(votes[j])c++;
        return c / (double)votes.length;
    }

    /**
     * Simulates a number of rounds of the adjudication game.
     * @return The average correctness observed.
     */
    public double simulate(){

        // In the first round, all agents invest epsilon effort and vote accordingly
        boolean[] votes = new boolean[n];
        for(int i=0; i<n; i++)
            votes[i] = signal(i, epsilon);

        // Initialize number of correct rounds
        int correct = 0;//correct(votes) ? 1 : 0;

        // Simulate each subsequent round
        for(int r=0; r<rounds; r++){
            boolean[] new_votes = new boolean[n];

            // Loop through each agent i
            for(int i=0; i<n; i++) {
                // Initialize their strategy
                double lambda;
                int B;

                // Compute agent specific values
                int mi = mi(i,votes);
                double Qi = p.Q(mi,n);

                // If the reward is not sufficiently high, then invest no effort
                if(Math.abs(Qi) <= 2){
                    lambda = 0;
                    B = 0;
                } else {
                    // Otherwise invest some effort and vote according to signal received
                    lambda = Math.log(Math.abs(Qi)/2);
                    if(Qi > 2)
                        B = well_informed(i) ? 1 : 0;
                    else
                        B = well_informed(i) ? 0 : 1;
                }
                boolean signal = signal(i, lambda);
                new_votes[i] = (B == 1) == signal;

            }
            votes = new_votes;
            if(correct(votes))
                correct++;
        }
        return correct / (1.0*rounds);// + 1.0);
    }

}
