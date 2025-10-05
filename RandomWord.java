import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        // initialise variables
        int i = 0;
        String champion = "";

        // begin a loop to go through user arguments
        while (!StdIn.isEmpty()) {
            // count iteration
            i++;

            // decide whether we have a new champion
            double prob = (double) 1 / i;
            String userInput = StdIn.readString();
            boolean newChamp = StdRandom.bernoulli(prob);

            // if we have a new champion update 'champion' value
            if (newChamp) {
                champion = userInput;
            }
        }

        // print champion
        System.out.println(champion);
    }
}
