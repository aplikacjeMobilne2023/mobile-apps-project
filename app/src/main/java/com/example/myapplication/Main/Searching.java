
package com.example.myapplication.Main;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.myapplication.Database.Activity;
import com.example.myapplication.Database.User;
import com.example.myapplication.R;

public class Searching extends AppCompatActivity {

    List<Activity> list;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    RecyclerViewAdapter myAdapter;

    Date startDateFilterDate;
    Date endDateFilterDate;
    SimpleDateFormat sdf;
    Date date;
    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        getSupportActionBar().hide();

        list = new ArrayList<>();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference(Activity.class.getSimpleName());
        firebaseStorage = FirebaseStorage.getInstance();
        //String activityName = getIntent().getStringExtra("activity");
        String activityName = "jogging";
        String sortBy = "distance";
        int ageMinFilter = 8;
        int ageMaxFilter = 30;
        int distanceFilter = 1000000;
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        String startDateFilter = "23-09-2021";
        String endDateFilter = "30-09-2021";
        String sexFilter = "Male";
        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude",0);
        Location myLocation = new Location("");
        myLocation.setLatitude(latitude);
        myLocation.setLongitude(longitude);
        Date currentDate; // to check if activity current
        try {
            startDateFilterDate = sdf.parse(startDateFilter);
            endDateFilterDate = sdf.parse(endDateFilter);
        }
        catch (Exception e) {

        }

        Query query = databaseReference.orderByChild("activity").equalTo(activityName);
        query.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Activity activity = dataSnapshot.getValue(Activity.class);
                try {
                    date = sdf.parse(activity.getDate());
                }
                catch (Exception e) {
                    activity.setName(e.toString());
                }

                // here check if current outdated
                double distance = distance(myLocation.getLatitude(),myLocation.getLongitude(),activity.getLatitude(),activity.getLongitude());
                if (activity.getActivity().equals(activityName)
                        && ageMinFilter<activity.getAge() && activity.getAge()<ageMaxFilter
                        && (startDateFilterDate.before(date) ||startDateFilterDate.equals(date))
                        && (endDateFilterDate.after(date) ||endDateFilterDate.equals(date))
                        && (sexFilter.equals(activity.getSex()) || activity.getSex() == null)
                        && (distance < distanceFilter)
                        && (activity.getUserId() != mAuth.getCurrentUser().getUid())) {

                    LocalDate localDate = LocalDate.now();
                    ZoneId zoneId = ZoneId.systemDefault();
                    ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
                    Date utilDate = Date.from(zonedDateTime.toInstant());

                    Calendar a = getCalendar(date);
                    Calendar b = getCalendar(utilDate);
                    long daysBetween = ChronoUnit.DAYS.between(a.toInstant(), b.toInstant());

                    activity.setDaysTo(daysBetween);

                    activity.setDistance(Math.round(distance/100)/10.0); //Math.round(distance/100)/10
                    list.add(activity);
                }

                if(sortBy == "date") {
                    Collections.sort(list, new Comparator<Activity>() {
                        @Override
                        public int compare(Activity a1, Activity a2) {
                            Date d1 = null;
                            Date d2 = null;
                            try {
                                d1 = sdf.parse(a1.getDate());
                                d2 = sdf.parse(a2.getDate());
                            }
                            catch (Exception e) {
                            }
                            return d1.compareTo(d2);
                        }
                    });
                }
                else if(sortBy == "distance") {
                    Collections.sort(list, new Comparator<Activity>() {
                        @Override
                        public int compare(Activity a1, Activity a2) {
                            if (distance(myLocation.getLatitude(),myLocation.getLongitude(),a1.getLatitude(),a1.getLongitude())
                                    > distance(myLocation.getLatitude(),myLocation.getLongitude(),a2.getLatitude(),a2.getLongitude())) {
                                return 1;
                            }
                            else return -1;
                        }
                    });
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        myAdapter = new RecyclerViewAdapter(this, list);
        myrv.setLayoutManager(new GridLayoutManager(this,2));
        myrv.setAdapter(myAdapter);

    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515* 1.609344;
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

}