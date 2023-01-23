import java.util.Random;

/**
 * This class simulates the adjudication game described in our paper and reports back the observed correctness.
 *
 * Round 0: Agent i puts an effort of ε and casts her signal as
 * vote.
 *
 * Round j, for j = 1, 2, ..., R: Agent i gets mi as feedback.
 * She decides her strategy βi ∈ {0, 1} and effort level
 * λi ≥ 0. She draws her signal, which is alternative T
 * with probability fi(λi) and alternative F with probability
 * 1 − fi(λi). If βi = 1, she casts her signal as vote;
 * otherwise, she casts the opposite of her signal as vote
 */
public class Simulator {

    private Random r = new Random();
    private EffortFunction f = EffortFunction.EXP;
    private PaymentFunction p;
    private double epsilon, rho;
    private int n, rounds;

    /**
     * Creates a new simulator for one iteration of the sequential game, using the exponential effort function.
     * @param n The number of agents.
     * @param rounds The number of rounds to execute (excluding round 0).
     * @param epsilon The starting effort to use.
     * @param rho The fraction of well-informed agents.
     * @param p The payment function.
     */
    public Simulator(int n, int rounds, double epsilon, double rho, PaymentFunction p){
        this.epsilon = epsilon;
        this.rho = rho;
        this.n = n;
        this.rounds = rounds;
        this.p = p;
    }

    /**
     * Determine if agent i is well-informed (assume the first rho*n agents are well-informed).
     * @param i The agent to determine
     * @return Whether agent i is well-informed
     */
    public boolean well_informed(int i){
        return i < rho*n;
    }

    /**
     * Sample a signal for agent i, given their effort.
     * Here, true represents the ground truth, and false the opposite alternative.
     * @param i The agent
     * @param effort The amount of effort to exert
     * @return A signal (true = ground truth, false = opposite)
     */
    public boolean signal(int i, double effort){
        return well_informed(i) == f.signal(effort);
    }

    /**
     * Computes the number of agents =/= i that voted for the ground truth
     * @param i The agent to exclude
     * @param votes The vector of votes.
     * @return sum_{j=/=i} votes[j]
     */
    public int mi(int i, boolean[] votes){
        int c = 0;
        for(int j=0; j<votes.length; j++)
            if(j!=i && votes[j])c++;
        return c;
    }

    /**
     * Determine if a vector of votes has a majority of votes for the ground truth.
     * @param votes The vector of votes.
     * @return Whether or not sum_i votes[i] >= votes.length/2
     */
    public boolean correct(boolean[] votes){
        int c = 0;
        for(int j=0; j<votes.length; j++)
            if(votes[j])c++;
        if(c==votes.length/2)
            return r.nextBoolean();
        return c>votes.length/2;
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
