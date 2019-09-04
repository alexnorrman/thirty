/**
 * Course:  Development for mobile applications.
 *          Umeå University
 *          Summer 2019
 * @author Alex Norrman
 */

package se.umu.cs.alno0025.thirty;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Dice implements Parcelable {
    private static final int NR_OF_DICE = 6;
    private List<Die> dice;

    /**
     * Constructor to initialize a new dice.
     */
    public Dice(){
        dice = new ArrayList<>();
        createDiceList();
    }

    /**
     * Creates a new die list with the size of NR_OF_DICE (6).
     */
    public void createDiceList(){
        if(!dice.isEmpty())
            dice.clear();
        for(int i = 0; i < NR_OF_DICE; i++)
            dice.add(new Die());
    }

    /**
     * Rolls all the dice.
     */
    public void rollDice(){
        for(Die die : dice)
            die.Roll();
    }

    /**
     * Returns the value of a specific die.
     * @param index The index of the list.
     * @return The value of the dice for the index.
     */
    public int getDieValue(int index){
        return dice.get(index).getValue();
    }

    /**
     * @return return the dice list.
     */
    public List<Die> getDice() {
        return dice;
    }

    /** Method for Parcelable.
     * @return int = 0.
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this dice in to a Parcel
     * @param out The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(dice);
    }
    /**
     * Constructor used to initialize the die
     * again after recreation.
     * @param in Parcel with the dice to recreate
     */
    private Dice(Parcel in){
        dice = in.createTypedArrayList(Die.CREATOR);
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field
     * that generates instances of the Parcelable dice class from a Parcel.
     */
    public static final Parcelable.Creator<Dice> CREATOR = new Parcelable.Creator<Dice>() {

        // Create a new instance of the Parcelable class.
        @Override
        public Dice createFromParcel(Parcel in) {
            return new Dice(in);
        }

        // Create a new array of the Parcelable class.
        @Override
        public Dice[] newArray(int size) {
            return new Dice[size];
        }
    };
}
