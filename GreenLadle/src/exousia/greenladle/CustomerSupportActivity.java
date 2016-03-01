package exousia.greenladle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomerSupportActivity extends AppCompatActivity implements
		OnClickListener {

	Toolbar toolbar;
	DrawerLayout mdrawerlayout;
	LinearLayout mainContent;
	int frag;
	TextView textview1, textview2, textview3, textview4, textview5, textview6;
	Button Email, call;
	final int MY_PERMISSIONS_REQUEST_CALL=101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_support);
		checkPermssionGrantedORNot();

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Customer");

		mdrawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		mainContent = (LinearLayout) findViewById(R.id.mainview);
		frag = R.id.left_drawer;

		textview1 = (TextView) findViewById(R.id.textView1);
		textview4 = (TextView) findViewById(R.id.textView4);
		textview5 = (TextView) findViewById(R.id.textView5);

		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/ubuntu.ttf");
		textview1.setTypeface(face);
		textview4.setTypeface(face);
		textview5.setTypeface(face);
		Email = (Button) findViewById(R.id.Email_btn);
		Email.setOnClickListener(this);

		call = (Button) findViewById(R.id.Callus_btn);
		call.setOnClickListener(this);

		NavigationDrawer navDrawer = ((NavigationDrawer) getSupportFragmentManager()
				.findFragmentById(R.id.left_drawer));
		navDrawer.actionbarToogler(this, mainContent, mdrawerlayout, toolbar,
				frag);

	}

	private void checkPermssionGrantedORNot() {
		// TODO Auto-generated method stub
		String permission = Manifest.permission.CALL_PHONE;
		if(ContextCompat.checkSelfPermission(this,permission)!=PackageManager.PERMISSION_GRANTED )
		{
			ActivityCompat.requestPermissions(this,
	                new String[]{permission},
	                MY_PERMISSIONS_REQUEST_CALL); 
		}

	}
	
	@Override 
	public void onRequestPermissionsResult(int requestCode,
	        String permissions[], int[] grantResults) {
	    switch (requestCode) {
	        case MY_PERMISSIONS_REQUEST_CALL: { 
	            // If request is cancelled, the result arrays are empty. 
	            if (grantResults.length > 0
	                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
	             call.setClickable(true);
	 
	            } else { 
	 
	            	call.setClickable(false);
	            } 
	            return; 
	        } 
	 

	    } 
	} 

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (R.id.Email_btn == v.getId()) {
			sendEmail();
		}

		if (R.id.Callus_btn == v.getId()) {

			call();
		}

	}

	private void sendEmail() {
		// TODO Auto-generated method stub
		String[] TO = { "contact@exousia.in" };
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setData(Uri.parse("mailto:"));
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}

	private void call() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_CALL,
				Uri.parse("tel:08699577845"));
		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.customer_support, menu);
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
		return super.onOptionsItemSelected(item);
	}
}
