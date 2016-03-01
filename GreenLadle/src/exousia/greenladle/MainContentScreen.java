package exousia.greenladle;


import com.facebook.FacebookSdk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import exousia.greenladlemain.DeliveryDetail;
import exousia.greenladlemain.DisheshHelperClass;
import exousia.greenladlemain.SignOutClass;
import exousia.intro.LoginActivity;

public class MainContentScreen extends AppCompatActivity implements OnClickListener {

	Toolbar toolbar;
	DrawerLayout mdrawerlayout;
	LinearLayout mainContent;
	
	int frag;
	int number_of_dishes_permission;
	DisheshHelperClass database;
	int totalItemcount;
	TextView itemCountTextView = null;
	RelativeLayout actionMenuItemCount;
	Fragment fragment;
	FragmentTransaction transaction;
	SharedPreferences recordsFile;
	Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.activity_main_content_screen);

		database = new DisheshHelperClass(MainContentScreen.this);

		recordsFile=getSharedPreferences("My_records",0);
	    editor=recordsFile.edit();	

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Home");
		mdrawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		mainContent = (LinearLayout) findViewById(R.id.mainview);

		// sending the view to navigationdrwaer class so that it will left
		// drawer
		// open and close function can be handled
		frag = R.id.left_drawer;

		// making navigation drawer object and calling actionbartoggle to
		// to sync activity, actionbar and left drawer
		NavigationDrawer navDrawer = ((NavigationDrawer) getSupportFragmentManager()
				.findFragmentById(R.id.left_drawer));
		navDrawer.actionbarToogler(this, mainContent, mdrawerlayout, toolbar, frag);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Toast.makeText(MainContentScreen.this, "onResume", Toast.LENGTH_SHORT).show();

		
		totalItemcount = database.getTotalCount();
		if (itemCountTextView != null)
			itemCountTextView.setText("" + totalItemcount);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// Toast.makeText(MainContentScreen.this,"onCreateOption",Toast.LENGTH_SHORT).show();
		getMenuInflater().inflate(R.menu.main_content_screen, menu);
		View v = menu.findItem(R.id.basket_actionbar).getActionView();

		actionMenuItemCount = (RelativeLayout) v.findViewById(R.id.menutiem_count_container);
		actionMenuItemCount.setOnClickListener(this);

		itemCountTextView = (TextView) v.findViewById(R.id.item_count);
		itemCountTextView.setText("" + totalItemcount);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_signout) {
			
			SignOutClass signout=new SignOutClass(MainContentScreen.this);
			signout.signOutFromAccount();
			Intent intent = new Intent(MainContentScreen.this,LoginActivity.class);
			startActivity(intent);
			finish();
			return true;
		}

		if (id == R.id.action_dropdown1) {
			//Toast.makeText(MainContentScreen.this, "onResume", Toast.LENGTH_SHORT).show();
			editor.putString("filter_option","All");
			editor.commit();
			//fragment=new MainHomeFragment();			
			applyFilter();
			return true;
		}
		if (id == R.id.action_dropdown2) {
			editor.putString("filter_option","Veg");
			editor.commit();
			applyFilter();
			return true;
		}
		if (id == R.id.action_dropdown3) {
			editor.putString("filter_option","Non_Veg");
			editor.commit();
            applyFilter();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			SQLiteDatabase db = new DisheshHelperClass(MainContentScreen.this).getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from dishes_detail", null);
			// Toast.makeText(this,"cursor "+cursor.getCount(),
			// Toast.LENGTH_LONG).show();
			if (cursor.moveToFirst()) {

				AlertDialog.Builder alertbox = new AlertDialog.Builder(MainContentScreen.this);
				alertbox.setCancelable(false);
				alertbox.setTitle("Your Basket will get deleted");
				alertbox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

						// finish used for destroyed activity
						DisheshHelperClass database = new DisheshHelperClass(MainContentScreen.this);
						database.deleteTableDishes();
						editor.putInt("number_of_dishes_selected_till_now", 0);
						editor.commit();
						finish();
					}
				});

				alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int arg1) {
						// Nothing will be happened when clicked on no button
						dialog.dismiss();
						// of Dialog

					}
				});

				alertbox.show();
			}

			else
				finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainContentScreen.this, DeliveryDetail.class);
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		editor.putString("filter_option","All");
		editor.commit();
		
	}
	
	public void applyFilter()
	{
		//fragment=new MainHomeFragment();			
		transaction=getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.content_frame,fragment);
		transaction.commit();		
	}

}
