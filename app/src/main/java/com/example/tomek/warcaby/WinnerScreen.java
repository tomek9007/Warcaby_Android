package com.example.tomek.warcaby;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class WinnerScreen extends AppCompatActivity {

    private boolean whiteWins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winner_screen);
        showWinner();
    }

    private void showWinner() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("key");
            //The key argument here must match that used in the other activity
            TextView tv1 = (TextView)findViewById(R.id.winner);
            tv1.setText(value);
        }
    }

}
