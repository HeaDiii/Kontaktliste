package com.example.marcel.kontaktliste;

import android.graphics.drawable.Drawable;

/**
 * Created by Marcel on 14.06.2017.
 */

public class Contact {

    public String name;
    public String company;
    public boolean favorite;
    public String smallImageURL;
    public String largeImageURL;
    public String email;
    public  String website;
    public String birthdate;
    public String phoneWork, phoneHome, phoneMobile;
    public String street, city, state, country, zip;
    public double latitude, longitude;
    public Drawable smallImage, largeImage;

    public Contact(String name, String company, boolean favorite, String smallImageURL, String largeImageURL, String email, String website, String birthdate, String phoneWork, String phoneHome, String phoneMobile, String street, String city, String state, String country, String zip, double latitude, double longitude){
        this.name = name;
        this.company = company;
        this.favorite = favorite;
        this.smallImageURL = smallImageURL;
        this.largeImageURL = largeImageURL;
        this.email = email;
        this.website = website;
        this.birthdate = birthdate;
        this.phoneWork = phoneWork;
        this.phoneHome = phoneHome;
        this.phoneMobile = phoneMobile;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setImages(Drawable smallImage, Drawable largeImage){
        this.smallImage = smallImage;
        this.largeImage = largeImage;
    }

}