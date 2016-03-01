package exousia.greenladlemain;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DisheshHelperClass extends SQLiteOpenHelper {

	private final static String TABLE_NAME = "dishes_detail";
	private final static String COLUMN_ID = "_id";
	private final static String COLUMN_DISHES_NAME = "dishes_name";
	private final static String COLUMN_DISH_COUNT = "dish_count";
	private final static String COLUMN_WHEAT_OR_WHITE = "wheat_white";
	private final static String COLUMN_MEAL_FOR_2_4 = "meal_for_2_4";

	private final static String DATABASE_NAME = "dishes";
	private final static int DATABASE_VERSION = 1;

	public DisheshHelperClass(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("database has been create now you can your data in it");
		final String query = "create table " + TABLE_NAME + " ( " + COLUMN_ID + " integer  primary key autoincrement , "
				+ COLUMN_DISHES_NAME + " text not null, " + COLUMN_DISH_COUNT + " int default 1, "
				+ COLUMN_WHEAT_OR_WHITE + " text, " + COLUMN_MEAL_FOR_2_4 + " integer not null, UNIQUE("
				+ COLUMN_DISHES_NAME + "," + COLUMN_WHEAT_OR_WHITE + "," + COLUMN_MEAL_FOR_2_4
				+ ")  ON CONFLICT REPLACE );";
		System.out.println(query);
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists " + TABLE_NAME);
		onCreate(db);
	}

	public void addDish(DishDetail dishDetail) {
		System.out.println("add is called");
		// System.out.println("selected dish helping ");
		SQLiteDatabase database = getWritableDatabase();
		ContentValues values = new ContentValues();

		String dishName = dishDetail.getDishName();
		values.put(COLUMN_DISHES_NAME, dishName);

		int dishCount = dishDetail.getDishCount();
		values.put(COLUMN_DISH_COUNT, dishCount);

		int mealFor2Or4 = dishDetail.getMealFor2Or4();
		values.put(COLUMN_MEAL_FOR_2_4, mealFor2Or4);

		String wheatOrWhite = dishDetail.getWheatOrWhite();

		values.put(COLUMN_WHEAT_OR_WHITE, wheatOrWhite);

		database.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_FAIL);
	}

	public List<DishDetail> getAllDishes() {
		SQLiteDatabase database = getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);

		List<DishDetail> dishesList = new ArrayList<DishDetail>();

		if (cursor.moveToFirst()) {

			do {
				DishDetail dishDetail = new DishDetail();
				String dishName = cursor.getString(1);
				int dishCount = cursor.getInt(2);
				dishDetail.setDishName(dishName);
				dishDetail.setDishCount(dishCount);
				dishesList.add(dishDetail);

			} while (cursor.moveToNext());
		}
		cursor.close();
		return dishesList;
	}

	public void updateDishCount(DishDetail dishDetail) {
		SQLiteDatabase database = getWritableDatabase();
		String dishName = dishDetail.getDishName();
		String dishIsWheatOrWhite = dishDetail.getWheatOrWhite();
		String dishFor2OrFor4 = "" + dishDetail.getMealFor2Or4();
		int count = dishDetail.getDishCount();
		ContentValues values = new ContentValues();
		values.put(COLUMN_DISH_COUNT, count);
		database.update(TABLE_NAME, values, COLUMN_DISHES_NAME + " LIKE ? and " + COLUMN_WHEAT_OR_WHITE + " LIKE ? and "
				+ COLUMN_MEAL_FOR_2_4 + " = ?", new String[] { dishName, dishIsWheatOrWhite, dishFor2OrFor4 });
	}

	public void updateDishCountDlete(String dishName, String dishIsWheatOrWhite, String dishFor2OrFor4) {
		SQLiteDatabase database = getWritableDatabase();
		int count = 0;
		Cursor cursor = database.query(
				TABLE_NAME, null, COLUMN_DISHES_NAME + " LIKE ? and " + COLUMN_WHEAT_OR_WHITE + " LIKE ? and "
						+ COLUMN_MEAL_FOR_2_4 + " = ?",
				new String[] { dishName, dishIsWheatOrWhite, dishFor2OrFor4 }, null, null, null);

		if (cursor.moveToFirst()) {

			count = cursor.getInt(2);
			count--;

		}
		cursor.close();
		ContentValues values = new ContentValues();
		values.put(COLUMN_DISH_COUNT, count);

		if (count > 0) {
			database.update(TABLE_NAME,
					values, COLUMN_DISHES_NAME + " LIKE ? and " + COLUMN_WHEAT_OR_WHITE + " LIKE ? and "
							+ COLUMN_MEAL_FOR_2_4 + " = ?",
					new String[] { dishName, dishIsWheatOrWhite, dishFor2OrFor4 });

		} else {
			database.delete(TABLE_NAME, COLUMN_DISHES_NAME + " LIKE ? and " + COLUMN_WHEAT_OR_WHITE + " LIKE ? and "
					+ COLUMN_MEAL_FOR_2_4 + " = ?", new String[] { dishName, dishIsWheatOrWhite, dishFor2OrFor4 });

		}
	}

	public void addOrUpdateDish(DishDetail dishDetail) {
		
		SQLiteDatabase database = getWritableDatabase();
		String dishName = dishDetail.getDishName();
		String dishIsWheatOrWhite = dishDetail.getWheatOrWhite();
		String dishFor2OrFor4 = "" + dishDetail.getMealFor2Or4();
		Cursor cursor = database.query(
				TABLE_NAME, null, COLUMN_DISHES_NAME + " LIKE ? and " + COLUMN_WHEAT_OR_WHITE + " LIKE ? and "
						+ COLUMN_MEAL_FOR_2_4 + " = ?",
				new String[] { dishName, dishIsWheatOrWhite, dishFor2OrFor4 }, null, null, null);

		if (cursor.moveToFirst()) {

			updateDishCount(dishDetail);

		} else {

			addDish(dishDetail);

		}
		cursor.close();
	}

	public int getDishCount(String dishName) {

		SQLiteDatabase database = getReadableDatabase();
		

		int count = 0;
		Cursor cursor = database.query(TABLE_NAME, new String[] { COLUMN_DISH_COUNT }, COLUMN_DISHES_NAME + " Like ?",
				new String[] { dishName }, null, null, null);

		if (cursor.moveToFirst()) {
			
			do{
			count = count + cursor.getInt(0);
			}while(cursor.moveToNext());
			cursor.close();
			return count;
		} else {
			return 0;
		}
	}

	public int getTotalCount() {

		SQLiteDatabase database = getReadableDatabase();
		int count = 0;
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);

		if (cursor.moveToFirst()) {

			do {
				count = count + cursor.getInt(2);
			} while (cursor.moveToNext());
			return count;
		} else {
			return 0;
		}
	}

	public void deleteTableDishes() {
		SQLiteDatabase database = getWritableDatabase();
		database.execSQL("delete from " + TABLE_NAME);

	}
}
