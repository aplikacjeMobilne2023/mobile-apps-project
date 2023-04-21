package com.example.myapplication.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.database.Activity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class Searching : AppCompatActivity() {
    val list: MutableList<Activity> = ArrayList()
    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var firebaseStorage: FirebaseStorage? = null
    var myAdapter: RecyclerViewAdapter? = null
    var startDateFilterDate: Date? = null
    var endDateFilterDate: Date? = null
    var sdf: SimpleDateFormat? = null
    var date: Date? = null
    private var mLocationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searching)
        supportActionBar!!.hide()
        val mAuth = FirebaseAuth.getInstance()
        firebaseDatabase =
            FirebaseDatabase.getInstance("https://activity-1f1ae-default-rtdb.europe-west1.firebasedatabase.app")
        databaseReference = firebaseDatabase!!.getReference(Activity::class.java.simpleName)
        firebaseStorage = FirebaseStorage.getInstance()
        //String activityName = getIntent().getStringExtra("activity");
        val activityName = "jogging"
        val sortBy = "distance"
        val ageMinFilter = 8
        val ageMaxFilter = 30
        val distanceFilter = 1000000
        sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val startDateFilter = "23-09-2021"
        val endDateFilter = "30-09-2021"
        val sexFilter = "Male"
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val myLocation = Location("")
        myLocation.latitude = latitude
        myLocation.longitude = longitude

        startDateFilterDate = sdf!!.parse(startDateFilter)
        endDateFilterDate = sdf!!.parse(endDateFilter)

        val query = databaseReference!!.orderByChild("activity").equalTo(activityName)
        query.addChildEventListener(object : ChildEventListener {
            @RequiresApi(api = Build.VERSION_CODES.O)
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val activity = dataSnapshot.getValue(
                    Activity::class.java
                )
                try {
                    date = sdf!!.parse(activity!!.date!!)
                } catch (e: Exception) {
                    activity!!.name = e.toString()
                }

                // here check if current outdated
                val distance = distance(
                    myLocation.latitude,
                    myLocation.longitude,
                    activity!!.latitude,
                    activity.longitude
                )
                if (activity.activity == activityName &&
                    ageMinFilter < activity.age &&
                    activity.age < ageMaxFilter &&
                    (startDateFilterDate!!.before(date) || startDateFilterDate == date) &&
                    (endDateFilterDate!!.after(date) || endDateFilterDate == date) &&
                    (sexFilter == activity.sex || activity.sex == null) &&
                    distance < distanceFilter &&
                    activity.userId !== mAuth.currentUser!!.uid
                ) {
                    val localDate = LocalDate.now()
                    val zoneId = ZoneId.systemDefault()
                    val zonedDateTime = localDate.atStartOfDay(zoneId)
                    val utilDate = Date.from(zonedDateTime.toInstant())
                    val a = getCalendar(date)
                    val b = getCalendar(utilDate)
                    val daysBetween = ChronoUnit.DAYS.between(a.toInstant(), b.toInstant())
                    activity.daysTo = daysBetween
                    activity.distance = (distance / 100).roundToInt() / 10.0 //Math.round(distance/100)/10
                    list.add(activity)
                }
                if (sortBy === "date") {
                    list.sortWith{ a1, a2 ->
                        val d1 = sdf!!.parse(a1.date!!)
                        val d2 = sdf!!.parse(a2.date!!)

                        d1!!.compareTo(d2)
                    }
                } else if (sortBy === "distance") {
                    list.sortWith{ a1, a2 ->
                        if (distance(
                                myLocation.latitude,
                                myLocation.longitude,
                                a1.latitude,
                                a1.longitude
                            )
                            > distance(
                                myLocation.latitude,
                                myLocation.longitude,
                                a2.latitude,
                                a2.longitude
                            )
                        ) {
                            1
                        } else -1
                    }
                }
                myAdapter!!.notifyDataSetChanged()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        val myRv = findViewById<View>(R.id.recyclerview_id) as RecyclerView
        myAdapter = RecyclerViewAdapter(this, list)
        myRv.layoutManager = GridLayoutManager(this, 2)
        myRv.adapter = myAdapter
    }

    // Found best last known location: %s", l);
    private val lastKnownLocation: Location?
        get() {
            mLocationManager =
                applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
            val providers = mLocationManager!!.getProviders(true)
            var bestLocation: Location? = null
            for (provider in providers) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val l = mLocationManager!!.getLastKnownLocation(provider) ?: continue

                    if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                        // Found best last known location: %s", l);
                        bestLocation = l
                    }
                }
            }
            return bestLocation
        }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist =
            sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(
                deg2rad(lat2)
            ) * cos(deg2rad(theta))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515 * 1.609344
        return dist
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/ /*::  This function converts decimal degrees to radians             :*/ /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/ /*::  This function converts radians to decimal degrees             :*/ /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    companion object {
        fun getCalendar(date: Date?): Calendar {
            val cal = Calendar.getInstance(Locale.US)
            cal.time = date!!
            return cal
        }
    }
}