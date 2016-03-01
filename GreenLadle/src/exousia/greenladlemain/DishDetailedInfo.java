package exousia.greenladlemain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.efoad.views.ViewPagerIndicator;
import com.squareup.picasso.Picasso;

import exousia.greenladle.HomeActivity;
import exousia.greenladle.R;

public class DishDetailedInfo extends AppCompatActivity implements
		OnClickListener {

	int numberOfMealsSelected = 1;

	TextView cookTime, title, prepTime, description,descriptionTitle, calories, origin,
			quantity, numberOfMeals, addToBasketTitle, ingrediantsTitle;
	TextView itemCountTextView = null;
	int totalItemcount = 0;
	Typeface typeFace;
	Intent intent = null;
	ImageView plusBtn, minusBtn;
	ViewPager dishImageGallery;
	ImageAdapter adapter;
	SharedPreferences recordsFile;
	Editor editor;
	DishesInfo dishInfo;
	ViewPagerIndicator pageIndicator;
	DishDetail dishDetail;
	DisheshHelperClass addItemToDatabase;
	FrameLayout addToBasket;
	RelativeLayout actionMenuItemCount;
	ToggleButton mealFor24, wheatWhite;
	HomeActivity homeActivity;
	int itemPosition;
	Context mContext;
	RecyclerViewAdapter recyclerViewAdapter;
	XmlPullParserFactory mXmlPullParserFactor;
	XmlPullParser mXmlPullParser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.dishe_complete_detail);
		recyclerViewAdapter = new RecyclerViewAdapter();
		recordsFile = getSharedPreferences("My_records", 0);
		editor = recordsFile.edit();
		typeFace = Typeface.createFromAsset(getAssets(), "fonts/ubuntu.ttf");
		addItemToDatabase = new DisheshHelperClass(DishDetailedInfo.this);
		mContext = this;
		intent = getIntent();
		dishInfo = (DishesInfo) intent.getSerializableExtra("DishInfo");
		itemPosition = intent.getIntExtra("position", -1);
		adapter = new ImageAdapter();
		dishImageGallery = (ViewPager) findViewById(R.id.dishes_gallery);
		dishImageGallery.setAdapter(adapter);
		pageIndicator = (ViewPagerIndicator) findViewById(R.id.page_indicator);
		pageIndicator.setViewPager(dishImageGallery);
		mealFor24 = (ToggleButton) findViewById(R.id.meal_for_2_or_4);

		wheatWhite = (ToggleButton) findViewById(R.id.wheat_or_white);
		if (!dishInfo.isWheatWhiteOption())
			wheatWhite.setVisibility(View.GONE);

		cookTime = (TextView) findViewById(R.id.cook_time);
		cookTime.setText("Cooking Time: " + dishInfo.getCooking_time());
		cookTime.setTypeface(typeFace);
		title = (TextView) findViewById(R.id.dish_title);
		title.setText(dishInfo.getTitle());
		title.setTypeface(typeFace);
		prepTime = (TextView) findViewById(R.id.prep_time);
		prepTime.setText("Preperation Time: " + dishInfo.getPrep_time());
		prepTime.setTypeface(typeFace);
		description = (TextView) findViewById(R.id.ingridents_description);
		descriptionTitle=(TextView) findViewById(R.id.ingridents_description_title);
		
		parseDescriptionString(dishInfo.getIngrediants());		
		calories = (TextView) findViewById(R.id.calories);
		calories.setText("Calories: " + dishInfo.getCalories() + "kCal");
		calories.setTypeface(typeFace);

		origin = (TextView) findViewById(R.id.origin);
		origin.setText("Origin: " + dishInfo.getOriginCountry());
		origin.setTypeface(typeFace);

		addToBasket = (FrameLayout) findViewById(R.id.addToBasket);
		addToBasket.setOnClickListener(this);

		plusBtn = (ImageView) findViewById(R.id.plusbtn);
		plusBtn.setOnClickListener(this);

		minusBtn = (ImageView) findViewById(R.id.minusbtn);
		minusBtn.setOnClickListener(this);

		quantity = (TextView) findViewById(R.id.quantity);
		quantity.setTypeface(typeFace);

		numberOfMeals = (TextView) findViewById(R.id.number_of_meals);
		numberOfMeals.setTypeface(typeFace);

		addToBasketTitle = (TextView) findViewById(R.id.addTobasket);
		addToBasketTitle.setTypeface(typeFace);

		ingrediantsTitle = (TextView) findViewById(R.id.ingredients_title);
		ingrediantsTitle.setTypeface(typeFace);

	}

	private void parseDescriptionString(String dishDescription) {
		// TODO Auto-generated method stub
		
		org.jsoup.nodes.Document doc = Jsoup.parse(dishDescription);
		Elements elements = doc.body().select("*");		
		description.setText(elements.get(4).ownText());
		descriptionTitle.setText(elements.get(3).ownText());
		
		
	}

	class ImageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dishInfo.getImg_src().size();
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			ImageView imageView = new ImageView(DishDetailedInfo.this);
			Log.e("hello", dishInfo.getImg_src().get(position));
			imageView.setScaleType(ScaleType.CENTER_CROP);
			Picasso.with(getApplicationContext())
					.load(dishInfo.getImg_src().get(position))
					.transform(
							recyclerViewAdapter.new CropSquareTransformation())
					.placeholder(R.drawable.back_min_min).into(imageView);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView((ImageView) object);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.addToBasket:
			addItemToBasket();
			editor.putInt("AddedItemAdapterPosition", itemPosition);
			editor.commit();
			createDialog();
			break;
		case R.id.plusbtn:
			numberOfMealsSelected++;
			quantity.setText("" + numberOfMealsSelected);
			break;
		case R.id.minusbtn:
			if (numberOfMealsSelected > 0)
				numberOfMealsSelected--;
			quantity.setText("" + numberOfMealsSelected);
			break;
		case R.id.menutiem_count_container:
			Intent intent = new Intent(this, DeliveryDetail.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}

	public void addItemToBasket() {
		dishDetail = new DishDetail();
		int meal24 = 0;
		String wheWhit = null;
		if (!mealFor24.isChecked()) {
			meal24 = 2;

		} else {
			meal24 = 4;

		}
		if (wheatWhite.getVisibility() == View.VISIBLE) {
			if (!wheatWhite.isChecked()) {

				wheWhit = "Wheat";
			} else {

				wheWhit = "White";
			}
		} else {
			wheWhit = "";
		}

		dishDetail.setDishName(dishInfo.getTitle());
		dishDetail.setMealFor2Or4(meal24);
		dishDetail.setWheatOrWhite(wheWhit);
		dishDetail
				.setDishCount(Integer.parseInt(quantity.getText().toString()));
		addItemToDatabase.addOrUpdateDish(dishDetail);
	}

	public void createDialog() {

		final Dialog addMoreOrCheckoutDialog = new Dialog(DishDetailedInfo.this);
		addMoreOrCheckoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		addMoreOrCheckoutDialog.setCancelable(false);
		addMoreOrCheckoutDialog
				.setContentView(R.layout.dialog_layout_for_user_preference);
		addMoreOrCheckoutDialog.findViewById(R.id.imageView1)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						Intent intent = new Intent(DishDetailedInfo.this,
								HomeActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						finish();
						addMoreOrCheckoutDialog.dismiss();
					}
				});

		addMoreOrCheckoutDialog.findViewById(R.id.imageView2)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(DishDetailedInfo.this,
								DeliveryDetail.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						finish();
						addMoreOrCheckoutDialog.dismiss();
					}
				});
		addMoreOrCheckoutDialog.show();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		totalItemcount = addItemToDatabase.getTotalCount();
		if (itemCountTextView != null)
			itemCountTextView.setText("" + totalItemcount);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.pick_your_plan, menu);

		View v = menu.findItem(R.id.basket_action2).getActionView();
		actionMenuItemCount = (RelativeLayout) v
				.findViewById(R.id.menutiem_count_container);
		actionMenuItemCount.setOnClickListener(this);
		itemCountTextView = (TextView) v.findViewById(R.id.item_count);
		itemCountTextView.setText("" + totalItemcount);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(DishDetailedInfo.this,
					HomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public interface DishItemAddNotification {
		void dishItemAdded(int position);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("destroy", "destroy");

	}
}
