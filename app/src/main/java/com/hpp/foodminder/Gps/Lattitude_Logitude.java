package com.hpp.foodminder.Gps;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Lattitude_Logitude extends Service implements LocationListener {

	private final Context mContext;

	// flag for GPS status
	boolean isGPSEnabled = false;
	// flag for network status
	boolean isNetworkEnabled = false;
	// flag for GPS status
	boolean canGetLocation = false;

	Location location; // location
	double latitude; // latitude
	double longitude; // longitude
	static String countryName = null;
	static String countryCode = null;
	static String cityName = null;

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	// Declaring a Location Manager
	protected LocationManager locationManager;

	public Lattitude_Logitude(Context context) {
		this.mContext = context;
		getLocation();
	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
			// getting GPS status
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				Log.e("no network", "isGPSEnabled .."+ isGPSEnabled+"..isNetworkEnabled.."+isNetworkEnabled);
				// no network provider is enabl
			} else {
				this.canGetLocation = true;

				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
//							Log.e("no network provider is enabled ", "latitude .."+ latitude+".......longitude...."+longitude
//									+"\n"+countryName+""+countryCode);
//							getLocationFormGoogle(latitude, longitude);

						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");

						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
								//getLocationFormGoogle(latitude, longitude);
								//onLocationCountry();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}

	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 * */
	public void stopUsingGPS(){
		if(locationManager != null){
			locationManager.removeUpdates(Lattitude_Logitude.this);
		}
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude(){
		if(location != null){
			latitude = location.getLatitude();
		}
		// return latitude
		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		}
		// return longitude
		return longitude;
	}

	/**
	 * Function to check GPS/wifi enabled
	 * @return boolean
	 * */
	public boolean canGetLocation(){
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 * */
	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");
		// Setting Dialog Message
		alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
			}
		});
		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}


//	public static void getLocationCityName(double lat, double lon ){
//	    getLocationFormGoogle();
////	    return getCityAddress(result);
//	}





	protected static String getCityAddress( JSONObject result ){
		if( result.has("results") ){
			try {

				//Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
				//startActivity(intent);
				JSONArray array = result.getJSONArray("results");
				if(array.length() > 0 ){

					JSONObject place = array.getJSONObject(0);
					JSONArray components = place.getJSONArray("address_components");

					for( int i = 0 ; i < components.length() ; i++ ){
						JSONObject component = components.getJSONObject(i);
						JSONArray types = component.getJSONArray("types");
						for( int j = 0 ; j < types.length() ; j ++ ){
							if( types.getString(j).equals("country") ){
								countryName= component.getString("long_name");
								countryCode= component.getString("short_name");
								return component.getString("long_name");
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return null;
	}




	public String onLocationCountry() {
        /*----------to get City-Name from coordinates ------------- */
		//String address = "";
		Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
		List<Address>  addresses = null;


		Geocoder geocoder;

		geocoder = new Geocoder(mContext, Locale.getDefault());

		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // Here 1 represent max location result to returned, by documents it recommended 1 to 5

		String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
		String city = addresses.get(0).getLocality();
		String state = addresses.get(0).getAdminArea();
		String country = addresses.get(0).getCountryName();
		String postalCode = addresses.get(0).getPostalCode();
		String knownName = addresses.get(0).getFeatureName();

		Log.e("", "address : "	+ addresses);
		Log.e("", "city : "		+ city);
		Log.e("", "state : "	+ state);
		Log.e("", "country : "	+ country);
		Log.e("", "postalCode : "+ postalCode);
		Log.e("", "knownName : "+ knownName);


		try {
			System.out.println(longitude+"   lat/// long"+latitude);
			addresses = gcd.getFromLocation(latitude, longitude, 1);
			if (addresses.size() > 0){
				System.out.println(addresses.get(0).getLocality());
				///countryName=addresses.get(0).getLocality();
				countryName= addresses.get(0).getCountryName();
				countryCode= addresses.get(0).getCountryCode();
				cityName = addresses.get(0).getLocality();
				//countryName= countryName+"(+"+country_code+")";
			}

			if (addresses != null && addresses.size() > 0) {
				address = addresses.get(0).getAddressLine(0);

				if (addresses != null && addresses.size() >= 1) {
					address += ", " + addresses.get(0).getAddressLine(1);
				}

				if (addresses != null && addresses.size() >= 2) {
					address += ", " + addresses.get(0).getAddressLine(2);
				}
			}
			System.out.println(address);

		} catch (IOException e) {
			e.printStackTrace();
		}
		String s = longitude+"\n"+latitude + "\n\nMy Currrent Country is: "+countryName;
		Log.e("", "address : "+ addresses);
		System.out.println(s+""+ addresses.size());
		return knownName;
	}


	public String getCountryName() {
		return countryName;
	}

	public void setCityName(String ciytName) {
		Lattitude_Logitude.cityName = ciytName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCountryName(String countryName) {
		Lattitude_Logitude.countryName = countryName;
	}


	public String getCountryCode() {
		return countryCode;
	}


	public void setCountryCode(String countryCode) {
		Lattitude_Logitude.countryCode = countryCode;
	}

	public static boolean isNetworkAvailable(Context context) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			return activeNetworkInfo != null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
