package exousia.greenladle;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.manager.LoadController;
import com.android.volley.manager.RequestManager;
import com.android.volley.manager.RequestManager.RequestListener;
import com.facebook.FacebookSdk;
import com.pnikosis.materialishprogress.ProgressWheel;

import exousia.greenladlemain.DeliveryDetail;
import exousia.greenladlemain.DishDetailedInfo;
import exousia.greenladlemain.DishesInfo;
import exousia.greenladlemain.DisheshHelperClass;
import exousia.greenladlemain.RecyclerViewAdapter;
import exousia.greenladlemain.SignOutClass;
import exousia.intro.LoginActivity;

public class HomeActivity extends AppCompatActivity implements
		OnItemClickListener, OnClickListener, RequestListener {

	int currentPage = 0;
	int previous = 0;
	int number_of_dishes_selected;
	int number_of_dishes__to_select;
	Runnable Update;
	Handler handler;
	ViewPager viewPager;
	String information_of_dishes;
	ArrayList<String> dishes_name = new ArrayList<String>();
	ArrayList<Integer> dishes_id = new ArrayList<Integer>();
	ArrayList<DishesInfo> dishes_info = new ArrayList<DishesInfo>();
	EditText search;
	RecyclerView modernLst;
	RecyclerViewAdapter adapter;

	ViewGroup viewgrp;
	BaseAdapter base;
	DisheshHelperClass dishesDatabase;
	ListView dishesToBeDeliver;
	int max_selection;
	int number_of_dishes_selected_till_now = 0;
	SharedPreferences recordsFile;
	Editor editor;
	ProgressWheel wheel;
	Button checkOut;
	boolean doTransition = false;
	String filterOption;
	LinearLayoutManager layoutManager;
	ArrayList<String> imageSrcList;
	Toolbar toolbar;
	DrawerLayout mdrawerlayout;
	LinearLayout mainContent;
	int frag;
	TextView itemCountTextView = null;
	RelativeLayout actionMenuItemCount;
	int totalItemcount;

	LoadController mLoadController;
	SpannableString customToolbarTittle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		FacebookSdk.sdkInitialize(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_home_fragment);
		customToolbarTittle = new SpannableString("Home");
		customToolbarTittle.setSpan(new TypefaceSpan("fonts/greenladle.ttf"),
				0, customToolbarTittle.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(customToolbarTittle);
		recordsFile = getSharedPreferences("My_records", 0);
		editor = recordsFile.edit();
		filterOption = recordsFile.getString("filter_option", "All");

		wheel = (ProgressWheel) findViewById(R.id.progress_wheel);

		layoutManager = new LinearLayoutManager(this);

		modernLst = (RecyclerView) findViewById(R.id.restaurant_list);
		modernLst.setLayoutManager(layoutManager);
		dishesDatabase = new DisheshHelperClass(this);

		mdrawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		mainContent = (LinearLayout) findViewById(R.id.mainview);
		frag = R.id.left_drawer;

		NavigationDrawer navDrawer = ((NavigationDrawer) getSupportFragmentManager()
				.findFragmentById(R.id.left_drawer));
		navDrawer.actionbarToogler(this, mainContent, mdrawerlayout, toolbar,
				frag);

		getDishesResponse();

	}

	private void getDishesResponse() {
		// TODO Auto-generated method stub
		mLoadController = RequestManager.getInstance().get(
				"http://www.greenladle.com/lader-api/product/index.php", this,
				1);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		DisheshHelperClass database;
		database = new DisheshHelperClass(this);
		totalItemcount = database.getTotalCount();

		totalItemcount = database.getTotalCount();
		if (itemCountTextView != null)
			itemCountTextView.setText("" + totalItemcount);

		if (recordsFile.getInt("AddedItemAdapterPosition", -1) != -1) {
			notifyItemAddedInTheBasket(recordsFile.getInt(
					"AddedItemAdapterPosition", -1));
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, DeliveryDetail.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (doTransition) {
			doTransition = false;
			overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

		}
		editor.remove("AddedItemAdapterPosition");
		editor.commit();
	}

	// item click listener for list view
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		doTransition = true;
		Intent intent = new Intent(this, DishDetailedInfo.class);
		intent.putExtra("DishInfo", dishes_info.get(position));
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main_content_screen, menu);
		View v = menu.findItem(R.id.basket_actionbar).getActionView();

		actionMenuItemCount = (RelativeLayout) v
				.findViewById(R.id.menutiem_count_container);
		actionMenuItemCount.setOnClickListener(this);

		itemCountTextView = (TextView) v.findViewById(R.id.item_count);
		itemCountTextView.setText("" + totalItemcount);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		  if (id == R.id.action_signout) {
		  
		  SignOutClass signout=new SignOutClass(this);
		  signout.signOutFromAccount(); Intent intent = new
		  Intent(this,LoginActivity.class); startActivity(intent); finish();
		  return true; }
		 

		if (id == R.id.action_dropdown1) {
			if (adapter != null) {
				applyFilter("All");

				return true;
			}
		}
		if (id == R.id.action_dropdown2) {

			if (adapter != null) {
				applyFilter("Veg");
				return true;
			}

		}
		if (id == R.id.action_dropdown3) {

			if (adapter != null) {
				applyFilter("Non Veg");
				return true;
			}

		}
		return super.onOptionsItemSelected(item);
	}

	public void applyFilter(String category) {
		ArrayList<DishesInfo> filtereDishesInfo = new ArrayList<DishesInfo>();
		for (int i = 0; i < dishes_info.size(); i++) {
			if (dishes_info.get(i).getNonVegOrVeg().equals(category)
					|| category.equals("All")) {
				filtereDishesInfo.add(dishes_info.get(i));
			}
		}
		adapter.filterActivated(filtereDishesInfo);

	}

	private void notifyItemAddedInTheBasket(int int1) {
		// TODO Auto-generated method stub
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			SQLiteDatabase db = new DisheshHelperClass(HomeActivity.this)
					.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from dishes_detail", null);
			if (cursor.moveToFirst()) {

				AlertDialog.Builder alertbox = new AlertDialog.Builder(
						HomeActivity.this);
				alertbox.setCancelable(false);
				alertbox.setTitle("Your Basket will get deleted");
				alertbox.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {

								// finish used for destroyed activity
								DisheshHelperClass database = new DisheshHelperClass(
										HomeActivity.this);
								database.deleteTableDishes();
								editor.putInt(
										"number_of_dishes_selected_till_now", 0);
								editor.commit();
								finish();
							}
						});

				alertbox.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int arg1) {
								// Nothing will be happened when clicked on no
								// button
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
	public void onError(String arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		Log.e("Response", "error");
	}

	@Override
	public void onRequest() {
		// TODO Auto-generated method stub
		Log.e("Response", "request");
	}

	@Override
	public void onSuccess(String response, Map<String, String> arg1,
			String arg2, int arg3) {
		// TODO Auto-generated method stub

		wheel.setVisibility(View.GONE);
		DishesInfo dishesInfo;
		try {
			JSONObject information = new JSONObject(response);
			JSONArray productsInfo = information.getJSONArray("products");
			int size = productsInfo.length();

			for (int i = 0; i < size; i++) {

				dishesInfo = new DishesInfo();
				imageSrcList = new ArrayList<String>();
				JSONObject obj = productsInfo.getJSONObject(i);
				dishesInfo.setTitle(obj.getString("title"));

				dishesInfo.setDish_id(obj.getInt("id"));
				JSONArray image_jsonArray = obj.getJSONArray("images");

				for (int size2 = 0; size2 < image_jsonArray.length(); size2++) {
					JSONObject obj1 = image_jsonArray.getJSONObject(size2);
					imageSrcList.add(obj1.getString("src"));
				}
				dishesInfo.setImg_src(imageSrcList);

				String ingrediants = obj.getString("short_description");
				dishesInfo.setIngrediants(ingrediants);

				JSONArray attributes = obj.getJSONArray("attributes");
				int attributesArraySize=attributes.length();
				 
				if(attributesArraySize!=8)
					 dishesInfo.setWheatWhiteOption(false);
				else
					dishesInfo.setWheatWhiteOption(true);

				JSONObject originCountry = attributes.getJSONObject(0);
				dishesInfo.setOriginCountry(originCountry.getJSONArray(
						"options").getString(0));

				JSONObject calories = attributes.getJSONObject(1);
				dishesInfo.setCalories(calories.getJSONArray("options")
						.getString(0));

				JSONObject nonVegOrVeg = attributes.getJSONObject(2);
				dishesInfo.setNonVegOrVeg(nonVegOrVeg.getJSONArray("options")
						.getString(0));

				JSONObject level = attributes.getJSONObject(3);
				dishesInfo.setLevel(level.getJSONArray("options").getString(0));

				JSONObject prepTime = attributes.getJSONObject(4);
				dishesInfo.setPrep_time(prepTime.getJSONArray("options")
						.getString(0));

				JSONObject cookTime = attributes.getJSONObject(5);
				dishesInfo.setCooking_time(cookTime.getJSONArray("options")
						.getString(0));

				dishes_info.add(dishesInfo);
			}

			adapter = new RecyclerViewAdapter(HomeActivity.this, dishes_info);
			modernLst.setAdapter(adapter);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
