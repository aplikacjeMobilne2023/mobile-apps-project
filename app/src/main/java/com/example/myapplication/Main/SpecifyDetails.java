package com.example.myapplication.Main; // uzytkownik moze miec wylaczona lokalizacje

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SpecifyDetails extends AppCompatActivity implements LocationListener{

    private Date birthday;
    private int diff;
    private Activity newActivity;
    double latitude;
    double longitude;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specify_details);
        getSupportActionBar().hide();

        EditText text = findViewById(R.id.details_text);

        ImageButton back = findViewById(R.id.details_backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        String activity = getIntent().getStringExtra("activity");
        getLocation();

        if(ContextCompat.checkSelfPermission(SpecifyDetails.this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SpecifyDetails.this,new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = database.getReference(User.class.getSimpleName()); //.child(mAuth.getCurrentUser().getUid())

        Query query = databaseReference.orderByChild("userId").equalTo("v4HL4Gnm7pXX4GfK8m5F1sjTHgU2"); //mAuth.getCurrentUser().getUid()
        query.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                try {
                    birthday = sdf.parse(user.getBirthday());
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                LocalDate localDate = LocalDate.now();
                ZoneId zoneId = ZoneId.systemDefault();
                ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
                Date utilDate = Date.from(zonedDateTime.toInstant());

                Calendar a = getCalendar(birthday);
                Calendar b = getCalendar(utilDate);
                diff = b.get(Calendar.YEAR) -a.get(Calendar.YEAR);
                if ((a.get(Calendar.MONTH) > b.get(Calendar.MONTH)) || (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DAY_OF_MONTH) > b.get(Calendar.DAY_OF_MONTH))) {
                    diff--;
                }

                String dateString  = b.get(Calendar.DAY_OF_MONTH) +"-"+b.get(Calendar.MONTH)+"-"+b.get(Calendar.YEAR);
                newActivity = new Activity(activity,user.getName(),diff,"v4HL4Gnm7pXX4GfK8m5F1sjTHgU2",text.getText().toString(),user.getImage(),dateString, latitude,longitude,user.getSex());
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                newActivity.setLatitude(latitude);
                newActivity.setLongitude(longitude);
                newActivity.setText(text.getText().toString());

                DAOActivity dao = new DAOActivity();

                dao.add(newActivity).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Record is inserted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Searching.class);
                        intent.putExtra("activity", activity);
                        intent.putExtra("latitude",latitude);
                        intent.putExtra("longitude",longitude);
                        startActivity(intent);
                    }
                });
            }
        });
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}