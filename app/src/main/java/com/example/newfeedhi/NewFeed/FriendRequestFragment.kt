package com.example.newfeedhi.NewFeed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.newfeedhi.Model.user
import com.example.newfeedhi.databinding.FragmentFriendRequestBinding
import com.example.newfeedhi.rcvAdapter.friendAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FriendRequestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendRequestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentFriendRequestBinding
    private lateinit var friendRequestList: ArrayList<user>
    private lateinit var uid: String
    private lateinit var adapterFriend: friendAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendRequestBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        uid = firebaseAuth.uid!!
        Log.i("friendRequest", "$uid")
        loadFriendRequest(container!!)
        return binding.root
    }

    fun loadFriendRequest(container: ViewGroup) {
        friendRequestList = ArrayList()
        Log.i("FriendRq", "test")
        val ref = FirebaseDatabase.getInstance().getReference("Users/$uid/listRequestFriends")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val model = ds.getValue().toString()
                    Log.i("F", model)
                    getDataUser(model) { userModel ->
                        if (userModel.uid != "") {
                            friendRequestList.add(userModel)
                            Log.i("Fr", userModel.name)
                            adapterFriend.notifyDataSetChanged()
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        adapterFriend = friendAdapter(container.context, friendRequestList)
        binding.rcvFriend.layoutManager = GridLayoutManager(container.context, 1)
        binding.rcvFriend.adapter = adapterFriend
    }

    fun getDataUser(uid: String, callback: (user) -> Unit) {
        val ref = FirebaseDatabase.getInstance().getReference("Users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userModel = snapshot.getValue(user::class.java)
                if (userModel != null) {
                    Log.i("getDataInFr", userModel.uid)
                    callback(userModel)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FriendRequestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FriendRequestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}