package com.example.newfeedhi.NewFeed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newfeedhi.Model.newfeed
import com.example.newfeedhi.Model.user
import com.example.newfeedhi.R
import com.example.newfeedhi.databinding.ActivityDetailStatusBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DetailStatus : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStatusBinding
    private lateinit var idPost: String
    private lateinit var uid: String
    private lateinit var caption: TextView
    private lateinit var nameUser: TextView
    private lateinit var imgView: ImageView
    private lateinit var imgAvt: ImageView
    private lateinit var timeTv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStatusBinding.inflate(layoutInflater)
        val intent = intent
        idPost = intent.getStringExtra("id")!!
        uid = intent.getStringExtra("uid")!!
        caption = binding.caption
        nameUser = binding.nameUser
        imgView = binding.img
        imgAvt = binding.imageView
        timeTv = binding.time
        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, StartedActivity::class.java))
        }

        initUI()
        setContentView(binding.root)
    }

    fun initUI() {
        val ref1 = FirebaseDatabase.getInstance().getReference("NewFeeds")
        ref1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val model = ds.getValue(newfeed::class.java)
                    if (model != null) {
                        if (model.id == idPost) {
                            binding.btnLike.setOnClickListener {
                                val ref = FirebaseDatabase.getInstance().getReference("NewFeeds/$idPost")
                                ref.child("numberOfLike").setValue(model.numberOfLike + 1)
                            }
                            binding.btnLike.text = model.numberOfLike.toString()
                            timeTv.text = timestampToDate(model.timestamp)
                            caption.text = model.caption
                            if (model.image != "") {
                                Glide.with(imgView.context)
                                    .load(model.image)
                                    .centerCrop()
                                    .override(500, 500)
                                    .apply(
                                        RequestOptions()
                                            .placeholder(R.drawable.loading_animation)
                                            .error(R.drawable.ic_broken_image)
                                    )
                                    .into(imgView)
                            }
                            val ref = FirebaseDatabase.getInstance().getReference("Users")
                            ref.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (ds in snapshot.children) {
                                        val model = ds.getValue(user::class.java)
                                        if (model != null) {
                                            if (model.uid == uid) {
                                                nameUser.text = model.name
                                                Log.i("adapterAvt", model.imgAvt)
                                                Glide.with(imgAvt.context)
                                                    .load(model.imgAvt)
                                                    .centerCrop()
                                                    .apply(
                                                        RequestOptions()
                                                            .placeholder(R.drawable.loading_animation)
                                                            .error(R.drawable.ic_broken_image)
                                                    )
                                                    .into(imgAvt)
                                            }
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun timestampToDate(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return date.format(formatter)
    }
}