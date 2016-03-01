package exousia.greenladleotp;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import exousia.greenladle.R;
import exousia.greenladleotp.SmsDetector.UiUpdater;
import exousia.intro.LoginActivity;

public class OtpActivity extends AppCompatActivity implements OnClickListener,UiUpdater {

	EditText otpDigit1, otpDigit2, otpDigit3, otpDigit4;
	ImageView otpVerificatonBtn, animatedBtn;
	String otpNumber;
	AnimationDrawable frameAnimation;
	String TAG="OtpToVerify";
	
	SharedPreferences recordFile;
    Editor editor;
    
    Intent intent;
    
    int validRandomNumber;
    String phoneNumber;
    
    Button resendOtpBtn;
    SmsDetector smsDetector;
    
    TextView phoneNumberTextView,timer;
    Runnable timerRunnable;
    int timerCounter=120;
    Handler timerHandler;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_otp);
		
		intent=getIntent();
		validRandomNumber=intent.getIntExtra("Otp",0);	
		phoneNumber=intent.getStringExtra("phoneNumber");
		
		timer=(TextView) findViewById(R.id.timer);
		
		phoneNumberTextView=(TextView) findViewById(R.id.phone_number);
		phoneNumberTextView.setText("+91"+phoneNumber);
		
		recordFile = getSharedPreferences("My_records", 0);
		editor = recordFile.edit();

		timerHandler=new Handler();
		timerRunnable=new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				timerCounter--;
				timerController();
			}
		};
		
		otpDigit1 = (EditText) findViewById(R.id.otp_verification_number_1);
		otpDigit1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (otpDigit1.length() == 1) {
					otpDigit1.clearFocus();
					otpDigit2.requestFocus();
					otpDigit2.setCursorVisible(true);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		otpDigit2 = (EditText) findViewById(R.id.otp_verification_number_2);
		otpDigit2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (otpDigit2.length() == 1) {
					otpDigit2.clearFocus();
					otpDigit3.requestFocus();
					otpDigit3.setCursorVisible(true);
				}
				if (otpDigit2.length() == 0) {
					otpDigit2.clearFocus();
					otpDigit1.requestFocus();
					otpDigit1.setCursorVisible(true);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		otpDigit3 = (EditText) findViewById(R.id.otp_verification_number_3);
		otpDigit3.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (otpDigit3.length() == 1) {
					otpDigit3.clearFocus();
					otpDigit4.requestFocus();
					otpDigit4.setCursorVisible(true);
				}
				if (otpDigit3.length() == 0) {
					otpDigit3.clearFocus();
					otpDigit2.requestFocus();
					otpDigit2.setCursorVisible(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		otpDigit4 = (EditText) findViewById(R.id.otp_verification_number_4);
		otpDigit4.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (otpDigit4.length() == 0) {
					otpDigit4.clearFocus();
					otpDigit3.requestFocus();
					otpDigit3.setCursorVisible(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		otpVerificatonBtn = (ImageView) findViewById(R.id.ok_otp_verfied);
		otpVerificatonBtn.setOnClickListener(this);

		animatedBtn = (ImageView) findViewById(R.id.animatedbtn);
		
	     timerHandler.postDelayed(timerRunnable,1000);
		
		resendOtpBtn=(Button) findViewById(R.id.resend_btn);
		resendOtpBtn.setOnClickListener(this);

	}
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	smsDetector=new SmsDetector(this);
    	IntentFilter intentFilter=new IntentFilter();
    	intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
    	registerReceiver(smsDetector, intentFilter);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.ok_otp_verfied) {
			hideKeyBoard(v);
			if (otpDigit1.length() == 0 || otpDigit2.length() == 0 || otpDigit3.length() == 0
					|| otpDigit4.length() == 0) {

				Toast.makeText(OtpActivity.this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
			}

			else {
				otpVerificatonBtn.setVisibility(View.GONE);
				animatedBtn.setVisibility(View.VISIBLE);
				animatedBtn.setBackgroundResource(R.anim.verification_btn_anim);
				frameAnimation = (AnimationDrawable) animatedBtn.getBackground();
				frameAnimation.start();				
				otpNumber =(otpDigit1.getText()).toString()+otpDigit2.getText()+otpDigit3.getText()+otpDigit4.getText();
				//addRequestToQueue();
				TimerTask task=new TimerTask() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						frameAnimation.stop();					

						validateOtp();
					}
				};
				
				Timer time=new Timer();
				time.schedule(task,3000);
			}
		}
		
		if(v.getId()==R.id.resend_btn)
		{
			Intent intent=new Intent(this,OtpRegisteration.class);
			startActivity(intent);
			finish();
		}
	}
	
	public void validateOtp()
	{
		Log.e("Valid Otp",validRandomNumber+" "+otpNumber);
		if(validRandomNumber==Integer.parseInt(otpNumber))
		{
			editor.putInt("isUserRegistered", 1);
			editor.putString("phoneNumber",phoneNumber);			
			editor.commit();
			startAnotherActivity();
		}
		else
		{
			OtpActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					otpVerificatonBtn.setVisibility(View.VISIBLE);
					animatedBtn.setVisibility(View.GONE);
					Toast.makeText(OtpActivity.this,"You Entered A Wrong Otp",Toast.LENGTH_SHORT).show();	
				}
			});
		}
	}
	

	
	private void startAnotherActivity()
	{
		Intent intent=new Intent(OtpActivity.this,LoginActivity.class);
		
	    startActivity(intent); 
	    finish();
	}

	private void hideKeyBoard(View v) {
		// TODO Auto-generated method stub
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	@Override
	public void setTextView(String message) {
		// TODO Auto-generated method stub
		timerHandler.removeCallbacks(timerRunnable);
		timer.setText("0:00");
		String number[]=message.split("");
	    otpDigit1.setText(number[1]);
		otpDigit2.setText(number[2]);
		otpDigit3.setText(number[3]);
		otpDigit4.setText(number[4]);
		otpVerificatonBtn.setEnabled(true);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timerHandler.removeCallbacks(timerRunnable);
		if(smsDetector==null)
		{}
		else
		{
			unregisterReceiver(smsDetector);
		}
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//timerHandler.removeCallbacks(timerRunnable);
	}
	
	public void timerController()
	{
		if(timerCounter>0)
		{
			if(timerCounter>60)
			{
				
				    if((timerCounter-60)==60)
				    {
				    	
				    	timer.setText("2:00");
				    }
				    else
				    {
				    	if((timerCounter-60)<10)
				    	{
				    		timer.setText("1:0"+(timerCounter-60));	
				    	}
				    	else
				    	{
				    		timer.setText("1:"+(timerCounter-60));
				    	}
				    	
				    }
			}
			else
			{
				if((timerCounter-60)==0)
			    {
					
			    	timer.setText("1:00");
			    }
				else
			    {
			    	if((timerCounter)<10)
			    	{
			    		timer.setText("0:0"+(timerCounter));	
			    	}
			    	else
			    	{
			    		timer.setText("0:"+(timerCounter));
			    	}
			    	
			    }
			}
			timerHandler.postDelayed(timerRunnable,1000);
		}
		
		else
		{
			timer.setText("0:00");
			resendOtpBtn.setVisibility(View.VISIBLE);
			timer.setVisibility(View.GONE);
			
		}
	}


}