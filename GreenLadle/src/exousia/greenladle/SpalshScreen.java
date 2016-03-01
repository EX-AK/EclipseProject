package exousia.greenladle;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import exousia.greenladleotp.OtpRegisteration;
import exousia.intro.LoginActivity;
import exousia.intro.MainAppIntro;

public class SpalshScreen extends AppCompatActivity {

	SharedPreferences recordsFile;
	Editor editor;
	boolean IS_USER_LOGGED_IN_ALREADY = false;
	boolean IS_USER_ALREADY_REGISTERED=false;
	Intent intent;
	boolean exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	Crashlytics.start(this);
		setContentView(R.layout.activity_spalsh_screen);		
		
		recordsFile = getSharedPreferences("My_records", 0);
		editor = recordsFile.edit();
		
		IS_USER_ALREADY_REGISTERED=getRegistrationStatus();

		int val = recordsFile.getInt("userAware", 0);

		intent = getIntent();
		exit = intent.getBooleanExtra("exit", false);

		if (exit)
			finish();

		else {

			boolean USER_AWARE;
			if (val == 0)
				USER_AWARE = false;
			else
				USER_AWARE = true;

			if (USER_AWARE) {

                             if(IS_USER_ALREADY_REGISTERED)
                             {
                 				IS_USER_LOGGED_IN_ALREADY = checkLoginStatus();             
                				new Handler().postDelayed(new Runnable() {

                					@Override
                					public void run() {
                						// TODO Auto-generated method stub

                						if (IS_USER_LOGGED_IN_ALREADY) {
                							Intent intent = new Intent(SpalshScreen.this, HomeActivity.class);// ,MainContentScreen.class);
                							startActivity(intent);
                							finish();
                						} else {
                							Intent intent = new Intent(SpalshScreen.this, LoginActivity.class);// ,MainContentScreen.class);
                							startActivity(intent);
                							finish();
                						}
                					}
                				}, 3000);
                             }
                             else
                             {
                            	 Intent intent = new Intent(SpalshScreen.this, OtpRegisteration.class);// ,MainContentScreen.class);
     							startActivity(intent);
     							finish();
                             }
			}

			else

			{
				editor.putInt("userAware", 1);
				editor.commit();
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SpalshScreen.this, MainAppIntro.class);
						startActivity(intent);
						finish();
					}
				}, 3000);
			}
		}

	}
 

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	public boolean checkLoginStatus() {

		if (recordsFile.getInt("isLoggedIn", -1) == 1) {
			
				return true;
		}
		else
		return false;
	}
	
	public boolean getRegistrationStatus()
	{
		if(recordsFile.getInt("isUserRegistered",0)==1)
			return true;
		else
			return false;
	}

}
