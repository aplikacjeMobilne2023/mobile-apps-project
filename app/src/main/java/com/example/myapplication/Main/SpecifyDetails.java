package com.example.myapplication.Main; // uzytkownik moze miec wylaczona lokalizacje


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.Database.Activity;
import com.example.myapplication.Database.DAOActivity;
import com.example.myapplication.Database.User;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SpecifyDetails extends AppCompatActivity implements LocationListener {

    private Date birthday;
    private int diff;
    private Activity newActivity;
    double latitude;
    double longitude;
    LocationManager locationManager;
    private IMapController mapController;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private Marker currentMarker = null;
    private double chosenLongitude;
    private double chosenLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_specify_details);
        getSupportActionBar().hide();

        EditText text = findViewById(R.id.details_text);
        TextView title = findViewById(R.id.details_title);


        ImageButton back = findViewById(R.id.details_backButton);
        back.setOnClickListener(view -> {
            finish();
        });

        String activity = getIntent().getStringExtra("activity");
        title.setText("New " + activity + " activity");

        getLocation();

        setupMap();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = database.getReference(User.class.getSimpleName()); //.child(mAuth.getCurrentUser().getUid())

        Query query = databaseReference.orderByChild("userId"); //mAuth.getCurrentUser().getUid()
        query.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);

                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

                try {
                    birthday = sdf.parse(user.getBirthday());
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                LocalDate localDate = LocalDate.now();
                ZoneId zoneId = ZoneId.systemDefault();
                ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
                Date utilDate = Date.from(zonedDateTime.toInstant());

                Calendar a = getCalendar(birthday);
                Calendar b = getCalendar(utilDate);
                diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
                if ((a.get(Calendar.MONTH) > b.get(Calendar.MONTH)) || (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DAY_OF_MONTH) > b.get(Calendar.DAY_OF_MONTH))) {
                    diff--;
                }

                String dateString = b.get(Calendar.DAY_OF_MONTH) + "-" + b.get(Calendar.MONTH) + "-" + b.get(Calendar.YEAR);
                newActivity = new Activity(activity, user.getName(), diff, FirebaseAuth.getInstance().getCurrentUser().getUid(), text.getText().toString(), user.getImage(), dateString, latitude, longitude, user.getSex(), "https://firebasestorage.googleapis.com/v0/b/activity-1f1ae.appspot.com/o/Activities%2Fjogging.webp?alt=media&token=789e584a-4f11-4897-bd5d-a59cacaac3d8", "running");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button button = findViewById(R.id.details_nextButton);
        button.setOnClickListener(view -> {

            if (currentMarker == null) {
                chosenLatitude = latitude;
                chosenLongitude = longitude;
            }
            getLocation();
            newActivity.setLatitude(chosenLatitude);
            newActivity.setLongitude(chosenLongitude);
            newActivity.setText(text.getText().toString());

            DAOActivity dao = new DAOActivity();

            dao.add(newActivity).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(), "Record is inserted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Searching.class);
                    intent.putExtra("activity", activity);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    startActivity(intent);
                }
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 5, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        updateLocation(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        permissionsToRequest.addAll(Arrays.asList(permissions).subList(0, grantResults.length));
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void updateLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapController.setCenter(startPoint);
    }


    private class OverlayClickListener extends Overlay {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
            if (currentMarker != null) {
                mapView.getOverlays().remove(currentMarker);
            }
            int x = (int) e.getX();
            int y = (int) e.getY();

            // Convert the screen coordinates to geographical coordinates
            IGeoPoint geoPoint = mapView.getProjection().fromPixels(x, y);
            chosenLatitude = geoPoint.getLatitude();
            chosenLongitude = geoPoint.getLongitude();
            // Create a marker at the tapped location
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(chosenLatitude, chosenLongitude));
            marker.setTitle("Selected Location");
            marker.setSnippet("This is where Activity happens!");
            currentMarker = marker;
            // Add the marker to the map
            mapView.getOverlayManager().add(marker);

            // Redraw the map to show the new marker
            mapView.invalidate();

            return true;
        }
    }

    private void setupMap() {
        map = findViewById(R.id.details_map);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(true);
        mapController = map.getController();
        mapController.setZoom(17.0f);
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissionsIfNecessary(permissions);
        MyLocationNewOverlay mylocation = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
        mylocation.enableMyLocation();
        map.getOverlays().add(mylocation);
        OverlayClickListener MarkerclickListener = new OverlayClickListener();
        map.getOverlays().add(MarkerclickListener);
    }
}