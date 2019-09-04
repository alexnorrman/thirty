/**
 * Course:  Development for mobile applications.
 *          Umeå University
 *          Summer 2019
 * @author Alex Norrman
 */

package se.umu.cs.alno0025.thirty;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    private static final int TOTAL_NR_OF_ROUNDS = 10;
    private static final String twName = "score";

    private int totalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the layout depending on the orientation of the phone.
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_score_h);
        else
            setContentView(R.layout.activity_score);


        // Sets the total score to 0;
        totalScore = 0;

        // Get the Intent that started this activity and extract the integers.
        // Sets the textviews to the string of the extracted integers.
        // Adds the integers to a total score.
        Intent intent = getIntent();
        for (int i = 0; i < TOTAL_NR_OF_ROUNDS; i++) {
            int target = i + 3;
            int score = intent.getIntExtra(String.valueOf(target), -1);

            TextView tw = findViewById(getResources().getIdentifier(
                    twName + i, "id", getPackageName()));
            if(score > -1){
                tw.setText(String.valueOf(score));
                totalScore += score;
            }
        }

        // Capture the layout's finalScore and set the final score as its text
        TextView textView = findViewById(R.id.finalScore);
        textView.setText(String.valueOf(totalScore));


        // Adds OnClickListener to new game button.
        // Calls newGame() on click.
        Button b = findViewById(R.id.newGame);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
    }


    /**
     * Go to MainActivity.
     */
    protected void newGame(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Calls newGame on back pressed.
     */
    @Override
    public void onBackPressed() {
        newGame();
    }
}