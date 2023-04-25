package com.example.newfeedhi.rcvAdapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newfeedhi.Model.newfeed
import com.example.newfeedhi.Model.notification
import com.example.newfeedhi.Model.user
import com.example.newfeedhi.NewFeed.ProfileUser
import com.example.newfeedhi.R
import com.example.newfeedhi.databinding.RcvItemNewfeedBinding
import com.example.newfeedhi.databinding.RcvItemNotificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class notiAdapter : RecyclerView.Adapter<notiAdapter.HolderNoti> {
    private lateinit var binding: RcvItemNotificationBinding
    private var context: Context
    private var notificationArraylist: ArrayList<notification>
    var uid :String=""
    private  lateinit var firebaseAuth: FirebaseAuth
    constructor(context: Context, notificationArrayList: ArrayList<notification>) : super() {
        this.context = context
        this.notificationArraylist = notificationArrayList
    }

    inner class HolderNoti(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content: TextView = binding.content
        val timeTv: TextView = binding.time
        val imgAvt :ImageView= binding.imgAvt
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderNoti {
        binding = RcvItemNotificationBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderNoti(binding.root)
    }
    override fun onBindViewHolder(holder: HolderNoti, position: Int) {
        val model = notificationArraylist[position]
        val content = model.content
        val time = model.timestamp
        firebaseAuth = FirebaseAuth.getInstance()
        uid = firebaseAuth.uid!!
        holder.content.text = content
        holder.timeTv.text = timestampToDate(time)
        holder.timeTv.text = timestampToDate(time)
        val ref = FirebaseDatabase.getInstance().getReference("Notifications")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val model = ds.getValue(user::class.java)
                    if (model != null ) {
                        if(model.uid.equals(uid)) {
                            Log.i("adapterAvt",model.imgAvt)
                            Glide.with(holder.imgAvt.context)
                                .load(model.imgAvt)
                                .centerCrop()
                                .apply(
                                    RequestOptions()
                                        .placeholder(R.drawable.loading_animation)
                                        .error(R.drawable.ic_broken_image))
                                .into(holder.imgAvt)
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
    override fun getItemCount(): Int {
        return notificationArraylist.size
    }
}

