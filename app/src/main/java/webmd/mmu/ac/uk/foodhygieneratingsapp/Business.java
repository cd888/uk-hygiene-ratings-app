package webmd.mmu.ac.uk.foodhygieneratingsapp;

import java.io.Serializable;

/**
 * This class is the Business object class in which the app's main objects are created
 * @author Christopher Daly 
 * @version 27/03/2016
 */


public class Business implements Serializable {

    /** These are all the variables which this class uses
     *
     */

    protected String id;
    protected String businessName;
    protected String addressLine1;
    protected String addressLine2;
    protected String addressLine3;
    protected String postCode;
    protected String ratingValue;
    protected String ratingDate;
    protected String longitude;
    protected String latitude;
    protected String distance;

    /**
     * The following methods are the getters and setters for the Business Class
     * @return
     */



    public String getId() {
        return id;
    }

    public void setId() {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName() {
        this.businessName = businessName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1() {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2() {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3() {
        this.addressLine3 = addressLine3;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode() {
        this.postCode = postCode;
    }

    public String getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue() {
        this.ratingValue = ratingValue;
    }

    public String getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate() {
        this.ratingDate = ratingDate;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude() {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude() {
        this.latitude = latitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance() {
        this.distance = distance;
    }

    /**
     * This contains an empty constructor for the class
     */

    public Business(){

    }


    /**
     * The following method outputs a String for all the business objects.
     * @return
     */


    public String toString(){
        String output;
        output = businessName + addressLine1 + addressLine2 + addressLine3 + postCode + ratingValue +
                ratingDate + latitude + longitude + distance;
        return output;
    }
}
