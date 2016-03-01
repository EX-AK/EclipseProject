package exousia.greenladle;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class ReferNEarnActivity extends AppCompatActivity {

	Toolbar toolbar;
	DrawerLayout mdrawerlayout;
	LinearLayout mainContent;	
	int frag;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.refer_n_earn);
        
        toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Refer N Earn");
        
		mdrawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		mainContent = (LinearLayout) findViewById(R.id.mainview);
		frag = R.id.left_drawer;
		
		
		NavigationDrawer navDrawer = ((NavigationDrawer) getSupportFragmentManager()
				.findFragmentById(R.id.left_drawer));
		navDrawer.actionbarToogler(this, mainContent, mdrawerlayout, toolbar, frag);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.refer_nearn, menu);
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
