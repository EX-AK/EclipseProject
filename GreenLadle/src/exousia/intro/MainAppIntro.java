package exousia.intro;

import com.github.paolorotolo.appintro.AppIntro2;

import android.content.Intent;
import android.os.Bundle;
import exousia.greenladleotp.OtpRegisteration;

public class MainAppIntro extends AppIntro2 {



	@Override
	public void init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		addSlide(new Intro1());
		addSlide(new Intro2());
		addSlide(new Intro3());
		//showDoneButton(false);
		
	}



	@Override
	public void onDonePressed() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(this,OtpRegisteration.class);
		startActivity(intent);
		finish();
	}

}
