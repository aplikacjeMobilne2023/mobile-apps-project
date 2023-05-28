package com.example.myapplication.Main

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Database.Activity
import com.example.myapplication.Database.DAOActivitySubcategory
import com.example.myapplication.R
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class UserActivityView(val view: View): RecyclerView.ViewHolder(view) {
    fun bindUserActivity(activity: Activity) {
        view.findViewById<TextView>(R.id.name_searching).text = activity.text
        view.findViewById<TextView>(R.id.age).text = activity.age.toString()
        val imageView = view.findViewById<ImageView>(R.id.profile_picture_searching)
        val subcategoryToImageMap:Map<String, String> = mapOf(
            Pair("Active", "https://firebasestorage.googleapis.com/v0/b/activity-1f1ae.appspot.com/o/Activity%20Categories%2FDating%2Fyoga-3427178_1280.webp?alt=media&token=e03d92df-2134-45d0-8abc-cf6d89c3fa96"),
            Pair("Culture", "https://firebasestorage.googleapis.com/v0/b/activity-1f1ae.appspot.com/o/Activity%20Categories%2FDating%2Fcultural.png?alt=media&token=6349ac7f-3c7c-4c45-8af0-5a63201dc413"),
            Pair("Partying", "https://firebasestorage.googleapis.com/v0/b/activity-1f1ae.appspot.com/o/Activity%20Categories%2FDating%2Fdrinking.png?alt=media&token=f753a3c5-56d7-44ff-9f95-21ce4f28619f"),
            Pair("Sit & Talk", "https://firebasestorage.googleapis.com/v0/b/activity-1f1ae.appspot.com/o/Activity%20Categories%2FDating%2Fsilhouette-3696049_1280.webp?alt=media&token=e9640a26-6b18-4e32-85ca-8627f86fec80"),
            Pair("Surprise me!", "https://firebasestorage.googleapis.com/v0/b/activity-1f1ae.appspot.com/o/Activity%20Categories%2FDating%2FBlack_question_mark.png?alt=media&token=957531d0-be7a-48d2-bd25-a99921941e6f"),
            Pair("Competitive Drinking", "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn.pixabay.com%2Fphoto%2F2014%2F03%2F25%2F16%2F35%2Fhypnotic-297485_960_720.png&f=1&nofb=1&ipt=430c48288383898b2f69d4e50342c0692dba1a7de00c4cf3a3d271d872379e5a&ipo=images"),
            Pair("Competitive Eating", "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.publicdomainpictures.net%2Fpictures%2F120000%2Fnahled%2Fgold-cup-silhouette.jpg&f=1&nofb=1&ipt=fdad7a51b0b8dda73faacd3aa769a20d18ce2d4af424f5d5d9f78094eeb0c8cb&ipo=images"),
            Pair("Shared Drink", "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fas1.ftcdn.net%2Fjpg%2F01%2F81%2F79%2F82%2F220_F_181798257_HoEFPZvWSHPce1WUzzAC9Tx09X1n4e1B.jpg&f=1&nofb=1&ipt=3d1a1555ea26060b297886fd41b44b7e2741dca65387e35d0dd7c5a214d82b9f&ipo=images"),
            Pair("Shared Meal", "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Flilliantoomandalaezine.com%2FEzineImages%2Fdiningroomtable.jpg&f=1&nofb=1&ipt=9d3e5b558dbc8dd7d332e25bdbbac144ebbbe7b5dd9c16482c221755bed06ca7&ipo=images"),
            Pair("cDrinking", "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fcdn.pixabay.com%2Fphoto%2F2014%2F03%2F25%2F16%2F35%2Fhypnotic-297485_960_720.png&f=1&nofb=1&ipt=430c48288383898b2f69d4e50342c0692dba1a7de00c4cf3a3d271d872379e5a&ipo=images"),
            Pair("cEating", "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.publicdomainpictures.net%2Fpictures%2F120000%2Fnahled%2Fgold-cup-silhouette.jpg&f=1&nofb=1&ipt=fdad7a51b0b8dda73faacd3aa769a20d18ce2d4af424f5d5d9f78094eeb0c8cb&ipo=images"),
            Pair("football", "https://firebasestorage.googleapis.com/v0/b/activity-1f1ae.appspot.com/o/Activity%20Categories%2Ffootball.png?alt=media&token=3d2bb994-37cd-4f2a-812e-04863c92040e"),
            Pair("running", "https://cdn.pixabay.com/photo/2014/04/03/10/50/run-311447_1280.png"),
        )
        val subCat = activity.subcategory
        val ref = subcategoryToImageMap[subCat]
        Picasso.get().load(ref).into(imageView)

        view.setOnClickListener() { v ->
            val intent =  Intent(view.context,Activity_Details::class.java)
            intent.putExtra("text", activity.getText())
            intent.putExtra("name", activity.getName())
            intent.putExtra("age", activity.getAge())
            intent.putExtra("activity", activity.getActivity())
            intent.putExtra("image", activity.getThumbnail())
            intent.putExtra("distance", activity.getDistance())
            intent.putExtra("daysTo", activity.getDaysTo())
            // start the activity
            // start the activity
            startActivity(view.context,intent,null)
        }
    }
}