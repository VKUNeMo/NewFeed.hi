package com.example.newfeedhi.rcvAdapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newfeedhi.Model.newfeed
import com.example.newfeedhi.Model.user
import com.example.newfeedhi.NewFeed.ProfileUser
import com.example.newfeedhi.R
import com.example.newfeedhi.databinding.RcvItemFriendrequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class friendAdapter : RecyclerView.Adapter<friendAdapter.HolderFriendRequest>{
    private lateinit var binding: RcvItemFriendrequestBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var context: Context
    private var friendrequestList: ArrayList<user>
    var uid :String=""

    constructor(context: Context, friendrequestList: ArrayList<user>) : super() {
        this.context = context
        this.friendrequestList = friendrequestList
    }

    inner class HolderFriendRequest(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameUser: TextView = binding.nameUser
        val imgView: ImageView = binding.imgAvt
        val btnConfirm : Button = binding.btnConfirm
        val btnDelete :Button = binding.btnDelete

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderFriendRequest {
        binding =RcvItemFriendrequestBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderFriendRequest(binding.root)
    }

    override fun onBindViewHolder(holder: HolderFriendRequest, position: Int) {
        val model = friendrequestList[position]
        val name = model.name
        val img = model.imgAvt
        firebaseAuth = FirebaseAuth.getInstance()
        uid = model.uid
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val model = ds.getValue(user::class.java)
                    if (model != null ) {
                             holder.nameUser.text = name
                            Glide.with(holder.imgView.context)
                                .load(model.imgAvt)
                                .centerCrop()
                                .apply(
                                    RequestOptions()
                                        .placeholder(R.drawable.loading_animation)
                                        .error(R.drawable.ic_broken_image))
                                .into(holder.imgView)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        holder.nameUser.setOnClickListener {
            toProfile(uid)
        }
        holder.btnConfirm.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("Users/$uid")
            ref.child("listFriends").push().setValue(firebaseAuth.uid)
            val refRemove = FirebaseDatabase.getInstance().getReference("Users/$uid/listRequestFriends")
            refRemove.orderByValue().equalTo("$uid").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        ds.ref.removeValue()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
            val ref1 = FirebaseDatabase.getInstance().getReference("Users/${firebaseAuth.uid}")
            ref1.child("listFriends").push().setValue(uid)
        }
        holder.btnDelete.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("Users/$uid")
            val refRemove = FirebaseDatabase.getInstance().getReference("Users/${firebaseAuth.uid}/listRequestFriends")
            refRemove.orderByValue().equalTo("$uid").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        ds.ref.removeValue()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }
    private fun toProfile(uid:String){
        val intent = Intent(context, ProfileUser::class.java)
        intent.putExtra("uid", uid)
        context.startActivity(intent)
    }
    override fun getItemCount(): Int {
        return friendrequestList.size
    }

}