package exousia.greenladleotp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;


public class SmsDetector extends BroadcastReceiver {

	UiUpdater updater;
	
	public SmsDetector(OtpActivity mainClass) {
		// TODO Auto-generated constructor stub
		updater=mainClass;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		
		    Bundle bundle=intent.getExtras();
		    if(bundle!=null)
		    {
		    	Object[] pdusObj = (Object[]) bundle.get("pdus");
		    	for(Object aPdusObj:pdusObj)
		    	{
		    		SmsMessage message=SmsMessage.createFromPdu((byte[])aPdusObj);
		    		String senderAddress = message.getDisplayOriginatingAddress();
                    String messageBody = message.getDisplayMessageBody();                    
                    String [] otpMessage=messageBody.split(" ");
                    String otpNumber=null;
                    if(senderAddress.equals("VM-FGRFRY"))
                    {
                    	otpNumber=otpMessage[otpMessage.length-2];
                    	  updater.setTextView(otpNumber);
                    }
                  
                    if(senderAddress.equals("AD-TFTCTOR")||senderAddress.equals("AM-TFTCTOR"))
                    {
                    	otpNumber=otpMessage[otpMessage.length-1];
                    	  updater.setTextView(otpNumber);
                    }
                    
                    	
		    	}
		    	
		    }
		
		
		
	}

	
	public interface UiUpdater
	{
		public void setTextView(String meassage);
	}
}
