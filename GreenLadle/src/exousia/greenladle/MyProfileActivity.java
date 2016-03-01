package exousia.greenladle;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

public class MyProfileActivity extends AppCompatActivity implements
		OnClickListener {

	RelativeLayout editBtn;
	ImageView editBtnImg;
	ImageView profilePic;

	SharedPreferences recordsFile;
	Editor editor;

	String name = null;
	String email = null;
	String picUrl = null;

	Typeface typeFace;

	EditText nameEditText, emailEditText, phoneEditText;

	boolean EDIT_PERMISSION = true;

	Toolbar toolbar;
	DrawerLayout mdrawerlayout;
	LinearLayout mainContent;
	int frag;
	NavigationDrawer navDrawer;
	String phoneNumber;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.my_profile);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("My Profile");

		recordsFile = getSharedPreferences("My_records", 0);
		editor = recordsFile.edit();
		getUserDetailsFromSharedPreference();

		typeFace = Typeface.createFromAsset(getAssets(), "fonts/ubuntu.ttf");

		editBtn = (RelativeLayout) findViewById(R.id.name_edit_btn);
		editBtn.setOnClickListener(this);

		editBtnImg = (ImageView) findViewById(R.id.edit_btn_img);

		nameEditText = (EditText) findViewById(R.id.name_edit_text);
		nameEditText.setTypeface(typeFace);
		nameEditText.setText(name);

		phoneEditText = (EditText) findViewById(R.id.phone_edit_text);
		phoneEditText.setTypeface(typeFace);
		phoneEditText.setText(phoneNumber);

		emailEditText = (EditText) findViewById(R.id.email_edit_text);
		emailEditText.setTypeface(typeFace);
		emailEditText.setText(email);

		profilePic = (ImageView) findViewById(R.id.profile_pic);
		setProfilePic(picUrl);

		mdrawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		mainContent = (LinearLayout) findViewById(R.id.mainview);
		frag = R.id.left_drawer;

		navDrawer = ((NavigationDrawer) getSupportFragmentManager()
				.findFragmentById(R.id.left_drawer));
		navDrawer.actionbarToogler(this, mainContent, mdrawerlayout, toolbar,
				frag);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.name_edit_btn:
			changeEditBtnAppereance(EDIT_PERMISSION, v);
			break;

		default:
			break;
		}
	}

	public void changeEditBtnAppereance(boolean EDIT_PERMISSION, View v) {

		if (EDIT_PERMISSION) {

			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			nameEditText.setFocusableInTouchMode(true);
			nameEditText.setText("");
			editBtnImg.setImageResource(R.drawable.tick_);
			this.EDIT_PERMISSION = false;
		} else {

			if (nameEditText.length() == 0) {

				nameEditText.setError("Please Fill Your Name");
			} else {

				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(v.getWindowToken(), 0);
				nameEditText.setFocusable(false);
				editBtnImg.setImageResource(R.drawable.edit);
				this.EDIT_PERMISSION = true;
				navDrawer.changeUserName(nameEditText.getText() + "");
				editor.putString("username", nameEditText.getText() + "");
				editor.commit();
			}
		}
	}

	private void getUserDetailsFromSharedPreference() {
		name = recordsFile.getString("username", "Unknown");
		email = recordsFile.getString("email", "Unknown");
		phoneNumber = recordsFile.getString("phoneNumber", "Unknown");
		picUrl = recordsFile.getString("profile_pic_uri", "");
	}

	public void setProfilePic(String picUrl) {
		Picasso.with(getApplicationContext())
		.load(picUrl)		
		.placeholder(R.drawable.profile_green).into(profilePic);

	}

	public interface ChangeName {

		public void changeUserName(String name);
	}

}
