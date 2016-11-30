package webmd.mmu.ac.uk.foodhygieneratingsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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


/** This is the main activity class for the app.
 *  Here is where the user fills in the information and searches through the results.
 *
 *  @author Christopher Daly
 *  @version 27/03/2015
 *
 */

public class MainActivity extends AppCompatActivity {

    /**
     * These are the variables which are used throughout the class.
     */

    private static ArrayList <Business> bList = new ArrayList<>();
    public static String serverURL = "serverURL";

    private double lat = 0;
    private double lon = 0;

    /**
     *This is the onCreate method which creates the user interface once the app is started.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /**
         * This permits all the processing of the map to be made on the same thread without producing an error.
         */

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /**
         * This part creates a Location Manager which can then use the phone's GPS to obtain the current location.
         * This is included in a security exception that can be produced by doing this.
         */

        try{
            Context context = this.getApplicationContext();
            LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lat =  location.getLatitude();
                    lon = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

        } catch (SecurityException e) {throw new ArrayIndexOutOfBoundsException();}


    }

    /**
     * This method creates the options menu for the app.
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This method creates any items for the options menu.
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method deletes any data in the Table and also the Array List where all the information is stored.
     */

    public void clearResults (){

        TableLayout tLay = (TableLayout)this.findViewById(R.id.tableLayout1);
        try{
            tLay.removeAllViews();
        }catch (NullPointerException e){e.printStackTrace();}
        bList.clear();

    }

    /**
     * This method obtains the data from the server and then places that in the table depending on the URL that is given by the other methods.
     * @param urlStr
     */


    public void getJsonAndDisplay(String urlStr){


        TableLayout tLay = (TableLayout)this.findViewById(R.id.tableLayout1);
        clearResults();


        try{

            /**
             * This loops round JSON Array and adds the data to an ArrayList of the Business Class.
             */

            URL hygiene = new URL(urlStr);
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
                    b.addressLine2 = jo.getString("AddressLine2");
                    b.addressLine3 = jo.getString("AddressLine3");
                    b.postCode = jo.getString("PostCode");
                    b.ratingValue = jo.getString("RatingValue");
                    b.ratingDate = jo.getString("RatingDate");
                    b.latitude = jo.getString("Latitude");
                    b.longitude = jo.getString("Longitude");

                    bList.add(b);

                }

            }

            /**
             * This part loops around the ArrayList and then adds the data to the Table for it to be displayed.
             */

            for (int i=0; i<bList.size();i++){

                TableRow tRow1 = new TableRow(this.getApplicationContext());
                TableRow tRow2 = new TableRow(this.getApplicationContext());
                TableRow tRow3 = new TableRow(this.getApplicationContext());
                TextView tbName = new TextView(this.getApplicationContext());
                TextView tAdd1 = new TextView(this.getApplicationContext());
                TextView tAdd2 = new TextView(this.getApplicationContext());
                TextView tAdd3 = new TextView(this.getApplicationContext());
                TextView tPost = new TextView(this.getApplicationContext());
                TextView tRDate = new TextView(this.getApplicationContext());
                ImageView ratingView = new ImageView(this.getApplicationContext());


                Business b = bList.get(i);

                TableRow.LayoutParams tlp = new TableRow.LayoutParams();
                tlp.setMargins(1,1,1,1);


                tbName.setText(b.getBusinessName());
                tbName.setTextColor(Color.BLACK);
                tbName.setTextSize(14);
                tbName.setTypeface(null, Typeface.BOLD);
                tbName.setLayoutParams(tlp);
                tbName.setPadding(0, 0, 1, 0);
                tAdd1.setText(b.getAddressLine1());
                tAdd1.setTextColor(Color.BLACK);
                tAdd1.setPadding(0, 1, 1, 1);
                tAdd1.setLayoutParams(tlp);
                tAdd2.setText(b.getAddressLine2());
                tAdd2.setTextColor(Color.BLACK);
                tAdd2.setLayoutParams(tlp);
                tAdd3.setText(b.getAddressLine3());
                tAdd3.setTextColor(Color.BLACK);
                tAdd3.setLayoutParams(tlp);
                tPost.setText(b.getPostCode());
                tPost.setTextColor(Color.BLACK);
                tPost.setLayoutParams(tlp);
                tRDate.setText(b.getRatingDate());
                tRDate.setTextColor(Color.BLACK);
                tRDate.setLayoutParams(tlp);

                /**
                 * This is a switch statement that adds an image to the table depending on what the rating is.
                 */


                switch (b.getRatingValue()) {
                    case "0":
                        ratingView.setImageResource(R.drawable.rating_0);
                        break;
                    case "1":
                        ratingView.setImageResource(R.drawable.rating_1);
                        break;
                    case "2":
                        ratingView.setImageResource(R.drawable.rating_2);
                        break;
                    case "3":
                        ratingView.setImageResource(R.drawable.rating_3);
                        break;
                    case "4":
                        ratingView.setImageResource(R.drawable.rating_4);
                        break;
                    case "5":
                        ratingView.setImageResource(R.drawable.rating_5);
                        break;
                    case "-1":
                        ratingView.setImageResource(R.drawable.exempt);
                        break;
                }

                TableRow.LayoutParams ilp = new TableRow.LayoutParams();
                ilp.width = 400;
                ilp.height = 150;


                ratingView.setLayoutParams(ilp);
                ratingView.setAdjustViewBounds(true);
                ratingView.setPadding(0, 1, 0, 0);



                tRow1.addView(tbName);

                tRow2.addView(tAdd1);
                tRow2.addView(tAdd2);
                tRow2.addView(tAdd3);
                tRow2.addView(tPost);

                tRow3.addView(ratingView);
                tRow3.addView(tRDate);


                tLay.addView(tRow1);
                tLay.addView(tRow2);
                tLay.addView(tRow3);

            }

        }catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();}




    }

    /**
     * This is an onclick method for searching by name.
     * It uses data from a text box and then use that to produce a URL and use getJsonAndDisplay method.
     * @param v
     */

    public void name_search_onclick (View v){


        clearResults();
        EditText dis_name = (EditText) this.findViewById(R.id.editText);
        String nameURL = "s_name&name=";
        String name = dis_name.getText().toString();
        String namec = name.replaceAll(" ","%20");

        getJsonAndDisplay(serverURL + nameURL + namec);


        dis_name.setText("");


    }

    /**
     * This is an onclick method for searching by postcode.
     * It uses data from a text box and then use that to produce a URL and use getJsonAndDisplay method.
     * @param v
     */

    public void postcode_search_onclick (View v){


        clearResults();
        EditText post_code = (EditText) this.findViewById(R.id.editText2);
        String postCodeURL = "s_postcode&postcode=";
        String postCode = post_code.getText().toString();
        String postCodec = postCode.replaceAll(" ","%20");

        getJsonAndDisplay(serverURL + postCodeURL + postCodec);

        post_code.setText("");

    }


    /**
     * This is an onclick method for searching by current location.
     * It uses data from the phone's current location and then produces the results in the table.
     * @param v
     */

    public void current_loca_onclick (View v){


        TableLayout tLay = (TableLayout)this.findViewById(R.id.tableLayout1);
        clearResults();
        String locationURL = "s_loc";
        String latURL = "&lat=";
        String lati = Double.toString(lat);
        String lonURL = "&long=";
        String longi = Double.toString(lon);

        System.out.println("Debug url" + serverURL + locationURL + latURL + lati + lonURL + longi);

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
                    b.addressLine2 = jo.getString("AddressLine2");
                    b.addressLine3 = jo.getString("AddressLine3");
                    b.postCode = jo.getString("PostCode");
                    b.ratingValue = jo.getString("RatingValue");
                    b.ratingDate = jo.getString("RatingDate");
                    b.latitude = jo.getString("Latitude");
                    b.longitude = jo.getString("Longitude");
                    b.distance = jo.getString("DistanceKM");


                    bList.add(b);

                }

            }

            /**
             * This part loops around the ArrayList and then adds the data to the Table for it to be displayed.
             */

            for (int i=0; i<bList.size();i++){

                TableRow tRow1 = new TableRow(this.getApplicationContext());
                TableRow tRow2 = new TableRow(this.getApplicationContext());
                TableRow tRow3 = new TableRow(this.getApplicationContext());
                TextView tbName = new TextView(this.getApplicationContext());
                TextView tAdd1 = new TextView(this.getApplicationContext());
                TextView tAdd2 = new TextView(this.getApplicationContext());
                TextView tAdd3 = new TextView(this.getApplicationContext());
                TextView tPost = new TextView(this.getApplicationContext());
                TextView tRDate = new TextView(this.getApplicationContext());
                TextView tDistance = new TextView(this.getApplicationContext());
                ImageView ratingView = new ImageView(this.getApplicationContext());


                Business b = bList.get(i);

                TableRow.LayoutParams tlp = new TableRow.LayoutParams();
                tlp.setMargins(1,1,1,1);


                tbName.setText(b.getBusinessName());
                tbName.setTextColor(Color.BLACK);
                tbName.setTextSize(14);
                tbName.setTypeface(null, Typeface.BOLD);
                tbName.setLayoutParams(tlp);
                tbName.setPadding(0, 0, 1, 0);
                tAdd1.setText(b.getAddressLine1());
                tAdd1.setTextColor(Color.BLACK);
                tAdd1.setPadding(0, 1, 1, 1);
                tAdd1.setLayoutParams(tlp);
                tAdd2.setText(b.getAddressLine2());
                tAdd2.setTextColor(Color.BLACK);
                tAdd2.setLayoutParams(tlp);
                tAdd3.setText(b.getAddressLine3());
                tAdd3.setTextColor(Color.BLACK);
                tAdd3.setLayoutParams(tlp);
                tPost.setText(b.getPostCode());
                tPost.setTextColor(Color.BLACK);
                tPost.setLayoutParams(tlp);
                tRDate.setText(b.getRatingDate());
                tRDate.setTextColor(Color.BLACK);
                tRDate.setLayoutParams(tlp);
                tDistance.setText("Distance: " + b.getDistance());
                tDistance.setTextColor(Color.BLACK);
                tDistance.setLayoutParams(tlp);

                /**
                 * This is a switch statement that adds an image to the table depending on what the rating is.
                 */



                switch (b.getRatingValue()) {
                    case "0":
                        ratingView.setImageResource(R.drawable.rating_0);
                        break;
                    case "1":
                        ratingView.setImageResource(R.drawable.rating_1);
                        break;
                    case "2":
                        ratingView.setImageResource(R.drawable.rating_2);
                        break;
                    case "3":
                        ratingView.setImageResource(R.drawable.rating_3);
                        break;
                    case "4":
                        ratingView.setImageResource(R.drawable.rating_4);
                        break;
                    case "5":
                        ratingView.setImageResource(R.drawable.rating_5);
                        break;
                    case "-1":
                        ratingView.setImageResource(R.drawable.exempt);
                        break;
                }

                TableRow.LayoutParams ilp = new TableRow.LayoutParams();
                ilp.width = 400;
                ilp.height = 150;


                //ratingView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ratingView.setLayoutParams(ilp);
                ratingView.setAdjustViewBounds(true);
                ratingView.setPadding(0,1,0,0);



                tRow1.addView(tbName);
                tRow1.addView(tDistance);

                tRow2.addView(tAdd1);
                tRow2.addView(tAdd2);
                tRow2.addView(tAdd3);
                tRow2.addView(tPost);

                tRow3.addView(ratingView);
                tRow3.addView(tRDate);


                tLay.addView(tRow1);
                tLay.addView(tRow2);
                tLay.addView(tRow3);


            }

        }catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();
        }

    }

    /**
     * This is an onclick method for searching by most recent to be added to the server.
     * It uses data the recent URL and then uses the getJsonAndDisplay method.
     * @param v
     */

    public void most_recent_onclick (View v){

        clearResults();
        String recentURL = "s_recent";

        getJsonAndDisplay(serverURL + recentURL);

    }

    /**
     * This is an onclick method for clearing the table and ArrayList.
     * It uses the clear results method..
     * @param v
     */

    public void clear_onclick (View v){

        clearResults();

    }

    /**
     * This is an onclick method for starting the app's map view.
     * It uses an intent to start and transfer the view to the maps activity.
     * @param v
     */

    public void map_onclick (View v){


        Intent in = new Intent(this, MapsActivity.class);

            startActivity(in);

    }



}
