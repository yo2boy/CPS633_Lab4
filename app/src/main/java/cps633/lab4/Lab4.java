package cps633.lab4;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/* Controller class to the user interface found under /res/layout/main */
public class Lab4 extends Activity {

    Button b;
    TextView textView;
    int count = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        b = (Button) findViewById(R.id.button1);
    }

    public void button(View view) {

        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Pressed!");

        textView.setTextSize(100);

        count++;
        if(count == 1)
            textView.setTextColor(Color.CYAN);
        else if (count == 2)
            textView.setTextColor(Color.RED);
        else if (count == 3)
            textView.setTextColor(Color.YELLOW);
        else if (count == 4) {
            textView.setTextColor(Color.MAGENTA);
            count = 1;
        }
    }
}