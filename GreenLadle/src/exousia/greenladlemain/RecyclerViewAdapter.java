package exousia.greenladlemain;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import exousia.greenladle.HomeActivity;
import exousia.greenladle.R;

public class RecyclerViewAdapter extends
		RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

	ArrayList<Double> allLat = new ArrayList<Double>();
	ArrayList<Double> allLng = new ArrayList<Double>();
	double[] lats;
	int positionOfItemClicked;
	List<DishesInfo> dishesDetailsList = Collections.emptyList();
	LayoutInflater inflater;

	DishesInfo dishesInfo;
	Typeface customFont;

	DisheshHelperClass database;
	Typeface typeFace;
	int count = 0;
	HomeActivity homeActivity;

	public RecyclerViewAdapter() {
	}

	public RecyclerViewAdapter(HomeActivity homeActivity,
			ArrayList<DishesInfo> dishesDetailsList) {
		// TODO Auto-generated constructor stub
		customFont = Typeface.createFromAsset(homeActivity.getAssets(),
				"fonts/greenladle.ttf");
		database = new DisheshHelperClass(homeActivity);
		inflater = LayoutInflater.from(homeActivity);
		this.dishesDetailsList = dishesDetailsList;
		this.homeActivity = homeActivity;

	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub

		return dishesDetailsList.size();
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		// TODO Auto-generated method stub

		DishesInfo dishesInfo = dishesDetailsList.get(position);
		count = database.getDishCount(dishesInfo.getTitle().toString());
		holder.dishesTitle.setText(dishesInfo.getTitle());
		holder.dishesTitle.setTypeface(typeFace);

		Picasso.with(homeActivity.getApplicationContext())
				.load(dishesInfo.getImg_src().get(0))
				.transform(new CropSquareTransformation())
				.placeholder(R.drawable.back_min_min).into(holder.dishImg);

		holder.dishCookTime.setText("Cooking Time: "
				+ dishesInfo.getCooking_time());
		holder.dishCookTime.setTypeface(typeFace);

		holder.dishPrepTime.setText("Prep Time: " + dishesInfo.getPrep_time());
		holder.dishPrepTime.setTypeface(typeFace);

		String level = dishesInfo.getLevel();

		if (level.equals("Beginner")) {
			holder.levelIndicator.setText("Beginner");
		}
		if (level.equals("Moderate")) {
			holder.levelIndicator.setText("Moderate");

		}
		if (level.equals("Advance")) {
			holder.levelIndicator.setText("Advance");
		}

		String vegOrNonVeg = dishesInfo.getNonVegOrVeg();
		if (vegOrNonVeg.equals("Veg")) {
			holder.veg.setVisibility(View.VISIBLE);
			holder.nonVeg.setVisibility(View.GONE);
		}
		if (vegOrNonVeg.equals("Non Veg")) {
			holder.nonVeg.setVisibility(View.VISIBLE);
			holder.veg.setVisibility(View.GONE);
		}

		if (count > 0) {

			holder.basketDescBack.setVisibility(View.VISIBLE);
			holder.basketDesc.setVisibility(View.VISIBLE);
			holder.basketDesc.setText(count + " Meal Added To The Basket");

		}
		if (count == 0) {
			holder.basketDescBack.setVisibility(View.GONE);
			holder.basketDesc.setVisibility(View.GONE);
		}

	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
		// TODO Auto-generated method stub

		View v = inflater
				.inflate(R.layout.home_dishes_layout, viewGroup, false);

		MyViewHolder holder = new MyViewHolder(v);
		return holder;
	}

	public void filterActivated(ArrayList<DishesInfo> filtereDishesInfo) {
		dishesDetailsList = filtereDishesInfo;
		notifyDataSetChanged();
	}

	class MyViewHolder extends RecyclerView.ViewHolder implements
			OnClickListener {

		TextView dishesTitle, levelIndicator;
		ImageView dishImg;

		TextView dishCookTime;

		TextView dishPrepTime;
		ImageView veg;
		ImageView nonVeg;
		ImageView basketDescBack;
		TextView basketDesc;

		public MyViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			dishesTitle = (TextView) itemView.findViewById(R.id.dish_name);
			dishImg = (ImageView) itemView.findViewById(R.id.dish_img);
			dishImg.setOnClickListener(this);

			dishCookTime = (TextView) itemView.findViewById(R.id.cooking_time);
			dishCookTime.setTypeface(customFont);
			dishPrepTime = (TextView) itemView.findViewById(R.id.prep_time);
			dishPrepTime.setTypeface(customFont);

			veg = (ImageView) itemView.findViewById(R.id.category);
			nonVeg = (ImageView) itemView.findViewById(R.id.imageView1);
			basketDescBack = (ImageView) itemView
					.findViewById(R.id.basket_desc_back);
			basketDesc = (TextView) itemView.findViewById(R.id.basket_desc);
			levelIndicator = (TextView) itemView
					.findViewById(R.id.levelIndicator);
			levelIndicator.setTypeface(customFont);

		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(homeActivity, DishDetailedInfo.class);
			intent.putExtra("DishInfo",dishesDetailsList.get(getAdapterPosition()));
			intent.putExtra("position", getAdapterPosition());
			homeActivity.startActivity(intent);
		}

	}

	public class CropSquareTransformation implements Transformation {
		@Override
		public Bitmap transform(Bitmap source) {

			Bitmap result = compressBitmap(source);
			if (result != source) {
				source.recycle();
			}
			return result;
		}

		@Override
		public String key() {
			return "square()";
		}
	}

	public Bitmap compressBitmap(Bitmap originalBitmap) {
		Bitmap scaledBitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		originalBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0,
				byteArray.length, options);
		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;
		float maxHeight = 500.0f;
		float maxWidth = 280.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;
		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;

			}
		}
		int scale = 1;
		while (options.outWidth / scale / 2 >= actualWidth
				&& options.outHeight / scale / 2 >= actualHeight) {
			scale *= 2;
		}
		options.inSampleSize = scale;
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16 * 1024];

		try {
			bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,
					options);			
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();

		}
		try {
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
					Bitmap.Config.ARGB_8888);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();
		}

		float ratioX = actualWidth / (float) options.outWidth;
		float ratioY = actualHeight / (float) options.outHeight;
		float middleX = actualWidth / 2.0f;
		float middleY = actualHeight / 2.0f;

		Matrix scaleMatrix = new Matrix();
		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

		Canvas canvas = new Canvas(scaledBitmap);
		canvas.setMatrix(scaleMatrix);
		canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
				middleY - bmp.getHeight() / 2, new Paint(
						Paint.FILTER_BITMAP_FLAG));

		return scaledBitmap;

	}


}
