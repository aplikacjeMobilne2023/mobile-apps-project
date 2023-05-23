package com.example.myapplication.Main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.Activity;
import com.example.myapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityWillingUsers extends AppCompatActivity implements LocationListener {

    List<Activity> list;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    UserActivityRecyclerViewAdapter myAdapter;

    Date startDateFilterDate;
    Date endDateFilterDate;
    SimpleDateFormat sdf;
    Date date;
    LocationManager mLocationManager;

    double latitude;
    double longitude;

    private static final int MINIMUM_TIME = 10000;  // 10s
    private static final int MINIMUM_DISTANCE = 50; // 50m

    /* GPS Constant Permission */
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        getSupportActionBar().hide();

        list = new ArrayList<>();

        StorageReference storageProfilePicRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        firebaseDatabase = FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference(Activity.class.getSimpleName());
        firebaseStorage = FirebaseStorage.getInstance();
        String activityName = "jogging";
        String sortBy = "distance";
        int ageMinFilter = 8;
        int ageMaxFilter = 30;
        int distanceFilter = 1000000;
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        String startDateFilter = "23-09-2021";
        String endDateFilter = "30-09-2021";
        String sexFilter = "Male";
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String mProviderName = mLocationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // No one provider activated: prompt GPS
            if (mProviderName == null || mProviderName.equals("")) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            // At least one provider activated. Get the coordinates
            switch (mProviderName) {
                case "passive":
                    mLocationManager.requestLocationUpdates(mProviderName, MINIMUM_TIME, MINIMUM_DISTANCE, this);
                    Location location = mLocationManager.getLastKnownLocation(mProviderName);
                    break;

                case "network":
                    break;

                case "gps":
                    break;

            }

            // One or both permissions are denied.
        } else {

            // The ACCESS_COARSE_LOCATION is denied, then I request it and manage the result in
            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_ACCESS_COARSE_LOCATION);
            }
            // The ACCESS_FINE_LOCATION is denied, then I request it and manage the result in
            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_ACCESS_FINE_LOCATION);
            }

        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        // Logic to handle location object
                    }
                });
        Date currentDate; // to check if activity current
        try {
            startDateFilterDate = sdf.parse(startDateFilter);
            endDateFilterDate = sdf.parse(endDateFilter);
        } catch (Exception e) {

        }

        Query query = databaseReference.orderByChild("activity").equalTo(activityName);
        query.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Activity activity = dataSnapshot.getValue(Activity.class);
                try {
                    date = sdf.parse(activity.getDate());
                } catch (Exception e) {
                    activity.setName(e.toString());
                }
                // here check if current outdated
                double distance = distance(latitude, longitude, activity.getLatitude(), activity.getLongitude());
                if (activity.getActivity().equals(activityName)
                        && ageMinFilter < activity.getAge() && activity.getAge() < ageMaxFilter
                        && (startDateFilterDate.before(date) || startDateFilterDate.equals(date))
                        && (endDateFilterDate.after(date) || endDateFilterDate.equals(date))
                        && (sexFilter.equals(activity.getSex()) || activity.getSex() == null)
                        && (distance < distanceFilter)
                ) { //TODO add this: && (activity.getUserId() != mAuth.getCurrentUser().getUid())

                    LocalDate localDate = LocalDate.now();
                    ZoneId zoneId = ZoneId.systemDefault();
                    ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
                    Date utilDate = Date.from(zonedDateTime.toInstant());

                    Calendar a = getCalendar(date);
                    Calendar b = getCalendar(utilDate);
                    long daysBetween = ChronoUnit.DAYS.between(a.toInstant(), b.toInstant());

                    activity.setDaysTo(daysBetween);

                    final StorageReference fileRef = storageProfilePicRef
                            .child(activity.getUserId()+".jpg");
                    fileRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            activity.setUserPhoto(bitmap);
                        }
                    });


                    activity.setDistance(Math.round(distance / 100) / 10.0); //Math.round(distance/100)/10
                    list.add(activity);
                }

                if (sortBy.equals("date")) {
                    Collections.sort(list, (a1, a2) -> {
                        Date d1 = null;
                        Date d2 = null;
                        try {
                            d1 = sdf.parse(a1.getDate());
                            d2 = sdf.parse(a2.getDate());
                        } catch (Exception e) {
                        }
                        return d1.compareTo(d2);
                    });
                } else if (sortBy == "distance") {
                    Collections.sort(list, (a1, a2) -> {
                        if (distance(latitude, longitude, a1.getLatitude(), a1.getLongitude())
                                > distance(latitude, longitude, a2.getLatitude(), a2.getLongitude())) {
                            return 1;
                        } else return -1;
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

        RecyclerView myrv = findViewById(R.id.recyclerview_id);
        myAdapter = new UserActivityRecyclerViewAdapter(this, list);
        myrv.setLayoutManager(new GridLayoutManager(this, 2));
        myrv.setAdapter(myAdapter);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;
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