package exousia.greenladlemain;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;

public class SignOutClass {
	
	ApplicationMain getMgoogleApiClient;
	GoogleApiClient mGoogleApiClient;
	
	SharedPreferences recordsFile;
	Editor editor;
	
	public SignOutClass(Context context) {
		// TODO Auto-generated constructor stub
	recordsFile=context.getSharedPreferences("My_records",0);
    editor=recordsFile.edit();	
	}
	
	public void signOutFromAccount()
	{
		if(recordsFile.getString("account_used","unknown").equals("fb"))
		{   
          
			LoginManager.getInstance().logOut();
			editor.putInt("isLoggedIn",0);
			editor.commit();			
		}
		else if(recordsFile.getString("account_used","unknown").equals("normal"))
		{			
			editor.putInt("isLoggedIn",0);
			editor.commit();
		}
			
	}


}
