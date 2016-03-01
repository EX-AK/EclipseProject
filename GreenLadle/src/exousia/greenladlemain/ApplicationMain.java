package exousia.greenladlemain;

import android.app.Application;

import com.android.volley.manager.RequestManager;

public class ApplicationMain extends Application {


	private static ApplicationMain mAppMain;

	public void onCreate() {

		super.onCreate();
		RequestManager.getInstance().init(this);		
		mAppMain = this;
	}

	public static ApplicationMain getApplicationMainInstance() {
		return mAppMain;
	}

}
