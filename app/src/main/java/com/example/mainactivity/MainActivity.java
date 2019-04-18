package com.example.mainactivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText txtGuess;
    private Button btnGuess;
    private Button btnPlayAgain;
    private TextView lblOutput;
    private int theNumber;
    private int tryCounter;
    private int range = 100;
    private TextView lblRange;
    private int tryLimit = range;

    public void checkGuess() {
        String guessText = txtGuess.getText().toString();
        String message = "";
        tryCounter++;
        int guess = Integer.parseInt(guessText);

            try {
                if (guess > theNumber) {
                    message = guess + " is too high. Try again.";
                } else if (guess < theNumber) {
                    message = guess + " is too low. Try again.";
                } else {
                    message = guess + " is correct. You win with " + tryCounter + " attempts!";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                    //Records the number of times you have won the game
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    int gamesPlayed = preferences.getInt("gamesPlayed", 0) + 1;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("gamesPlayed", gamesPlayed);
                    editor.apply();

                    btnPlayAgain.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                message = "Enter a whole number between 1 and " + range + ".";
            } finally {
                lblOutput.setText(message);
                txtGuess.requestFocus();
                txtGuess.selectAll();
            }
    }

    public void newGame() {
        theNumber = (int) (Math.random() * range + 1);
        btnPlayAgain.setVisibility(View.GONE);
        tryCounter = 0;
        lblOutput.setText("Enter a whole number between 1 and "+ range +".");
        lblRange.setText("Enter a whole number between 1 and "+ range +".");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtGuess = (EditText) findViewById(R.id.txtGuess);
        btnGuess = (Button) findViewById(R.id.btnGuess);
        btnPlayAgain = (Button) findViewById(R.id.btnPlayAgain);
        lblOutput = (TextView) findViewById(R.id.lblOutput);
        lblRange = (TextView)findViewById(R.id.textView2);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        range = preferences.getInt("range",100);
        newGame();

        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGuess();
            }
        });
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
        txtGuess.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // If statement will ensure that checkGuess only runs no click of enter
                if ((event.getAction()==KeyEvent.ACTION_DOWN)) {
                    checkGuess();
                    return true;
                }
                return true;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                final CharSequence[] items = {"1 to 10","1 to 100","1 to 1000","1 to 1000000"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select the Range:");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch(item) {
                            case 0:
                                range = 10;
                                storeRange(10);
                                newGame();
                                break;
                            case 1:
                                range = 100;
                                storeRange(100);
                                newGame();
                                break;
                            case 2:
                                range = 1000;
                                storeRange(1000);
                                newGame();
                                break;
                            case 3:
                                range = 1000000;
                                storeRange(1000000);
                                newGame();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case R.id.action_newgame:
                newGame();
                return true;
            case R.id.action_gamestats:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesPlayed = preferences.getInt("gamesPlayed",0);
                int gamesWon = preferences.getInt("gamesWon",0);
                int gamesLost = preferences.getInt("gamesLost",0);
                int gameRatio = preferences.getInt("gamesRatio",gamesWon/gamesPlayed);
                AlertDialog statDialog = new AlertDialog.Builder(MainActivity.this).create();
                statDialog.setTitle("Guessing Game Stat");
                String alertGamesPlayed = "Games Played: "+gamesPlayed;
                String alertChallengerMode = "Challenger Mode";
                String alertGamesWon = "Games Won: "+gamesWon;
                String alertGamesLost = "Games Lost: "+gamesLost;
                String alertWinRatio = "Games Win Rate: "+gameRatio;
                statDialog.setMessage(alertGamesPlayed+"\n\n"+alertChallengerMode+"\n"+alertGamesWon+"\n"+alertGamesLost+"\n"+alertWinRatio);
                statDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",new DialogInterface.OnClickListener(){
                   public void onClick(DialogInterface dialog, int which){
                       dialog.dismiss();
                   }
                });
                statDialog.show();
                return true;
            case R.id.action_about:
                AlertDialog aboutDialog = new AlertDialog.Builder(MainActivity.this).create();
                aboutDialog.setTitle("About Guessing Game");
                aboutDialog.setMessage("(c)2018 Andrzej Ciepiela.");
                aboutDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                aboutDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void storeRange(int newRange){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("range", newRange);
        editor.apply();
    }
}
