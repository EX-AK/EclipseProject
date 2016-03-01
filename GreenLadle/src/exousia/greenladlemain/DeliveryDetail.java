package exousia.greenladlemain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import exousia.greenladle.GreenLadle;
import exousia.greenladle.HomeActivity;
import exousia.greenladle.R;
import exousia.greenladlemain.DatePickerClass.DatePickerListener;
import exousia.greenladlemain.UniversalCalendarAndTimeClass.DateAndTimePicker;

public class DeliveryDetail extends AppCompatActivity implements
		DatePickerListener, DateAndTimePicker, OnClickListener {

	ListView delivery_detail;
	View globalSpinnerView;

	Typeface typeFace;

	java.text.DateFormat dateFormat;

	ApplicationMain mApp;
	ArrayList<String> selected_dishes = new ArrayList<String>();
	ArrayList<String> selected_dishes_wheatOrWhite = new ArrayList<String>();
	ArrayList<Integer> selected_dishes_MealFor24 = new ArrayList<Integer>();
	HashMap<Integer, String> dateRec = new HashMap<Integer, String>();
	HashMap<Integer, String> timeRec = new HashMap<Integer, String>();
	TextView date;
	Spinner time;
	TextView currDate;
	ToggleButton toggleButton;
	TextView dish_name, total;
	int totalBill = 0;
	Button checkContinue;
	int year;
	int month;
	int dayOfMonth;
	BaseAdapter adapter;
	Calendar c;
	StringBuilder date1;
	String dateofdelivery, dishname, deliveryTime;
	int currYear, currMonth, currDay;
	String timings[] = { "Select Time", "10:00 AM-1:00 PM",
			"01:00 PM-04:00 PM", "04:00 PM-7:00 PM", "10:00 PM-10:00 PM" };
	ArrayList<View> allViews = new ArrayList<View>();
	UniversalCalendarAndTimeClass universalDatePicker;
	DisheshHelperClass database;
	JSONObject mainJsonObject;
	JSONArray dishesJsonArray;
	String uniqueTimeStamp;
	int datePosition = 0;
	// JSONObject obj;
	JSONObject delivery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.deliver_details);
		mainJsonObject = new JSONObject();
		typeFace = Typeface.createFromAsset(getAssets(), "fonts/ubuntu.ttf");

		dateFormat = java.text.DateFormat.getDateInstance(
				java.text.DateFormat.LONG, Locale.getDefault());

		delivery_detail = (ListView) findViewById(R.id.dishes_delivery_details);
		checkContinue = (Button) findViewById(R.id.checkOutContinue);

		database = new DisheshHelperClass(DeliveryDetail.this);

		total = (TextView) findViewById(R.id.Total);
		total.setTypeface(typeFace);

		c = Calendar.getInstance();
		currYear = c.get(Calendar.YEAR) + 1;
		currMonth = c.get(Calendar.MONTH) + 1;
		currDay = c.get(Calendar.DAY_OF_MONTH);
		checkContinue.setOnClickListener(this);
		viewAllSelectedDish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	public void viewAllSelectedDish() {
		adapter = new BaseAdapterForDeliveryDetail();
		delivery_detail.setAdapter(adapter);
		getAllSelectedDishes();
		total.setText("Total: Rs " + totalBill + "/-");
	}

	// method to get all the dishes added to the basket
	public void getAllSelectedDishes() {
		SQLiteDatabase db = new DisheshHelperClass(this).getReadableDatabase();
		Cursor cursor = db.query("dishes_detail", new String[] { "dishes_name",
				"dish_count", "wheat_white", "meal_for_2_4" }, null, null,
				null, null, null);

		if (cursor.moveToFirst()) {

			do {
				int count = cursor.getInt(1);
				String dish_name = cursor.getString(0);
				int meal24 = cursor.getInt(3);
				String whiteOrWheat = cursor.getString(2);
				for (int i = 0; i < count; i++) {
					selected_dishes.add(dish_name);
					selected_dishes_MealFor24.add(meal24);
					selected_dishes_wheatOrWhite.add(whiteOrWheat);
				}
				if (meal24 == 2)
					totalBill = totalBill + (600 * count);
				else
					totalBill = totalBill + (600 * 2 * count);

			} while (cursor.moveToNext());

		}

	}

	@Override
	public void datePicker(Calendar c) {
		// TODO Auto-generated method stub
		int checkYear = c.get(Calendar.YEAR) + 1;
		int checkMonth = c.get(Calendar.MONTH) + 1;
		int checkDay = c.get(Calendar.DAY_OF_MONTH);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);

		if (month < 10) {

			month = Integer.parseInt("0" + month);
		}
		if (day < 10) {

			day = Integer.parseInt("0" + day);
		}
		if ((day > checkDay && month > checkMonth) || year >= checkYear
				|| month > checkMonth)
			date.setBackgroundColor(Color.BLUE);
		else
			date.setBackgroundColor(Color.WHITE);

		date.setText(day + "-" + month + "-" + year);
		dateRec.put(datePosition, day + "-" + month + "-" + year);

	}

	private class BaseAdapterForDeliveryDetail extends BaseAdapter {

		ImageView calendar, deleteItem;
		Spinner time;
		TextView rate;
		TextView dish_name;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return selected_dishes.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = getLayoutInflater().inflate(
					R.layout.list_style_for_delivery_detail, parent, false);
			allViews.add(convertView);
			dish_name = (TextView) convertView.findViewById(R.id.dish_name);
			time = (Spinner) convertView.findViewById(R.id.delivery_time);
			time.setAdapter(new ArrayAdapter<String>(DeliveryDetail.this,
					android.R.layout.simple_spinner_dropdown_item, timings));
			dish_name.setText(selected_dishes.get(position) + " (Meal For "
					+ selected_dishes_MealFor24.get(position) + ", "
					+ selected_dishes_wheatOrWhite.get(position) + ")");
			dish_name.setTypeface(typeFace);

			deleteItem = (ImageView) convertView.findViewById(R.id.delete_item);
			deleteItem.setTag(position);
			deleteItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					createDialog(Integer.parseInt("" + v.getTag()));
				}
			});

			rate = (TextView) convertView.findViewById(R.id.rate);
			if (selected_dishes_MealFor24.get(position) == 2) {
				rate.setText("600.0");
			} else {
				rate.setText("1200.0");
			}

			calendar = (ImageView) convertView.findViewById(R.id.calendar);
			calendar.setTag(position);
			time.setTag(position);
			calendar.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int wantedPosition = (Integer) v.getTag();
					int firstPosition = delivery_detail
							.getFirstVisiblePosition() - 0;
					int wantedChild = wantedPosition - firstPosition;
					universalDatePicker = new UniversalCalendarAndTimeClass(
							DeliveryDetail.this);
					universalDatePicker.chooseDate();
					View view = delivery_detail.getChildAt(wantedChild);
					date = (TextView) view.findViewById(R.id.date);
					datePosition = position;
				}
			});

			time.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if (position > 0)
						timeRec.put(
								Integer.parseInt(""
										+ ((Spinner) ((LinearLayout) parent
												.getParent())
												.findViewById(R.id.delivery_time))
												.getTag()),
								""
										+ ((Spinner) ((LinearLayout) parent
												.getParent())
												.findViewById(R.id.delivery_time))
												.getSelectedItemPosition());
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});

			setDateTimeAgain((TextView) convertView.findViewById(R.id.date),
					time, position);
			return convertView;
		}

	}

	private void setDateTimeAgain(TextView date, Spinner time, int position) {

		if (dateRec.get(position) != null) {
			date.setText(dateRec.get(position));
		}
		if (timeRec.get(position) != null)
			time.setSelection(Integer.parseInt(timeRec.get(position)));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.check, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		if (item.getItemId() == android.R.id.home
				|| item.getItemId() == R.id.add_action) {
			Intent intent = new Intent(this, HomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void createDialog(final int position) {

		final Dialog deleteOrCancleItemDialog = new Dialog(DeliveryDetail.this);
		deleteOrCancleItemDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		deleteOrCancleItemDialog.setCancelable(false);
		deleteOrCancleItemDialog
				.setContentView(R.layout.dialog_layout_to_delete);

		deleteOrCancleItemDialog.findViewById(R.id.no).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						deleteOrCancleItemDialog.dismiss();
					}
				});

		deleteOrCancleItemDialog.findViewById(R.id.yes).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String dishName = selected_dishes.get(position);
						String wheatOrWhite = selected_dishes_wheatOrWhite
								.get(position);
						String for2OrFor4 = ""
								+ selected_dishes_MealFor24.get(position);
						database.updateDishCountDlete(dishName, wheatOrWhite,
								for2OrFor4);
						reloadListView();
						allViews.remove(position);
						deleteOrCancleItemDialog.dismiss();
					}
				});
		deleteOrCancleItemDialog.show();

	}

	public void reloadListView() {
		selected_dishes.clear();
		selected_dishes_MealFor24.clear();
		selected_dishes_wheatOrWhite.clear();
		dateRec.clear();
		timeRec.clear();
		totalBill = 0;
		adapter = new BaseAdapterForDeliveryDetail();
		delivery_detail.setAdapter(adapter);
		getAllSelectedDishes();
		total.setText("Total: Rs " + totalBill + "/-");
	}

	@Override
	public void dateSelected(Calendar calendar) {
		// TODO Auto-generated method stub

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		// date.setText(dateFormat.format(calendar.getTime()));
		date.setText(day + "-" + month + "-" + year);
		dateRec.put(datePosition, day + "-" + month + "-" + year);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		dishesJsonArray = new JSONArray();
		date1 = new StringBuilder();
		Date tareek = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd h:mm:ss a");
		uniqueTimeStamp = sdf.format(tareek);

		try {
			mainJsonObject.put("orderId", uniqueTimeStamp);
			mainJsonObject.put("dishes", dishesJsonArray);
			mainJsonObject.put("total_price", totalBill + "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < allViews.size(); i++) {

			View v1 = allViews.get(i);
			dateofdelivery = (String) ((TextView) v1.findViewById(R.id.date))
					.getText();
			dishname = (String) ((TextView) v1.findViewById(R.id.dish_name))
					.getText();
			deliveryTime = (timings[((Spinner) v1
					.findViewById(R.id.delivery_time))
					.getSelectedItemPosition()]);
			makejson(i, dateofdelivery, dishname, deliveryTime);
		}

		if (allViews.size() > 0) {
			Intent intent = new Intent(DeliveryDetail.this, GreenLadle.class);
			intent.putExtra("deliverDetail", mainJsonObject.toString());
			startActivity(intent);
		}
		else
		{
			Toast.makeText(this,"Your Basket is Empty, Please Select Some Dishes To Proceed",Toast.LENGTH_SHORT).show();
		}
	}

	private void makejson(int i, String date, String dishname,
			String deliverytime) {
		// TODO Auto-generated method stub

		delivery = new JSONObject();

		try {
			dishesJsonArray.put(i, delivery);
			delivery.put("date", date);
			delivery.put("dishName", dishname);
			delivery.put("deliveryTime", deliverytime);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
