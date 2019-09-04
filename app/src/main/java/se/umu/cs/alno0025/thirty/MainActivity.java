/**
 * Course:  Development for mobile applications.
 *          Umeå University
 *          Summer 2019
 * @author Alex Norrman
 */

package se.umu.cs.alno0025.thirty;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String GAME_KEY = "se.umu.cs.alno0025.thirty.game";
    private static final int TOTAL_NR_OF_ROLLS = 3;
    private static final int TOTAL_NR_OF_ROUNDS = 10;
    private static final int NR_OF_DICE = 6;

    private Game game;
    private List<ImageView> imgView;
    private List<Button> addScoreButton;
    private List<TextView> scoreT;
    private TextView diceToRoll, timesRolled;
    private Button rollButton, buttonRestart;
    private Die die;
    private int rolling, diceSaved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the layout depending on the orientation of the phone.
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_main_h);
        else
            setContentView(R.layout.activity_main);

        // If an instance is saved, gets the parcelable game.
        // Otherwise, stars a new game.
        if(savedInstanceState != null)
            game = savedInstanceState.getParcelable(GAME_KEY);
        else
            game = new Game();

        // Gets all the needed widgets
        getWidgets();



        // Adds OnClickListener for all dice IageViews.
        // Changes the dices from grey (saved) to white (reroll) and vice versa.
        // Updates UI
        diceSaved = 0;
        for(int i = 0; i < NR_OF_DICE; i++){
            final int fI = i;
            imgView.get(fI);
            imgView.get(fI).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    die = game.getDieList().get(fI);
                    if(die.isReRoll()){
                        die.setReRoll(false);
                        diceSaved++;
                    }
                    else{
                        die.setReRoll(true);
                        diceSaved--;
                    }
                    updateImageView(imgView.get(fI), die.getImgName());
                    updateUI();
                }
            });
        }

        // Adds OnClickListener for rollButton.
        // If the game is not over, roll the dice and update the UI.
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!game.gameOver() && diceSaved != 6){
                    game.rollDice();
                    updateUI();
                }
            }
        });

        // Adds OnClickListener for all addScoreButtons.
        // If the game is not over, sets the users choice of score, rerolls
        // and updates UI.
        for(int i = 0; i < 10; i++){
            final int fT = i+3;
            Button b = addScoreButton.get(i);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!game.gameOver()){
                        game.setScore(fT,game.getRoundScore(fT));
                        game.resetRolls();
                        game.addRound();
                        for(Die d:game.getDieList())
                            d.setReRoll(true);
                        game.rollDice();
                    }
                    diceSaved = 0;
                    updateUI();
                }
            });
        }

        // Adds OnClickListener for restart button.
        // Creates a new game object.
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle savedInstanceState = null;
                game = new Game();
                updateUI();
            }
        });

        // Updates UI.
        updateUI();
    }

    /**
     * Gets all needed widgets by ID.
     * Creates a new game.
     */
    public void getWidgets() {
        final String imgName = "dieView";
        final String buttonName = "addScore";
        final String twName = "score";

        imgView = new ArrayList<>();
        addScoreButton = new ArrayList<>();
        scoreT = new ArrayList<>();

        rollButton = findViewById(R.id.rollButton);
        buttonRestart = findViewById(R.id.restartButton);
        diceToRoll = findViewById(R.id.diceToRoll);
        timesRolled = findViewById(R.id.timesRolled);

        for (int i = 0; i < NR_OF_DICE; i++) {
            ImageView iv = findViewById(getResources().getIdentifier(
                    imgName + i, "id", getPackageName()));
            imgView.add(iv);
        }

        for (int i = 0; i < TOTAL_NR_OF_ROUNDS; i++) {
            int target = i + 3;
            Button a = findViewById(getResources().getIdentifier(
                    buttonName + i, "id", getPackageName()));
            addScoreButton.add(a);

            TextView tw = findViewById(getResources().getIdentifier(
                    twName + i, "id", getPackageName()));
            scoreT.add(tw);
        }
    }

    /**
     * Updates the given ImageView.
     * @param img ImageView to update.
     * @param imgName Name to be updated
     */
    private void updateImageView(ImageView img, String imgName){
        img.setImageResource(getResources().getIdentifier(imgName, "drawable", getPackageName()));
    }

    /**
     * Updates all needed widgets for the UI.
     */
    private void updateUI(){
        if(game.gameOver())
            goToScoreActivity();

        rolling = 0;
        for(Die d:game.getDieList()){
            if(d.isReRoll())
                rolling++;
        }
        if(game.getRolls() == TOTAL_NR_OF_ROLLS){
            rollButton.setEnabled(false);
            diceToRoll.setText("");
        }
        else{
            rollButton.setEnabled(true);
            diceToRoll.setText("Rolling " + rolling + " dice:");
        }

        for(int i = 0; i < NR_OF_DICE; i++)
            updateImageView(imgView.get(i),game.getDieList().get(i).getImgName());

        timesRolled.setText("(" + game.getRolls() + "/"+ TOTAL_NR_OF_ROLLS +" rolls)");

        TextView tw;
        Button ab;
        for(int i = 0; i < TOTAL_NR_OF_ROUNDS; i++){
            int target = i + 3;
            ab = addScoreButton.get(i);
            tw = scoreT.get(i);
            if(game.getScore().containsKey(target)){
                tw.setText(Integer.toString(game.getScore().get(target)));
                tw.setTypeface(null, Typeface.BOLD);
                ab.setEnabled(false);
                ab.setVisibility(View.INVISIBLE);
            }
            else{
                tw.setText("(" + game.getRoundScore(target) +")");
                tw.setTypeface(null, Typeface.ITALIC);
                ab.setEnabled(true);
                ab.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Saves the game to the
     * @param saveInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putParcelable(GAME_KEY, game);
    }

    /**
     * Gets the total score of the game and creates an intent with the score.
     * Goes to ScoreActivity.
     */
    public void goToScoreActivity() {
        int totalScore = 0;
        Intent intent = new Intent(this, ScoreActivity.class);

        for(int i = 3; i < TOTAL_NR_OF_ROUNDS+3; i++){
            intent.putExtra(String.valueOf(i), game.getScore().get(i));
        }
        startActivity(intent);
    }

    /**
     * Adds alert dialog if the user presses the back button
     * to close the application.
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Thirty")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}