package exousia.greenladle;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.android.volley.manager.RequestManager.RequestListener;
import com.android.volley.manager.RequestMap;

import exousia.greenladlemain.DeliveryDetail;

public class GreenLadle extends AppCompatActivity implements OnClickListener,RequestListener {

	JSONObject deliveryDetailJsonObject;
	EditText userName, userContactNumber, userStreetAdd, userAptOrSuiteAdd
			,cityPostCode;
	Spinner userTownCityAdd;
	SharedPreferences recordsFile;
	Editor editor;
	Button sendDeliveryDetailToServer;
	String userId;
	LoadController mLoadController;
	ArrayAdapter<CharSequence> cityNames;
	RadioButton cashOnDelivery;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.checkout_details);
		recordsFile = getSharedPreferences("My_records", 0);
		userName = (EditText) findViewById(R.id.checkoutNameEditText);
		userName.setFocusable(false);
		userContactNumber = (EditText) findViewById(R.id.checkoutPhoneNumberEditText);
		userContactNumber.setFocusable(false);
		userStreetAdd = (EditText) findViewById(R.id.checkoutStreetAddressEditText);
		userAptOrSuiteAdd = (EditText) findViewById(R.id.checkoutHouseNumAddressEditText);
		cityNames=ArrayAdapter.createFromResource(this,R.array.cityName,android.R.layout.simple_spinner_item);
		cityNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		userTownCityAdd = (Spinner) findViewById(R.id.townCitySpinner);
		userTownCityAdd.setAdapter(cityNames);		
		cityPostCode = (EditText) findViewById(R.id.checkoutPstCodeEditText);
		sendDeliveryDetailToServer = (Button) findViewById(R.id.PlaceOrderBtn);
		sendDeliveryDetailToServer.setOnClickListener(this);
        cashOnDelivery=(RadioButton)findViewById(R.id.cashOnDelivery);
		getJsonObjectFromIntent();
		setSavedValueFromPreferenceToEditText();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.green_ladle, menu);
		return true;
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

		if (id == android.R.id.home) {
			Intent intent = new Intent(this, DeliveryDetail.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void getJsonObjectFromIntent() {
		// TODO Auto-generated method stub
		try {
			deliveryDetailJsonObject = new JSONObject(getIntent().getExtras()
					.getString("deliverDetail"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private void setSavedValueFromPreferenceToEditText() {
		// TODO Auto-generated method stub
		String name = recordsFile.getString("username", "Unknown");
		String phoneNumber = recordsFile.getString("phoneNumber", "Unknown");
		userId = recordsFile.getString("userid", "Unknown");
		userName.setText(name);
		userContactNumber.setText(phoneNumber);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (allFieldsAreFilled()) {
			addRequiredDetailToJsonArray();
		} else {
			Toast.makeText(this, "Please Fill The Required Fields",
					Toast.LENGTH_SHORT).show();
		}
	}

	private boolean allFieldsAreFilled() {
		// TODO Auto-generated method stub		
		if (TextUtils.isEmpty(userName.getText())
				|| TextUtils.isEmpty(userContactNumber.getText())
				|| TextUtils.isEmpty(userStreetAdd.getText())
				|| TextUtils.isEmpty(userAptOrSuiteAdd.getText())
				|| userTownCityAdd.getSelectedItemPosition()==0
				||!cashOnDelivery.isChecked()
				|| TextUtils.isEmpty(cityPostCode.getText())) {
			return false;
		} else
			return true;
	}

	private void addRequiredDetailToJsonArray() {
		// TODO Auto-generated method stub

		try {
			deliveryDetailJsonObject.put("userName", userName.getText());
			deliveryDetailJsonObject.put("userContactNumber",
					userContactNumber.getText());
			deliveryDetailJsonObject.put("userStreetAdd",
					userStreetAdd.getText());
			deliveryDetailJsonObject.put("userAptOrSuiteAdd",
					userAptOrSuiteAdd.getText());
			deliveryDetailJsonObject.put("userTownCityAdd",
					userTownCityAdd.getSelectedItem());
			deliveryDetailJsonObject
					.put("cityPostCode", cityPostCode.getText());
			deliveryDetailJsonObject
					.put("cityPostCode", cityPostCode.getText());
			deliveryDetailJsonObject.put("userId", userId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		sendDeliveryDetail();
	}
	
	public void sendDeliveryDetail()
	{
		RequestMap params=new RequestMap();
		params.put("checkout",deliveryDetailJsonObject.toString());
		mLoadController=RequestManager.getInstance().post("http://www.greenladle.com/lader-api/checkout/checkout.php",params,this,2);
		
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
	public void onSuccess(String response, Map<String, String> arg1, String arg2,
			int arg3) {
		// TODO Auto-generated method stub
		if(response.equals("\"Order placed Successfully\""))
		{
			Intent intent=new Intent(this,ThankyouActivity.class);
			startActivity(intent);
			finish();
			
		}
		
	}
}
