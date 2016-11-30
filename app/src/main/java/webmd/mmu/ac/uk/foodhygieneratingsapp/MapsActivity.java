package webmd.mmu.ac.uk.foodhygieneratingsapp;


import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**This is the maps activity class for the app.
 * Here is where the google map is displayed showing the user's current location and the ratings of businesses around.
 * @author Christopher Daly 
 * @version 27/03/2016
 *
 */

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, LocationListener {


    /**
     * This includes the main variables used by the MapsActivity.
     */

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private LocationRequest mLocationRequest;

    /**
     * This is the onCreate method which creates the user interface once the activity is started.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /**
         * This created the GoogleApiClient which is used by the app to access Google Maps features.
         */


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        mLocationRequest = LocationRequest.create();
    }

    /**
     * This is the onResume method which connects the ApiClient once the activity has been resumed from its state.
     */

    @Override
    protected void onResume(){
        super.onResume();
        mGoogleApiClient.connect();

    }

    /**
     * This is the onPause method which disconnects the ApiClient once the activity has been paused from its state.
     */

    @Override
    protected void onPause(){
        super.onPause();
        if (mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng start = new LatLng(54.00366, -2.547855);
        mMap.addMarker(new MarkerOptions().position(start));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));


    }

    /**
     * This method obtains the data from the server and then places that information about the Businesses as markers on the map.
     * @param location
     */

    public void businessRate (Location location){

        ArrayList<Business> bList = new ArrayList<>();


        double lat = location.getLatitude();
        double lon = location.getLongitude();
        LatLng currLoc = new LatLng(lat, lon);
        String serverURL = "http://sandbox.kriswelsh.com/hygieneapi/hygiene.php?op=";
        String locationURL = "s_loc";
        String lati = Double.toString(lat);
        String latURL = "&lat=";
        String lonURL = "&long=";
        String longi = Double.toString(lon);


        try{

            /**
             * This loops round JSON Array and adds the data to an ArrayList of the Business Class.
             */

            URL hygiene = new URL(serverURL + locationURL + latURL + lati + lonURL + longi);
            HttpURLConnection hy = (HttpURLConnection) hygiene.openConnection();
            InputStreamReader isr = new InputStreamReader(hy.getInputStream());
            BufferedReader in = new BufferedReader(isr);

            String line;
            while ((line = in.readLine()) !=null){
                JSONArray ja = new JSONArray(line);
                for (int i =0; i<ja.length(); i++){
                    JSONObject jo = (JSONObject) ja.get(i);
                    Business b = new Business();
                    b.businessName = jo.getString("BusinessName");
                    b.addressLine1 = jo.getString("AddressLine1");
                    b.postCode = jo.getString("PostCode");
                    b.ratingValue = jo.getString("RatingValue");
                    b.latitude = jo.getString("Latitude");
                    b.longitude = jo.getString("Longitude");
                    b.distance = jo.getString("DistanceKM");


                    bList.add(b);
                }

            }

            /**
             * This part loops around the ArrayList and then adds the data onto the map in the form of markers.
             */

            for (int i=0; i<bList.size();i++){


                Business b = bList.get(i);

                double locLat = Double.parseDouble(b.getLatitude());
                double locLon = Double.parseDouble(b.getLongitude());
                LatLng bLoc = new LatLng(locLat,locLon);


                mMap.addMarker(new MarkerOptions().position(bLoc).title(b.businessName).snippet(b.getAddressLine1()
                        + "\n" + b.getPostCode() + "\n" + "Distance: " + b.getDistance()));


            }


        }catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();
        }

        /**
         * This moves the camera to the user's current location on the map, showing an overview of the area.
         */

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 15));

    }




    /**
     * This method invokes the businessRate method every time the device's location changes.
     * @param location
     */

    @Override
    public void onLocationChanged (Location location){

        businessRate(location);

    }


    /**
     * This method retrieves the current location and then places a marker in the place.
     * @param location
     */

    private void giveNewLocation(Location location){

        Log.d(TAG, location.toString());

        double currentLat = location.getLatitude();
        double currentLon = location.getLongitude();

        LatLng latLng = new LatLng(currentLat, currentLon);

        MarkerOptions mOptions = new MarkerOptions().position(latLng);

        mMap.addMarker(mOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    /**
     * This method uses the Location Services once it is connected to then use the giveNewLocation method.
     * @param bundle
     */

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location Services connected.");
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null){
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            }
            else {
                giveNewLocation(location);
            }


        }catch (SecurityException e) {throw new ArrayIndexOutOfBoundsException();}

    }

    /**
     * This method notifies the console if the connection to Location Services has been suspended.
     * @param i
     */

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location Services suspended.");

    }

    /**
     * This method notifies the console if the connection to Location Services has failed to be established.
     * @param connectionResult
     */

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection Failed");

    }




}
