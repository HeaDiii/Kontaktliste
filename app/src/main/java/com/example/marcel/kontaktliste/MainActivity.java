package com.example.marcel.kontaktliste;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ToggleButton mFavoriteToggle;
    private ToggleButton mEditToggle;
    private List<Contact> mMyContacts = new ArrayList<Contact>();
    private Contact mCurrentContact;

    EditText nameText;
    EditText companyText;
    EditText phoneWorkText;
    EditText phoneHomeText;
    EditText phoneMobileText;
    EditText streetText;
    EditText stateText;
    EditText zipText;
    EditText cityText;
    EditText birthdayText;
    EditText emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = (EditText) findViewById(R.id.name_text);
        companyText = (EditText) findViewById(R.id.company_text);
        phoneWorkText = (EditText) findViewById(R.id.phone_work_text);
        phoneHomeText = (EditText) findViewById(R.id.phone_home_text);
        phoneMobileText = (EditText) findViewById(R.id.phone_mobile_text);
        streetText = (EditText) findViewById(R.id.street_text);
        stateText = (EditText) findViewById(R.id.state_text);
        zipText = (EditText) findViewById(R.id.zip_text);
        cityText = (EditText) findViewById(R.id.city_text);
        birthdayText = (EditText) findViewById(R.id.birthday_text);
        emailText = (EditText) findViewById(R.id.email_text);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        setInputsEditable(false);

        mFavoriteToggle = (ToggleButton) findViewById(R.id.favorite_toggleButton);
        mFavoriteToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCurrentContact.favorite = isChecked;
            }
        });

        mEditToggle = (ToggleButton) findViewById(R.id.edit_toggleButton);
        mEditToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setInputsEditable(true);
                } else {
                    setInputsEditable(false);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DownloadTask task = new DownloadTask();
        try {
            String result = task.execute("https://waltken.de/Contacts_v2.json").get();
            //Log.i("Result", result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void setInputsEditable(boolean value){
            nameText.setEnabled(value);
            companyText.setEnabled(value);
            phoneWorkText.setEnabled(value);
            phoneHomeText.setEnabled(value);
            phoneMobileText.setEnabled(value);
            streetText.setEnabled(value);
            stateText.setEnabled(value);
            zipText.setEnabled(value);
            cityText.setEnabled(value);
            birthdayText.setEnabled(value);
            emailText.setEnabled(value);
    }

    public void hideLoadingIndicator(){
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView loadingText = (TextView) findViewById(R.id.textView_loading);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);

        progressBar.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }

    public void setupMenu(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        ListView listView = (ListView) findViewById(R.id.list_view);

        Menu menu = navigationView.getMenu();
        menu.clear();

        int index = 0;
        for(Iterator<Contact> i = mMyContacts.iterator(); i.hasNext();){
            final Contact contact = i.next();
            menu.add(0, Menu.FIRST + index++, Menu.NONE, contact.name)
                    .setIcon(contact.smallImage)
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return setupView(contact);
                }
            });
        }

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        navigationView.setItemIconTintList(null);
    }

    public boolean setupView(Contact contact){
        mDrawerLayout.closeDrawers();

        mCurrentContact = contact;
        mFavoriteToggle.setChecked(contact.favorite);
        mEditToggle.setChecked(false);

        nameText.setText(contact.name);
        companyText.setText(contact.company);
        phoneWorkText.setText(contact.phoneWork);
        phoneHomeText.setText(contact.phoneHome);
        phoneMobileText.setText(contact.phoneMobile);
        streetText.setText(contact.street);
        stateText.setText(contact.state);
        zipText.setText(contact.zip);
        cityText.setText(contact.city);
        birthdayText.setText(contact.birthdate);
        emailText.setText(contact.email);

        ImageView imageView = (ImageView) findViewById(R.id.large_image);
        if(contact.largeImage == null){
            Log.i("Error", "largeImage is empty");
            imageView.setImageDrawable(null);
        } else {
            imageView.setImageDrawable(contact.largeImage);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //Log.i("URL", "Hole daten von "+params[0]);
            String result = "";
            HttpURLConnection connection = null;
            InputStreamReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new InputStreamReader(stream);

                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    boolean favorite;
                    String mobile;

                    try {
                        favorite = jsonObject.getBoolean("favorite");
                    } catch (Exception e) {
                        if (favorite = jsonObject.getInt("favorite") == 1) {
                            favorite = true;
                        } else {
                            favorite = false;
                        }
                    }

                    try {
                        mobile = jsonObject.getJSONObject("phone").getString("mobile");
                    } catch (Exception e) {
                        mobile = "";
                    }

                    mMyContacts.add(new Contact(jsonObject.getString("name"), jsonObject.getString("company"), favorite, jsonObject.getString("smallImageURL"), jsonObject.getString("largeImageURL"), jsonObject.getString("email"), jsonObject.getString("website"), jsonObject.getString("birthdate"), jsonObject.getJSONObject("phone").getString("work"), jsonObject.getJSONObject("phone").getString("home"), mobile, jsonObject.getJSONObject("address").getString("street"), jsonObject.getJSONObject("address").getString("city"), jsonObject.getJSONObject("address").getString("state"), jsonObject.getJSONObject("address").getString("country"), jsonObject.getJSONObject("address").getString("zip"), jsonObject.getJSONObject("address").getDouble("latitude"), jsonObject.getJSONObject("address").getDouble("longitude")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < mMyContacts.size(); i++) {
                Log.i("Contact", mMyContacts.get(i).name);
            }

            ImageDownload imageDownload = new ImageDownload();
            imageDownload.execute();
        }


    }
    public class ImageDownload extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            Drawable smallImage = null, largeImage = null;

            for(int i = 0; i < mMyContacts.size(); i++){
                try {
                    smallImage = DownloadDrawable(mMyContacts.get(i).smallImageURL, "smallImage");
                    largeImage = DownloadDrawable(mMyContacts.get(i).largeImageURL, "largeImage");

                    mMyContacts.get(i).setImages(smallImage, largeImage);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result){
            super.onPostExecute(result);

            setupMenu();
            setupView(mMyContacts.get(0));
            hideLoadingIndicator();
        }

        Drawable DownloadDrawable(String url, String src_name) throws java.io.IOException {
            try{
                return Drawable.createFromStream(((java.io.InputStream) new java.net.URL(url).getContent()), src_name);
            } catch(Exception e){
                //e.printStackTrace();
                return null;
            }
        }
    }
}
