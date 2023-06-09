package com.example.newfeedhi.NewFeed

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.newfeedhi.Model.newfeed
import com.example.newfeedhi.Model.user
import com.example.newfeedhi.databinding.FragmentHomeBinding
import com.example.newfeedhi.rcvAdapter.homeAdapter
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
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var firebaseAuth: FirebaseAuth
    val TAG = "HOME"
    private lateinit var binding: FragmentHomeBinding
    private lateinit var addStatusTv :TextView
    private  lateinit var nf_arraylist: ArrayList<newfeed>
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
        binding = FragmentHomeBinding.inflate(inflater,container,  false)
        firebaseAuth = FirebaseAuth.getInstance()
        loadStatus(container!!)
        addStatusTv = binding.addStatus
        addStatusTv.setOnClickListener {
            startActivity(Intent(activity,AddStatusActivity::class.java))
            Toast.makeText(activity, "Text view clicked!", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    private fun loadStatus(container: ViewGroup){
        nf_arraylist=ArrayList()
        val uid = firebaseAuth.uid
        val ref = FirebaseDatabase.getInstance().getReference("NewFeeds")
        ref.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                nf_arraylist.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(newfeed::class.java)
                       nf_arraylist.add(model!!)
                }
                val adapterHome = homeAdapter(container.context, nf_arraylist)
                binding.rcvStatus.layoutManager = GridLayoutManager(container.context, 1)
                binding.rcvStatus.adapter = adapterHome
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}