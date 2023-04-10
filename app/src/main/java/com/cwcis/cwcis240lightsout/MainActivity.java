package com.cwcis.cwcis240lightsout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private LightsOutGame mGame;
    private GridLayout mLightGrid;
    private int mLightOnColor;
    private int mLightOffColor;
    private int mLightOffText;
    private int mLightOnText;

    private final String GAME_STATE = "gameState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLightGrid = findViewById(R.id.light_grid);

        // Add the same click handler to all grid buttons
        for (int buttonIndex = 0; buttonIndex < mLightGrid.getChildCount(); buttonIndex++) {
            Button gridButton = (Button) mLightGrid.getChildAt(buttonIndex);
            gridButton.setOnClickListener(this::onLightButtonClick);
        }

        mLightOnColor = ContextCompat.getColor(this, R.color.yellow);
        mLightOffColor = ContextCompat.getColor(this, R.color.black);
        mLightOffText = R.string.lightOffText;
        mLightOnText = R.string.lightOnText;

        mGame = new LightsOutGame();
        if (savedInstanceState == null) {
            startGame();
        }
        else {
            String gameState = savedInstanceState.getString(GAME_STATE);
            mGame.setState(gameState);
            setButtonColors();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GAME_STATE, mGame.getState());
    }

    private void startGame() {
        mGame.newGame();
        setButtonColors();
    }

    private void onLightButtonClick(View view) {

        // Find the button's row and col
        int buttonIndex = mLightGrid.indexOfChild(view);
        int row = buttonIndex / LightsOutGame.GRID_SIZE;
        int col = buttonIndex % LightsOutGame.GRID_SIZE;

        mGame.selectLight(row, col);
        setButtonColors();

        // Congratulate the user if the game is over
        if (mGame.isGameOver()) {
            Toast.makeText(this, R.string.congrats, Toast.LENGTH_SHORT).show();
        }
    }

    private void setButtonColors() {

        for (int buttonIndex = 0; buttonIndex < mLightGrid.getChildCount(); buttonIndex++) {
            Button gridButton = (Button) mLightGrid.getChildAt(buttonIndex);

            // Find the button's row and col
            int row = buttonIndex / LightsOutGame.GRID_SIZE;
            int col = buttonIndex % LightsOutGame.GRID_SIZE;

            if (mGame.isLightOn(row, col)) {
                gridButton.setBackgroundColor(mLightOnColor);
                gridButton.setText(mLightOnText);
                gridButton.setTextColor(mLightOffColor);
            } else {
                gridButton.setBackgroundColor(mLightOffColor);
                gridButton.setText(mLightOffText);
                gridButton.setTextColor(mLightOnColor);
            }
        }
    }

    public void onNewGameClick(View view) {
        startGame();
    }
}