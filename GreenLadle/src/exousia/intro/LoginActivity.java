package exousia.intro;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.android.volley.manager.RequestManager.RequestListener;
import com.android.volley.manager.RequestMap;
//import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import exousia.greenladle.HomeActivity;
import exousia.greenladle.MainContentScreen;
import exousia.greenladle.R;
import exousia.greenladlemain.ApplicationMain;
import exousia.greenladlemain.VolleySingleTon;
import exousia.utils.InternetConnectinListener;

public class LoginActivity extends AppCompatActivity implements
		 OnClickListener,
		AnimatorListener, RequestListener {

	ImageView fb_btn;

	Button loginBtn, registerBtn, loginFormBtn, registrationForm;

	LinearLayout emailLayout;

	EditText email, fullname, password;

	TextView emailTextView;

	RequestQueue volleyRequest;

	String nonce;

	AccessToken accessToken;

	String userName, userEmail,userPassword;
	Intent intent;
	Dialog blockUser;

	InternetConnectinListener internetConnectionListener;

	private CallbackManager callbackManager;
	ApplicationMain mApp;
	SharedPreferences recordsFile;
	Editor editor;
	int flag = 0;
	boolean EVENT_FROM_LOGINBTN = false;

	boolean IS_FACEBOOK = false;
	LoadController mLoadController;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.activity_login);

		recordsFile = getSharedPreferences("My_records", 0);
		editor = recordsFile.edit();
		
		mApp = (ApplicationMain) getApplicationContext();

		volleyRequest = VolleySingleTon.getInstance().getRequestQueue();

		intent = new Intent(LoginActivity.this, MainContentScreen.class);

		callbackManager = CallbackManager.Factory.create();
		fb_btn = (ImageView) findViewById(R.id.facebook_btn);

		fb_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IS_FACEBOOK = true;
				blockUser.show();
				fbLogin();

			}
		});
		
		email = (EditText) findViewById(R.id.email_edit_text);
		password = (EditText) findViewById(R.id.password_edit_text);
		fullname = (EditText) findViewById(R.id.fullname_edit_text);
		setUserName();
		emailLayout = (LinearLayout) findViewById(R.id.email_layout);

		loginFormBtn = (Button) findViewById(R.id.member_login_from);
		loginFormBtn.setOnClickListener(this);

		registrationForm = (Button) findViewById(R.id.register_login_form);
		registrationForm.setOnClickListener(this);

		registerBtn = (Button) findViewById(R.id.register_btn);
		registerBtn.setOnClickListener(this);

		loginBtn = (Button) findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(this);

		emailTextView = (TextView) findViewById(R.id.email_textview);
		blockUser=createDialogObject();	
		//T4Kyd2+WHHdK2qxlce/6ubxQ/yQ=
	}

	private void setUserName() {
		// TODO Auto-generated method stub
		if(recordsFile.getString("phoneNumber","xx").equals("xx"))
		{
			
		}
		else
		{
			fullname.setText(recordsFile.getString("phoneNumber","xx"));
		}
		
	}

	private Dialog createDialogObject() {
		// TODO Auto-generated method stub
		Dialog blockUserDialog=new Dialog(this);
		blockUserDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		blockUserDialog.setCancelable(false);
		blockUserDialog.getWindow().setBackgroundDrawable(getDrawable(android.R.color.transparent));
		blockUserDialog.setContentView(R.layout.dialog_layout_block_user);
		return blockUserDialog;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		internetConnectionListener = new InternetConnectinListener();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(internetConnectionListener, intentFilter);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
			callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(internetConnectionListener);
		internetConnectionListener.dismissDialog();
	}

	public void fbLogin() {

		LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
				Arrays.asList("public_profile", "email", "user_birthday"));
		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {

					@Override
					public void onSuccess(LoginResult result) {
						// TODO Auto-generated method stub

						GraphRequest request = GraphRequest.newMeRequest(
								result.getAccessToken(),
								new GraphJSONObjectCallback() {

									@Override
									public void onCompleted(JSONObject object,
											GraphResponse response) {
										// TODO Auto-generated method stub
										try {
											userPassword=object.getString("id");
											String profilePicUrl = "https://graph.facebook.com/"
													+ userPassword
													+ "/picture?type=large";											
											if (object.has("email")) {
												userEmail = object
														.getString("email");
												editor.putString("email",
														userEmail);
												editor.commit();
											}

											userName = object
													.getString("name");
											editor.putString("username",
													userName);
											
											editor.putString("userFbId",
													userPassword);
											editor.putString("profile_pic_uri",
													profilePicUrl);
											editor.putString("account_used",
													"fb");
											editor.commit();
											IS_FACEBOOK=true;
											if(userEmail!=null)
									         registerUserForGreenLadle();
											else
											{
												blockUser.dismiss();
												Toast.makeText(LoginActivity.this,"Unable To Fetch Your Email Address, So"
														+ " Kindly Register Manually",Toast.LENGTH_SHORT).show();
											}

										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});

						Bundle parameters = new Bundle();
						parameters.putString("fields",
								"id,name,email,gender, birthday");
						request.setParameters(parameters);
						request.executeAsync();
					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						blockUser.dismiss();
						Toast.makeText(LoginActivity.this,"Facebook Login Cancled",Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(FacebookException error) {
						// TODO Auto-generated method stub
						blockUser.dismiss();
						Toast.makeText(LoginActivity.this,""+error.getMessage(),Toast.LENGTH_SHORT).show();
					}
				});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register_login_form:
			handleForRegistrationFormBtn();
			break;
		case R.id.member_login_from:
			handleForLoginFormBtn();
			break;
		case R.id.register_btn:
			if (checkFormCompletness()) {
				blockUser.show();
				registerUserForGreenLadle();
			}
			else
			{
				Toast.makeText(LoginActivity.this,"Completely Fill the Fields",Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.login_btn:
			if (checkFormCompletnessBeforeLogin()) {
                EVENT_FROM_LOGINBTN=true;
                blockUser.show();
				loginUserInGreenLadle();
			}
			else
			{
				Toast.makeText(LoginActivity.this,"Completely Fill the Fields",Toast.LENGTH_SHORT).show();
			}
			break;
		}
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	private boolean checkFormCompletness() {
		if (email.length() > 0 && password.length() > 0
				&& fullname.length() > 0) {
			userEmail=email.getText()+"";
			userName=fullname.getText()+"";
			userPassword=password.getText()+"";
			return true;
		}
		return false;
	}

	private boolean checkFormCompletnessBeforeLogin() {
		if (password.length() > 0 && fullname.length() > 0) {
			userName=fullname.getText()+"";
			userPassword=password.getText()+"";
			return true;
		}
		return false;
	}

	private void handleForLoginFormBtn() {
		// TODO Auto-generated method stub
		EVENT_FROM_LOGINBTN = true;
		emailLayout.animate().alpha(0f).setDuration(2000).setListener(this);
		emailTextView.animate().alpha(0f).setDuration(2000);

	}

	private void handleForRegistrationFormBtn() {
		// TODO Auto-generated method stub
		EVENT_FROM_LOGINBTN = false;
		emailLayout.setVisibility(View.VISIBLE);
		emailTextView.setVisibility(View.VISIBLE);
		emailLayout.animate().alpha(1f).setDuration(2000).setListener(this);
		emailTextView.animate().alpha(1f).setDuration(2000);
	}

	@Override
	public void onAnimationStart(Animator animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animator animation) {
		// TODO Auto-generated method stub
		if (EVENT_FROM_LOGINBTN) {
			emailLayout.setVisibility(View.GONE);
			registrationForm.setVisibility(View.VISIBLE);
			emailTextView.setVisibility(View.GONE);
			loginBtn.setVisibility(View.VISIBLE);
			loginFormBtn.setVisibility(View.GONE);
			registerBtn.setVisibility(View.GONE);
		} else {
			registrationForm.setVisibility(View.GONE);
			loginBtn.setVisibility(View.GONE);
			loginFormBtn.setVisibility(View.VISIBLE);
			registerBtn.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onAnimationCancel(Animator animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animator animation) {
		// TODO Auto-generated method stub

	}


	private void registerUserForGreenLadle() {
		RequestMap params = new RequestMap();
		params.put("user_name", userName);
		params.put("password", userPassword);
		params.put("user_email", userEmail);
		mLoadController = RequestManager.getInstance().post(
				"http://www.greenladle.com/lader-api/new/add_user.php", params,
				this, 2);
	}

	private void loginUserInGreenLadle() {	
		RequestMap params = new RequestMap();
		params.put("username", userName);
		params.put("password", userPassword);
		mLoadController = RequestManager
				.getInstance()
				.post("http://www.greenladle.com/get-started/?json=user/generate_auth_cookie",
						params, this, 2);
	}

	public int getRandomNumber() {
		// TODO Auto-generated method stub
		int randomNumber = (int) ((Math.random() * 9000) + 1000);
		return randomNumber;
	}

	@Override
	public void onError(String arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		blockUser.dismiss();
	}

	@Override
	public void onRequest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(String response, Map<String, String> arg1,
			String arg2, int arg3) {
		// TODO Auto-generated method stub
		blockUser.dismiss();
       if(!EVENT_FROM_LOGINBTN)
       {
   		if(response.equals("\"Email already registered\"")&&IS_FACEBOOK)
   		{
   			
   			EVENT_FROM_LOGINBTN=true;
   			loginUserInGreenLadle();
   		}
   		else if(response.equals("\"New User created successfull\""))
   		{
   			EVENT_FROM_LOGINBTN=true;
   			loginUserInGreenLadle();
   		}
   		else if(response.equals("\"Email already registered\""))
   		{
   			Toast.makeText(this,"\"Email already registered\"",Toast.LENGTH_SHORT).show();
   		}
       }
       else
       {
    	   if(!IS_FACEBOOK)
    	   {
    		   editor.putString("profile_pic_uri","unkown");
    	   } 
    		   
    	   getTheUserDetail(response);
       }

	}
	
	private void getTheUserDetail(String response)
	{
		String status;
		try {
			JSONObject userDetail=new JSONObject(response);
			   status=userDetail.getString("status");
			   if(status.equals("ok"))
			   {
				   JSONObject userBioData=userDetail.getJSONObject("user");
					 editor.putString("userid", userBioData.getString("id"));
					 editor.putString("email", userBioData.getString("email"));
					 editor.putString("username", userBioData.getString("username"));
					  if(IS_FACEBOOK)
					  {
						  editor.putString("account_used","fb");
						  editor.putInt("isLoggedIn",1);
					  }
					  else
					  {
						  editor.putString("account_used","normal"); 
						  editor.putInt("isLoggedIn",1);
					  }
					 editor.commit();					
                  
					 startAnotherActivity();
				   
			   }
			   else
			   {
				   String error=userDetail.getString("error");
				   Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
				   
			   }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void startAnotherActivity()
	{
		blockUser.dismiss();
		  Intent intent=new Intent(this,HomeActivity.class);
	         startActivity(intent);
	         finish();
	}
	
	
	

}
