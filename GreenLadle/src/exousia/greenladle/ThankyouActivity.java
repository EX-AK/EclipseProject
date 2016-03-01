package exousia.greenladle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ThankyouActivity extends AppCompatActivity implements OnClickListener{
	Button homeBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.thankyou_activity_layout);
	    
		homeBtn=(Button) findViewById(R.id.HomeBtn);
		homeBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	startHomeActivity();
	}
	
	private void startHomeActivity()
	{
		Intent intent=new Intent(this,HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			startHomeActivity();
			return true;
		}
		
		return false;
		
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
        startHomeActivity();
		}
		return super.onOptionsItemSelected(item);
	}

}
