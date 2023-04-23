package com.example.newfeedhi.rcvAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newfeedhi.Model.newfeed
import com.example.newfeedhi.Model.user
import com.example.newfeedhi.databinding.RcvItemFriendrequestBinding

class friendAdapter : RecyclerView.Adapter<friendAdapter.HolderFriendRequest>{
    private lateinit var binding: RcvItemFriendrequestBinding
    private var context: Context
    private var friendrequestList: ArrayList<user>

    constructor(context: Context, friendrequestList: ArrayList<user>) : super() {
        this.context = context
        this.friendrequestList = friendrequestList
    }

    inner class HolderFriendRequest(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderFriendRequest {
        binding =RcvItemFriendrequestBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderFriendRequest(binding.root)
    }

    override fun onBindViewHolder(holder: HolderFriendRequest, position: Int) {
        val model = friendrequestList[position]
    }

    override fun getItemCount(): Int {
        return friendrequestList.size
    }

}