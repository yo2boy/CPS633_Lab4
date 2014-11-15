package cps633.lab4;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/* Controller class to the user interface found under /res/layout/main */
public class Lab4 extends Activity {

    Button b;
    ImageView imageView;
    TextView textView;
    int count = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        b = (Button) findViewById(R.id.button1);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setVisibility(View.INVISIBLE);
    }

    public void button(View view) {

        imageView.setVisibility(View.VISIBLE);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("SUDDENLY CAGE");

        textView.setTextSize(45);

        count++;
        if(count == 1)
            textView.setTextColor(Color.CYAN);
        else if (count == 2)
            textView.setTextColor(Color.RED);
        else if (count == 3)
            textView.setTextColor(Color.YELLOW);
        else if (count == 4) {
            textView.setTextColor(Color.MAGENTA);
            count = 0;
        }
    }
}