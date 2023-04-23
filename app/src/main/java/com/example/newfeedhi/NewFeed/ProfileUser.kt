package com.example.newfeedhi.NewFeed

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newfeedhi.Model.newfeed
import com.example.newfeedhi.Model.user
import com.example.newfeedhi.R
import com.example.newfeedhi.databinding.ActivityProfileUserBinding
import com.example.newfeedhi.databinding.ActivityStartedBinding
import com.example.newfeedhi.rcvAdapter.homeAdapter
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileUser : AppCompatActivity() {
    private lateinit var binding: ActivityProfileUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var imgAvt :ImageView
    private lateinit var nameUserTv:TextView
    private lateinit var btnback: ImageButton
    private lateinit var uid: String
    private  lateinit var nf_arraylist: ArrayList<newfeed>
    private lateinit var pickImgBtn: ImageButton
    private val PICK_IMAGE_REQUEST: Int = 1
    private lateinit var progressBar: ProgressBar
    private lateinit var imgUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileUserBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        val intent = intent
        uid = intent.getStringExtra("uid")!!
        imgAvt = binding.imgAvt
        nameUserTv = binding.nameUser
        pickImgBtn = binding.pickBtn
        progressBar = binding.progressBar
        btnback = binding.backBtn
        if(uid == firebaseAuth.uid){
            binding.btnFollow.visibility = View.GONE
            binding.btnAddFriend.visibility = View.GONE
        }
        pickImgBtn.setOnClickListener {
            openFileChooser()
        }
        btnback.setOnClickListener {
            startActivity(Intent(this, StartedActivity::class.java))
        }
        getDataUser(uid)
        loadStatus(binding.rcvStatus1,uid)
        setContentView(binding.root)
    }
    private fun getDataUser(uid:String){
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val model = ds.getValue(user::class.java)
                    if (model != null ) {
                        if(model.uid.equals(uid)){
                           nameUserTv.text=model.name
                            Log.i("adapterAvt",model.imgAvt)
                            Glide.with(imgAvt.context)
                                .load(model.imgAvt)
                                .centerCrop()
                                .apply(
                                    RequestOptions()
                                        .placeholder(R.drawable.loading_animation)
                                        .error(R.drawable.ic_broken_image))
                                .into(imgAvt)
                        }

                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun loadStatus(container: ViewGroup,uid: String){
        nf_arraylist=ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("NewFeeds")
        ref.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                nf_arraylist.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(newfeed::class.java)
                    if (model != null) {
                        Log.i("profile",model.uid)
                        Log.i("profileCheck","$uid")
                        if(model.uid==uid){
                                nf_arraylist.add(model!!)
                        }
                    }
                }
                val adapterHome = homeAdapter(container.context, nf_arraylist)
                binding.rcvStatus1.layoutManager = GridLayoutManager(container.context, 1)
                binding.rcvStatus1.adapter = adapterHome
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun openFileChooser() {
        progressBar.visibility = View.VISIBLE
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
            .addOnSuccessListener { taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadPdfUrl = "${uriTask.result}"
                val ref = FirebaseDatabase.getInstance().getReference("Users/$uid")
                ref.child("profileImage").setValue(uploadPdfUrl)
                Toast.makeText(this, "Changed avatar", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE
                imgAvt.setImageURI(imgUri)
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "failed upload pdf due to ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    }