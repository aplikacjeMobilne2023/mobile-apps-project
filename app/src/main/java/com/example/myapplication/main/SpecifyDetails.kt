package com.example.myapplication.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.database.Activity
import com.example.myapplication.database.DAOActivity
import com.example.myapplication.database.User
import com.example.myapplication.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

// uzytkownik moze miec wylaczona lokalizacje
class SpecifyDetails : AppCompatActivity(), LocationListener {
    private var birthday: Date? = null
    private var diff = 0
    private var newActivity: Activity? = null
    var latitude = 0.0
    var longitude = 0.0
    private var locationManager: LocationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specify_details)
        supportActionBar!!.hide()
        val text = findViewById<EditText>(R.id.details_text)
        val back = findViewById<ImageButton>(R.id.details_backButton)
        back.setOnClickListener {
            val intent = Intent(applicationContext, Home::class.java)
            startActivity(intent)
        }
        val activity = intent.getStringExtra("activity")
        location
        if (ContextCompat.checkSelfPermission(
                this@SpecifyDetails,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@SpecifyDetails, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 100
            )
        }
        val database =
            FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app")
        val databaseReference =
            database.getReference(User::class.java.simpleName) //.child(mAuth.getCurrentUser().getUid())
        val query = databaseReference.orderByChild("userId")
            .equalTo("v4HL4Gnm7pXX4GfK8m5F1sjTHgU2") //mAuth.getCurrentUser().getUid()
        query.addChildEventListener(object : ChildEventListener {
            @RequiresApi(api = Build.VERSION_CODES.O)
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(
                    User::class.java
                )
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                try {
                    birthday = sdf.parse(user!!.birthday!!)
                } catch (e: ParseException) {
                    Toast.makeText(applicationContext, "" + e.message, Toast.LENGTH_SHORT).show()
                }
                val localDate = LocalDate.now()
                val zoneId = ZoneId.systemDefault()
                val zonedDateTime = localDate.atStartOfDay(zoneId)
                val utilDate = Date.from(zonedDateTime.toInstant())
                val a = getCalendar(birthday)
                val b = getCalendar(utilDate)
                diff = b[Calendar.YEAR] - a[Calendar.YEAR]
                if (a[Calendar.MONTH] > b[Calendar.MONTH] || a[Calendar.MONTH] == b[Calendar.MONTH] && a[Calendar.DAY_OF_MONTH] > b[Calendar.DAY_OF_MONTH]) {
                    diff--
                }
                val dateString =
                    b[Calendar.DAY_OF_MONTH].toString() + "-" + b[Calendar.MONTH] + "-" + b[Calendar.YEAR]
                newActivity = Activity(
                    activity,
                    user!!.name,
                    diff,
                    "v4HL4Gnm7pXX4GfK8m5F1sjTHgU2",
                    text.text.toString(),
                    user.image,
                    dateString,
                    latitude,
                    longitude,
                    user.sex
                )
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
        val button = findViewById<Button>(R.id.details_nextButton)
        button.setOnClickListener {
            location
            newActivity!!.latitude = latitude
            newActivity!!.longitude = longitude
            newActivity!!.text = text.text.toString()
            val dao = DAOActivity()
            dao.add(newActivity).addOnSuccessListener {
                Toast.makeText(applicationContext, "Record is inserted", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(applicationContext, Searching::class.java)
                intent.putExtra("activity", activity)
                intent.putExtra("latitude", latitude)
                intent.putExtra("longitude", longitude)
                startActivity(intent)
            }
        }
    }

    @get:SuppressLint("MissingPermission")
    private val location: Unit
        get() {
            try {
                locationManager =
                    applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    5f,
                    this
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
    }

    companion object {
        fun getCalendar(date: Date?): Calendar {
            val cal = Calendar.getInstance(Locale.US)
            cal.time = date!!
            return cal
        }
    }
}