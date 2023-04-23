package com.example.newfeedhi.NewFeed

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.newfeedhi.databinding.ActivityAddStatusBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddStatusActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST: Int = 1
    private lateinit var imgUri: Uri
    private lateinit var captionEt :EditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding : ActivityAddStatusBinding
    private lateinit var imv : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStatusBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, StartedActivity::class.java))
        }
        captionEt = binding.caption

        binding.photo.setOnClickListener {
            pickImg()
        }
        binding.btnUp.setOnClickListener {
            validateData()
        }
        setContentView(binding.root)
    }
    var caption:String=""
    private fun validateData() {
        caption = captionEt.text.toString()
        if (caption.isEmpty()){
            Toast.makeText(this, "Enter your caption", Toast.LENGTH_SHORT).show()
        }else{
            Log.i("add","validate oke")
            upStatus()
        }
    }
    private fun upStatus(){
        Log.i("add","start Add")
        val uid = firebaseAuth.uid!!
        val timestamp = System.currentTimeMillis()
        val hashMap: HashMap<String, Any?> =  HashMap()
        hashMap["id"]="$timestamp"
        hashMap["uid"] = uid
        hashMap["image"] = ""
        hashMap["timestamp"] = timestamp
        hashMap["numberOfLike"] = 0
        hashMap["caption"]=caption
        val ref = FirebaseDatabase.getInstance().getReference("NewFeeds")
        ref.child(timestamp.toString()).setValue(hashMap)
            .addOnSuccessListener {
                Log.i("add","success")
                Toast.makeText(this, "Updated!!!", Toast.LENGTH_SHORT)
            }
    }
    private fun pickImg(){
        var intent = Intent()
        intent.type= "image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null){
            imgUri = data.data!!
            uploadImgToStorage()
        }
    }
    private fun uploadImgToStorage() {
        val uid = firebaseAuth.uid
        val filePathAndName = "Avatars/$uid"
        val storageRef = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageRef.putFile(imgUri!!)
            .addOnSuccessListener {taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadPdfUrl = "${uriTask.result}"
                val ref = FirebaseDatabase.getInstance().getReference("NewFeeds/$uid")
                ref.child("imageUrl").setValue(uploadPdfUrl)
                Toast.makeText(this, "Changed avatar", Toast.LENGTH_SHORT).show()
                imv.setImageURI(imgUri)
            }
            .addOnFailureListener{
                Toast.makeText(this, "failed upload pdf due to ", Toast.LENGTH_SHORT).show()
            }

    }

}