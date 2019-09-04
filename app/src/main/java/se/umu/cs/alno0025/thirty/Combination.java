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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Combination implements Parcelable {
    private List<Die> combinationList;
    private int rank;

    /**
     * Constructor to initialize objects new combination.
     * @param list List of multiple die that adds up to a combination.
     */
    public Combination(List<Die> list){
        combinationList = new ArrayList<>();
        combinationList = list;
        rank = setRank(list);
    }

    /**
     * Checks if the die is used in the combination.
     * @param die the die to check.
     * @return true/false depending on if the die is used or not.
     */
    public boolean isUsed(Die die){
        boolean isUsed = false;
        for(Die d: combinationList){
            if(d.equals(die))
                isUsed = true;
        }
        return isUsed;
    }

    /**
     * @return the list of the combination.
     */
    public List<Die> getCombinationList() {
        return combinationList;
    }

    /**
     * Sets rank for the combination.
     * The more duplicates the lower the rank.
     * (The lower the rank the better)
     * @param list
     * @return
     */
    private static int setRank(List<Die> list) {

        final List<Integer> duplicatedObjects = new ArrayList<Integer>();
        Set<Integer> set = new HashSet<Integer>() {
            @Override
            public boolean add(Integer value) {
                if (contains(value)) {
                    duplicatedObjects.add(value);
                }
                return super.add(value);
            }
        };
        for (Die d : list) {
            set.add(d.getValue());
        }
        return 1 + duplicatedObjects.size();
    }

    /**
     * Rank is used to prioritize the combinations.
     * @return returns this combinations rank.
     */
    public int getRank() {
        return rank;
    }

    /** Method for Parcelable.
     * @return int = 0.
     */
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this combination in to a Parcel
     * @param out The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(combinationList);
    }

    /**
     * Constructor used to initialize the die
     * again after recreation.
     * @param in Parcel with the combination to recreate
     */
    private Combination(Parcel in){
        combinationList = in.createTypedArrayList(Die.CREATOR);
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field
     * that generates instances of the Parcelable combination class from a Parcel.
     */
    public static final Parcelable.Creator<Combination> CREATOR = new Parcelable.Creator<Combination>() {

        // Create a new instance of the Parcelable class.
        @Override
        public Combination createFromParcel(Parcel in) {
            return new Combination(in);
        }

        // Create a new array of the Parcelable class.
        @Override
        public Combination[] newArray(int size) {
            return new Combination[size];
        }
    };
}
