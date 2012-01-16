package code.sas_server.com;


import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.widget.Toast;


//This class is attached to activity via manifest xml, now it is attached to ServerPart
//Can be attached to any activity
public class SAS_Server_Sms_Receiver extends BroadcastReceiver 
{
	
	public static final String SMS_EXTRA_NAME = "pdus";
	
	public static final String SMS_URI = "content://sms";
	
	public static final String ADDRESS = "address";
   
    public static final String DATE = "date";
 
    public static final String BODY = "body";
   
  
       
	public void onReceive( Context context, Intent intent ) 
	{
		String body    = "";
		String address = "";
		// Get SMS map from Intent
        Bundle extras = intent.getExtras();
        
        String messages = "";
        
        if ( extras != null )
        {
        	
            // Get received SMS array
            Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );
            
         
            
            for ( int i = 0; i < smsExtra.length; ++i )
            {
            	SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
            	
            	//this.abortBroadcast(); 
            	
                body= sms.getMessageBody().toString();
                address = sms.getOriginatingAddress();
      
                
                messages += "SMS from :" + address + " \n";                    
                messages += body + "\n";
                
               
                
               // putSmsToDatabase( contentResolver, sms );
            }
            if( !parsSMS(body,context))
            	return;
         
            
          //Alert on emergency received.
          ((Vibrator)context.getSystemService(context.VIBRATOR_SERVICE)).vibrate(3000l);
          
          for(int i = 0; i < 3 ; i++)
          {
            Toast.makeText( context, messages, Toast.LENGTH_SHORT ).show();
          }
        }
        
       
	}
	
	
	//--------------------------------------------------------------------------------
	
	public boolean parsSMS(String sms,Context c)
	{
		
		if(sms.length() < 4)
			return false;
		String aux = sms.substring(0,4);
		//Toast.makeText( c,aux, Toast.LENGTH_SHORT ).show();
		if(!aux.equals("SAS1"))
			return false;
		return true;
	}
}
