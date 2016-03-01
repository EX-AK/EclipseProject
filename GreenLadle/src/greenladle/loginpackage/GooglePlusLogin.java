/*package greenladle.loginpackage;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

public class GooglePlusLogin implements ConnectionCallbacks,OnConnectionFailedListener{

	private boolean mIsResolving = false;
	private boolean mShouldResolve = false;
	private static final int RC_SIGN_IN = 0;
	private GoogleApiClient mGoogleClient;
	Context context;
	
	public GooglePlusLogin(Context context) {
		// TODO Auto-generated constructor stub
		mGoogleClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		this.context=context;
	}
	
	
	private void onSignInClicked() {
		// User clicked the sign-in button, so begin the sign-in process and
		// automatically
		// attempt to resolve any errors that occur.

		mShouldResolve = true;
		mGoogleClient.connect();

	}
	
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		
		if (!mIsResolving && mShouldResolve) {

			if (connectionResult.hasResolution()) {
				try {
					//connectionResult.startResolutionForResult(context, RC_SIGN_IN);
					mIsResolving = true;
				} catch (IntentSender.SendIntentException e) {
					Log.e("GoogleLogin", "Could not resolve ConnectionResult.", e);
					mIsResolving = false;
					mGoogleClient.connect();
				}
			} else {
				// Could not resolve the connection result, show the user an
				// error dialog.
				// showErrorDialog(connectionResult);
			}
		} else {
			// Show the signed-out UI
			// showSignedOutUI();
		}
		
	}



	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}

}
*/