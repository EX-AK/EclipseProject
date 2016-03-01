package exousia.greenladlemain;

import java.io.Serializable;
import java.util.ArrayList;

public class DishesInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private int dish_id;
	private ArrayList<String> img_src;
	private String cooking_time;
	private String prep_time;
	private String originCountry;
	private String calories;
	private String nonVegOrVeg;
	private String level;
	private String ingrediants;
	private boolean wheatWhiteOption;
	
	
 	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getDish_id() {
		return dish_id;
	}
	public void setDish_id(int dish_id) {
		this.dish_id = dish_id;
	}

	public String getCooking_time() {
		return cooking_time;
	}
	public void setCooking_time(String cooking_time) {
		this.cooking_time = cooking_time;
	}
	public String getPrep_time() {
		return prep_time;
	}
	public void setPrep_time(String prep_time) {
		this.prep_time = prep_time;
	}
	public String getOriginCountry() {
		return originCountry;
	}
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	public String getCalories() {
		return calories;
	}
	public void setCalories(String calories) {
		this.calories = calories;
	}
	public String getNonVegOrVeg() {
		return nonVegOrVeg;
	}
	public void setNonVegOrVeg(String nonVegOrVeg) {
		this.nonVegOrVeg = nonVegOrVeg;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getIngrediants() {
		return ingrediants;
	}
	public void setIngrediants(String ingrediants) {
		this.ingrediants = ingrediants;
	}
	public ArrayList<String> getImg_src() {
		return img_src;
	}
	public void setImg_src(ArrayList<String> img_src) {
		this.img_src = img_src;
	}
	public boolean isWheatWhiteOption() {
		return wheatWhiteOption;
	}
	public void setWheatWhiteOption(boolean wheatWhiteOption) {
		this.wheatWhiteOption = wheatWhiteOption;
	}
	
}
