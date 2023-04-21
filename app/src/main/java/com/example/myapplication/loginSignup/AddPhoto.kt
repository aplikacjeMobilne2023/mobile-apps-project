package com.example.myapplication.loginSignup

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.main.Home
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage

class AddPhoto : AppCompatActivity() {
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)
        supportActionBar!!.hide()
        findViewById<Button>(R.id.save_button).setOnClickListener { uploadProfileImage() }
        findViewById<Button>(R.id.profile_image).setOnClickListener {
            CropImage.activity().setAspectRatio(1, 1).start(this@AddPhoto)
        }
        userInfo
    }

    private val userInfo: Unit
        get() {
            FirebaseDatabase.getInstance().reference.child("User")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists() && snapshot.childrenCount > 0) {
                            if (snapshot.hasChild("image")) {
                                val image = snapshot.child("image").value.toString()
                                Picasso.get().load(image)
                                    .into(findViewById<ImageView>(R.id.profile_image))
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            findViewById<ImageView>(R.id.profile_image).setImageURI(imageUri)
        } else {
            Toast.makeText(this, "Error, Try Again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadProfileImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Saving picture")
        progressDialog.setMessage("Please wait while we are setting your data")
        progressDialog.show()

        if (imageUri != null) {
            val fileRef = FirebaseStorage.getInstance().reference.child("Profile Pic")
                .child(FirebaseAuth.getInstance().currentUser!!.uid + ".jpg")
            fileRef.putFile(imageUri!!).addOnCompleteListener { task ->
                FirebaseDatabase.getInstance().reference.child("User").child("Image")
                    .setValue(task.result.toString())
                Toast.makeText(this@AddPhoto, task.result.toString(), Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                openNext()
            }

            /*uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUri = downloadUrl.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image", myUri);

                        databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);

                        progressDialog.dismiss();

                        openNext();
                    }
                }
            });*/
        } else {
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openNext() {
        val intent = Intent(applicationContext, Home::class.java)
        startActivity(intent)
    }
}