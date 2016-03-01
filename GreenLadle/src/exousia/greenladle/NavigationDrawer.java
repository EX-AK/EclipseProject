package exousia.greenladle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import exousia.greenladle.MyProfileActivity.ChangeName;

public class NavigationDrawer extends Fragment implements ChangeName {

	DrawerLayout mdrawerLayout;
	Toolbar toolbar;
	LinearLayout mainContent;
	ActionBarDrawerToggle mdrawerToggle;

	AppCompatActivity activity;
	ListView mleftDrawerItem;
	String mtitle[];// array for storing title for left drawer list
	TypedArray mimage;// array for storing image path for left drawer list
	View left_drawer_frag;

	ImageView profilePic;
	TextView userName;
	Typeface typeFace;

	Intent intent;
	SharedPreferences recordsFile;

	public NavigationDrawer() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.left_drawer_list, container, false);

		recordsFile = getActivity().getSharedPreferences("My_records", 0);

		profilePic = (ImageView) v.findViewById(R.id.profile_pic);

		userName = (TextView) v.findViewById(R.id.user_name);
		setProfilePicAndName(recordsFile.getString("profile_pic_uri", ""),
				recordsFile.getString("username", "Unknown"),getActivity());

		typeFace = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/ubuntu.ttf");
		userName.setTypeface(typeFace);

		mleftDrawerItem = (ListView) v.findViewById(R.id.nav_left_drawer_list);
		mtitle = getResources().getStringArray(R.array.mdrawerListArray);
		mimage = getResources().obtainTypedArray(R.array.mdrawerListArrayimg);
		mleftDrawerItem.setAdapter(new LeftDrawerListAdapter());
		

		mleftDrawerItem.setOnItemClickListener(new OnItemClickListener() {
			// @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mdrawerLayout.closeDrawer(left_drawer_frag);
				diplayScreen(position);
			}
		});
		return v;
	}

	// wthid that sync between toolbar, activity and leftdrawer
	public void actionbarToogler(final AppCompatActivity activity,
			LinearLayout mainview, DrawerLayout mdrawer, Toolbar toolbar,
			int left_drawer_id) {
		this.left_drawer_frag = activity.findViewById(left_drawer_id);
		this.mdrawerLayout = mdrawer;
		this.mainContent = mainview;
		this.toolbar = toolbar;
		this.activity = activity;
		mdrawerToggle = new ActionBarDrawerToggle(activity, mdrawer, toolbar,
				R.string.open_drawer, R.string.close_drawer) {

			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub

				super.onDrawerOpened(drawerView);
				activity.invalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(drawerView);
				activity.invalidateOptionsMenu();
			}

			@SuppressLint("NewApi")
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// TODO Auto-generated method stub
				super.onDrawerSlide(drawerView, slideOffset);
				mainContent
						.setTranslationX(drawerView.getWidth() * slideOffset);
			}
		};

		mdrawer.setDrawerListener(mdrawerToggle);
		mdrawer.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mdrawerToggle.syncState();
			}
		});

	}

	// we attach replace and attach the fragment
	// as user select item i.e. home, my profile etc from left drawer list view
	private void diplayScreen(int listItem) {

		switch (listItem) {
		case 0:
			if (!(activity instanceof HomeActivity)) {
				intent = new Intent(activity, HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			break;
		case 1:
			if (!(activity instanceof MyProfileActivity)) {
				intent = new Intent(activity, MyProfileActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				
			}
			break;
		case 2:
			if (!(activity instanceof MyOrderHistoryActivity)) {
				intent = new Intent(activity, MyOrderHistoryActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				
			}
			break;
		case 3:
			if (!(activity instanceof CustomerSupportActivity)) {
				intent = new Intent(activity, CustomerSupportActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				
			}
			break;
		case 4:
			if (!(activity instanceof ReferNEarnActivity)) {
				intent = new Intent(activity, ReferNEarnActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			break;
		case 5:
			if (!(activity instanceof AboutUsActivity)) {
				intent = new Intent(activity, AboutUsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			break;
		}
		if (!(activity instanceof HomeActivity)) {
         activity.finish();
		}
	}

	// making custom adapter for left drawer list view
	public class LeftDrawerListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mtitle.length;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View leftListViewStyle = getActivity().getLayoutInflater().inflate(
					R.layout.left_drawer_list_style, null);
			ImageView image = (ImageView) leftListViewStyle
					.findViewById(R.id.image_view_left_drawer_list);
			TextView textView = (TextView) leftListViewStyle
					.findViewById(R.id.text_view_left_drawer_list);
			textView.setTypeface(typeFace);
			image.setImageResource(mimage.getResourceId(position, -1));
			textView.setText(mtitle[position]);			
			return leftListViewStyle;
		}
	}

	public void setProfilePicAndName(String picUrl, String name,Context mContext) {
		Picasso.with(mContext)
		.load(picUrl)		
		.placeholder(R.drawable.profile_new).into(profilePic);
		userName.setText("Welcome " + name);
	}

	@Override
	public void changeUserName(String name) {
		// TODO Auto-generated method stub
		userName.setText("Welcome " + name);
	}

}
