package exousia.greenladlemain;

public class DishDetail {

	private String dishName;
	private int dishCount=1;
	private int  _id;
	private String wheatOrWhite;
	private int mealFor2Or4;
	
	public void setDishName(String dishName)
	{
		this.dishName=dishName;
	}
	
	public String getDishName()
	{
		return dishName;
	}
	
	public void setDishCount(int dishCount)
	{
		this.dishCount=dishCount;
	}
	
	public int getDishCount()
	{
		return dishCount;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getWheatOrWhite() {
		return wheatOrWhite;
	}

	public void setWheatOrWhite(String wheatOrWhite) {
		this.wheatOrWhite = wheatOrWhite;
	}

	public int getMealFor2Or4() {
		return mealFor2Or4;
	}

	public void setMealFor2Or4(int mealFor2Or4) {
		this.mealFor2Or4 = mealFor2Or4;
	}
	
}
