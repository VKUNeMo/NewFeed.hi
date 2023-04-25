package com.example.newfeedhi.rcvAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newfeedhi.Model.newfeed
import com.example.newfeedhi.Model.user
import com.example.newfeedhi.NewFeed.DetailStatus
import com.example.newfeedhi.NewFeed.ProfileUser
import com.example.newfeedhi.R
import com.example.newfeedhi.databinding.RcvItemNewfeedBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class homeAdapter : RecyclerView.Adapter<homeAdapter.HolderHome> {
    private lateinit var binding: RcvItemNewfeedBinding
    private var context: Context
    private var newfeedArraylist: ArrayList<newfeed>
    var uid :String=""
    constructor(context: Context, newfeedArraylist: ArrayList<newfeed>) : super() {
        this.context = context
        this.newfeedArraylist = newfeedArraylist
    }

    inner class HolderHome(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val caption: TextView = binding.caption
        val nameUser: TextView = binding.nameUser
        val imgView: ImageView = binding.img
        val imgAvt:ImageView = binding.imageView
        val timeTv: TextView = binding.time
        val btnLike :Button = binding.btnLike
        val item =binding.iteamNewFeed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderHome {
        binding = RcvItemNewfeedBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderHome(binding.root)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: HolderHome, position: Int) {
        val model = newfeedArraylist[position]
        val caption = model.caption
        val img = model.image
        val time = model.timestamp
        uid = model.uid
        holder.btnLike.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("NewFeeds/${model.id}")
            ref.child("numberOfLike").setValue(model.numberOfLike+1)
        }
        holder.timeTv.text = timestampToDate(time)
        holder.caption.text = caption
        holder.btnLike.text = model.numberOfLike.toString()
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val model = ds.getValue(user::class.java)
                    if (model != null ) {
                        if(model.uid.equals(uid)){
                            holder.nameUser.text=model.name
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
                if(img != ""){
                    Glide.with(holder.imgView.context)
                        .load(model.image)
                        .centerCrop()
                        .override(500,500)
                        .apply(
                            RequestOptions()
                                .placeholder(R.drawable.loading_animation)
                                .error(R.drawable.ic_broken_image))
                        .into(holder.imgView)
                }

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        holder.nameUser.setOnClickListener {
            toProfile(uid)
        }
        holder.item.setOnClickListener {
            toDetail(model.id)
        }

    }
    private fun toProfile(uid:String){
        val intent = Intent(context, ProfileUser::class.java)
        intent.putExtra("uid", uid)
        context.startActivity(intent)
    }
    private fun toDetail(id:String){
        val intent = Intent(context, DetailStatus::class.java)
        intent.putExtra("id", id)
        intent.putExtra("uid", uid)
        context.startActivity(intent)
    }
    private fun timestampToDate(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return date.format(formatter)
    }
    override fun getItemCount(): Int {
        return newfeedArraylist.size
    }
}

