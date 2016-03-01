package exousia.greenladlemain;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleTon {
	
	public static RequestQueue requestQueue;
	
	
	private VolleySingleTon()
	{}
	
 public static VolleySingleTon getInstance()
 {
	 VolleySingleTon volleySinglton=new VolleySingleTon();
	 return volleySinglton;
 }
 
 public RequestQueue getRequestQueue()
 {
	 RequestQueue requestQueue=Volley.newRequestQueue(ApplicationMain.getApplicationMainInstance().getApplicationContext());
	 return requestQueue;
 }
}
