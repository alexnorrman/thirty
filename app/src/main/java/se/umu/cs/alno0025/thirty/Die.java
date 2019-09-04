/**
 * Course:  Development for mobile applications.
 *          Umeå University
 *          Summer 2019
 * @author Alex Norrman
 */
package se.umu.cs.alno0025.thirty;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class Die implements Parcelable {
    private static final String REROLL_IMG_NAME = "white";
    private static final String ROLL_IMG_NAME = "grey";

    private int value;
    private String imgName;
    private boolean reRoll;

    /**
     * Constructor to initialize a new die.
     */
    public Die(){
        reRoll = true;
        Roll();
    }

    /**
     * Roll the die if it is a reroll die.
     * Updates the image name for the die.
     */
    public void Roll(){
        if(reRoll)
            value = new Random().nextInt(6) + 1;
        setImgName();
    }

    /**
     * @return Value of the die.
     */
    public int getValue() { return value;    }

    /**
     * @return Image name for the die
     */
    public String getImgName() { return imgName; }

    /**
     * Sets the imagename to the correct name
     * depending on if the die should be rerolled or not.
     */
    public void setImgName() {
        if(reRoll)
            imgName = REROLL_IMG_NAME+value;
        else
            imgName = ROLL_IMG_NAME+value;
    }

    /**
     * @return true/falls if the die should be rerolled or not.
     */
    public boolean isReRoll() { return reRoll;  }

    /**
     * Sets a die to reroll true or false.
     * Then updates the image name for the die.
     * @param reRoll
     */
    public void setReRoll(boolean reRoll) {
        this.reRoll = reRoll;
        setImgName();
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
        out.writeInt(value);
        out.writeByte(reRoll ? (byte) 1 : (byte) 0);
        out.writeString(imgName);
    }
    /**
     * Constructor used to initialize the die
     * again after recreation.
     * @param in Parcel with the die to recreate
     */
    private Die(Parcel in){
        value = in.readInt();
        reRoll = in.readByte() != 0;
        imgName = in.readString();
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field
     * that generates instances of the Parcelable die class from a Parcel.
     */
    public static final Parcelable.Creator<Die> CREATOR = new Parcelable.Creator<Die>() {

        // Create a new instance of the Parcelable class.
        @Override
        public Die createFromParcel(Parcel in) {
            return new Die(in);
        }

        // Create a new array of the Parcelable class.
        @Override
        public Die[] newArray(int size) {
            return new Die[size];
        }
    };
}
