package exousia.greenladleotp;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.android.volley.manager.RequestManager.RequestListener;
import com.android.volley.manager.RequestMap;

import exousia.greenladle.R;
//import com.android.volley.toolbox.StringRequest;

public class OtpRegisteration extends AppCompatActivity implements OnClickListener,RequestListener {

	ImageView next, animatedBtn;
	String TAG="OtpRequest";
	EditText mobileNumber;
	SharedPreferences recordsFile;
	Editor editor;
	AnimationDrawable frameAnimation;
	//StringRequest stringRequest;
	int randomNumber;
	LoadController mLoadController;
	
	RequestQueue requestQueue;
	
	String url="http://www.greenladle.com/lader-api/request_otp/index.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_otp_registration);

		next = (ImageView) findViewById(R.id.next);
		next.setOnClickListener(this);
		animatedBtn = (ImageView) findViewById(R.id.animatedbtn);
		
		recordsFile=getSharedPreferences("My_records",0);
	    editor=recordsFile.edit();		

		mobileNumber = (EditText) findViewById(R.id.mobile_number);

	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.next:
			
			hideKeyBoard(v);
			if (!validateNumber()) {
				mobileNumber.setError("Enter Valid Mobile Number");
			}

			else {
				
				next.setVisibility(View.GONE);
				animatedBtn.setVisibility(View.VISIBLE);
				animatedBtn.setBackgroundResource(R.anim.verification_btn_anim);
				frameAnimation = (AnimationDrawable) animatedBtn.getBackground();
				frameAnimation.start();
				showAnimationForFewSecond();
				
			}
			break;

		default:
			break;
		}
	}

	private void hideKeyBoard(View v) {
		// TODO Auto-generated method stub
		((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(),0);
	}

	private void showAnimationForFewSecond() {
		// TODO Auto-generated method stub
		TimerTask task = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
            	sendOtp();
            }
        };
        Timer t = new Timer();
        t.schedule(task, 2000);
	}

	public boolean validateNumber() {
		if (mobileNumber.length() > 10 || mobileNumber.length() < 10) {

			return false;
		}

		else {

			return true;
		}
	}
	
	
	
	public int getRandomNumber() {
		// TODO Auto-generated method stub			
		int randomNumber=(int)((Math.random()*9000)+1000);
		return randomNumber;
	}
	
	private void StartAnotherActivity() {
		// TODO Auto-generated method stub
		
		
		TimerTask task = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
            	Intent intent = new Intent(OtpRegisteration.this, OtpActivity.class);
            	intent.putExtra("Otp",randomNumber);
            	intent.putExtra("phoneNumber",mobileNumber.getText().toString());
    			startActivity(intent);
    			finish();
            }
        };
        Timer t = new Timer();
        t.schedule(task, 2000);
		
	}
	
private void sendOtp()
{
	RequestMap params = new RequestMap();    
    params.put("To",mobileNumber.getText().toString());
  	params.put("Mode","Send"); 
  	randomNumber=getRandomNumber();
  	params.put("Otp",randomNumber+"");
    mLoadController = RequestManager.getInstance().post(url, params, this, 2);

}



@Override
public void onError(String arg0, String arg1, int arg2) {
	// TODO Auto-generated method stub
	
}



@Override
public void onRequest() {
	// TODO Auto-generated method stub
	
}



@Override
public void onSuccess(String arg0, Map<String, String> arg1, String arg2,
		int arg3) {
	// TODO Auto-generated method stub
	frameAnimation.stop();
	next.setVisibility(View.VISIBLE);
	next.setImageResource(R.drawable.l_);
	animatedBtn.setVisibility(View.GONE);
	StartAnotherActivity();
}
}
