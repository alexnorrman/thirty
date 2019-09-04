/**
 * Course:  Development for mobile applications.
 *          Umeå University
 *          Summer 2019
 * @author Alex Norrman
 */

package se.umu.cs.alno0025.thirty;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Game implements Parcelable {

    private static final int ROUNDS = 10;
    private static final int ALLOWED_ROLLS = 3;

    private Dice dice;
    private List<Combination> allCombinations;
    private HashMap<Integer, Integer> savedScore;
    private boolean gameOver;
    private int round;
    private int rolls;

    /**
     * Constructor to initialize a new game.
     */
    public Game(){
        dice = new Dice();
        allCombinations = new ArrayList<>();
        savedScore = new HashMap<>();
        gameOver = false;
        rolls = 0;
        round = 0;
        rollDice();
    }

    /**
     * If rolls is allowed.
     * Roll the dice and adds 1 to rolls.
     * @return number of rolls.
     */
    public int rollDice(){
        if(rolls < ALLOWED_ROLLS){
            dice.rollDice();
            rolls++;
        }
        return rolls;
    }

    /**
     * @return list of multiple die
     */
    public List<Die> getDieList() {
        return dice.getDice();
    }

    /**
     * @return number of rolls
     */
    public int getRolls() { return rolls; }

    /**
     * Set rolls to 0.
     */
    public void resetRolls() {
        rolls = 0;
    }

    /**
     * Gets the amount of rounds saved (how many rounds that has been played)
     * @return size of HashMap of the rounds/score.
     */
    public int getRounds() {
        return savedScore.size();
    }

    /**
     * Adds one more round.
     */
    public void addRound() {
        round++;
    }

    /**
     * Adds a new value to the HashMap with the round as key
     * and the value as score.
     * @param round ID of the round.
     * @param score value of the score.
     */
    public void setScore(int round, int score) { savedScore.put(round, score); }

    /**
     * @return HashMap with the saved scores form the played round.
     */
    public HashMap<Integer, Integer> getScore() { return savedScore; }

    /**
     * Checks the if the rounds has reached the maximum rounds.
     * If it has, return true, else false.
     * @return game over = true/false
     */
    public boolean gameOver(){
        if (getRounds() >= ROUNDS){
            gameOver = true;
            return gameOver;
        }
        return gameOver;
    }

    /**
     * Input the wanted value, the targeted sum.
     * If the target is above 3, then calls getAllCombinations();
     * Else, adds the values of all the dice with the face value 1,2 or 3.
     * @param targetSum
     * @return
     */
    public int getRoundScore(int targetSum){
        int finalSum = 0;
        if(targetSum > 3){
            allCombinations.clear();
            getAllCombinations(dice.getDice(), targetSum, new ArrayList<Die>());
            finalSum = getBestCombinations(allCombinations).size() * targetSum;
        }
        else{
            for(Die die:dice.getDice()){
                if(die.getValue() < 4)
                    finalSum += die.getValue();
            }
        }
        return finalSum;
    }
    /**
     * Sorts a list of combinations by their rank. Starts adding the lowest ranked combination
     * to a final list of combinations. If a die recur, this combination will not be added.
     * @param allCombinations List of combinations.
     * @return List of the best possible combinations.
     */
    private List<Combination> getBestCombinations(List<Combination> allCombinations){
        List<Combination> bestCombinations = new ArrayList<>();
        List<Die> usedDice = new ArrayList<>();
        Boolean used;

        // Sorts the list. The lower the rank the better.
        Collections.sort(allCombinations, new Comparator<Combination>() {
            public int compare(Combination c1, Combination c2) {
                return c1.getRank() - c2.getRank();
            }
        });

        // Checks if the dice in the combination already is used
        // Otherwise, adds the dice to the best combination list.
        for(Combination c:allCombinations){
            used = false;
            for(Die die:c.getCombinationList()){
                if(usedDice.contains(die))
                    used = true;
            }
            if(!used){
                for(Die die:c.getCombinationList())
                    usedDice.add(die);
                bestCombinations.add(c);
            }
        }
        return bestCombinations;
    }

    /**
     *  A recursive function to get all the possible combinations that adds up to the
     *  targeted sum. Adds all the combinations to a list of combinations called 'allCombinations'.
     * @param remaining The remaining list of dice this iteration.
     * @param targetSum The targeded sum that the combinations should reach.
     * @param listToSum The dice to sum for this iteration.
     */
    private void getAllCombinations(List<Die> remaining, int targetSum, List<Die> listToSum) {
        int sum = 0;

        // Sums all the dice in the list to sum.
        for (Die die : listToSum) {
            sum += die.getValue();
        }

        // If the combination is the wanted value,
        // adds this combination to the list.
        if (sum == targetSum){
            allCombinations.add(new Combination(listToSum));
            return;
        }

        // If not a combination, return
        if (sum > targetSum){
            return;
        }

        //Iterate each remaining die
        for (int i = 0; i < remaining.size(); i++) {
            // Creates a new list of the remaining die
            // used for the next iteration.
            List<Die> newRemaining = new ArrayList<>();
            for (int j = i + 1; j < remaining.size(); j++) {
                newRemaining.add(remaining.get(j));
            }
            // Creates the new list of die to sum
            // next iteration.
            List<Die> newListToSum = new ArrayList<>(listToSum);
            Die currentDie = remaining.get(i);
            newListToSum.add(currentDie);

            // Calls the function again to iterate.
            getAllCombinations(newRemaining, targetSum, newListToSum);
        }
    }

    /** Method for Parcelable.
     * @return int = 0.
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this die in to a Parcel
     * @param out The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(dice , flags);
        out.writeTypedList(allCombinations);
        out.writeSerializable(this.savedScore);
        out.writeByte(gameOver ? (byte) 1 : (byte) 0);
        out.writeInt(round);
        out.writeInt(rolls);
        out.writeSerializable((Serializable) savedScore);
    }

    /**
     * Constructor used to initialize the die
     * again after recreation.
     * @param in Parcel with the game to recreate
     */
    private Game(Parcel in){
        dice = in.readParcelable(Dice.class.getClassLoader());
        allCombinations = in.createTypedArrayList(Combination.CREATOR);
        savedScore = (HashMap<Integer, Integer>) in.readSerializable();
        gameOver = in.readByte() != 0;
        round = in.readInt();
        rolls = in.readInt();
        savedScore = (HashMap<Integer, Integer>) in.readSerializable();
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field
     * that generates instances of the Parcelable game class from a Parcel.
     */
    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {

        // Create a new instance of the Parcelable class.
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        // Create a new array of the Parcelable class.
        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
