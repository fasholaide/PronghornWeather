package com.example.bofashola.pronghornweather;

import java.io.IOException;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import com.example.bofashola.pronghornweather.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.location.Address;

public class MapActivity extends FragmentActivity implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private GoogleMap map;
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	private LocationRequest mLocationRequest;
	private double lat, lon;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	boolean mUpdatesRequested = false;
	static String address = "";

	// Handle to SharedPreferences for this app
	SharedPreferences mPrefs;

	// Handle to a SharedPreferences editor
	SharedPreferences.Editor mEditor;
	Button fetchReport;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps_main);
		mLocationRequest = LocationRequest.create();
		mLocationRequest
				.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest
				.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		mUpdatesRequested = false;
		mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES,
				Context.MODE_PRIVATE);
		mEditor = mPrefs.edit();
		mLocationClient = new LocationClient(this, this, this);
		addListenerOnButton();
	}

	public void addListenerOnButton() {
		fetchReport = (Button) findViewById(R.id.button1);

		fetchReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String str = getAddress();
					if (str == null || str.equals(""))
						Toast.makeText(MapActivity.this,
								"Can't Retrieve Address", Toast.LENGTH_SHORT)
								.show();
					else {
						Intent i = new Intent(
								"com.example.bofashola.pronghornweather.DisplayWeatherActivity");
						i.putExtra("location", str);
						startActivity(i);
					}

				} catch (Exception e) {
					Toast.makeText(MapActivity.this,
							"Sorry, your device is not enabled!",
							Toast.LENGTH_SHORT).show();
					onRestart();
				}
			}
		});

	}

	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		if (connectionResult.hasResolution()) {
			try {

				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
				
			} catch (IntentSender.SendIntentException e) {
				Log.d(LocationUtils.APPTAG, e.getMessage());
			}
		} else {
			Log.d(LocationUtils.APPTAG, connectionResult.getErrorCode() + "");
		}

	}

	@SuppressLint("NewApi")
	@Override
	public void onConnected(Bundle bundle) {
		// TODO Auto-generated method stub
		if (mUpdatesRequested) {
			startPeriodicUpdates();
		}
		LatLng myLoc;
		mCurrentLocation = mLocationClient.getLastLocation();
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMyLocationEnabled(true);
		String locationName = "";
		String snipp = "";

		if (mCurrentLocation != null) {
			lon = mCurrentLocation.getLongitude();
			lat = mCurrentLocation.getLatitude();
			snipp = "This is your present location.";
			locationName = "My Location.";
		} else {
			lon = 151.206;
			lat = -33.867;
			locationName = "Default Location";
			snipp = "This is Sydney; your device is not enabled.";
		}
		myLoc = new LatLng(lat, lon);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 13));
		map.addMarker(new MarkerOptions().title(locationName)
				.snippet(snipp).position(myLoc));

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		mLocationClient.connect();
	}

	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		if (mLocationClient.isConnected()) {
			stopPeriodicUpdates();
		}
		mLocationClient.disconnect();
		super.onStop();
	}

	private void stopPeriodicUpdates() {
		mLocationClient.removeLocationUpdates(this);
	}

	private void startPeriodicUpdates() {

		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	@Override
	public void onPause() {

		// Save the current setting for updates
		mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED,
				mUpdatesRequested);
		mEditor.commit();

		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mPrefs.contains(LocationUtils.KEY_UPDATES_REQUESTED)) {
			mUpdatesRequested = mPrefs.getBoolean(
					LocationUtils.KEY_UPDATES_REQUESTED, false);
		} else {
			mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
			mEditor.commit();
		}

	}

	@SuppressLint("NewApi")
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:

			switch (resultCode) {

			case Activity.RESULT_OK:

				Log.d(LocationUtils.APPTAG, getString(R.string.resolved));
				break;
			default:
				Log.d(LocationUtils.APPTAG, getString(R.string.no_resolution));
				break;
			}
		default:
			Log.d(LocationUtils.APPTAG,
					getString(R.string.unknown_activity_request_code,
							requestCode));

			break;
		}

	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d(LocationUtils.APPTAG,
					getString(R.string.play_services_available));

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Display an error dialog
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					this, 0);
			if (dialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				// errorFragment.show(getSupportFragmentManager(),
				// LocationUtils.APPTAG);
			}
			return false;
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@SuppressLint("NewApi")
	public String getAddress() {

		// In Gingerbread and later, use Geocoder.isPresent() to see if a
		// geocoder is available.
		String addressText = "";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
				&& !Geocoder.isPresent()) {
			// No geocoder is present. Issue an error message
			Toast.makeText(this, R.string.no_geocoder_available,
					Toast.LENGTH_LONG).show();
			return "";
		}

		if (servicesConnected()) {
			Location location = mLocationClient.getLastLocation();
			Geocoder geocoder = new Geocoder(MapActivity.this);
			try {
				List<Address> addresses = geocoder.getFromLocation(
						location.getLatitude(), location.getLongitude(), 2);
				addressText = addresses.get(0).getPostalCode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d(LocationUtils.APPTAG, e.getMessage());
				Toast.makeText(MapActivity.this, "You device is not enabled",
						Toast.LENGTH_SHORT).show();
				onRestart();
			}
		}
		return addressText;
	}
}
