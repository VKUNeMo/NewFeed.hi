package com.example.newfeedhi

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import com.example.newfeedhi.NewFeed.StartedActivity
import com.example.newfeedhi.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    val TAG = "LOGIN"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(true)

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, StartedActivity::class.java))
        }
        binding.notHaveTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))

        }
        binding.loginBtn.setOnClickListener {
            validateData()
        }
    }
    private var email = ""
    private var password = ""
    private fun validateData(){
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
        }else if (password.isEmpty()){
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
        }else{
            loginUser()
        }
    }
    private fun loginUser(){
        progressDialog.setMessage("Logging in")
        progressDialog.show()
        Log.i(TAG,"loginuser")
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.i(TAG,"dang nhap thanh cong")
                startActivity(Intent(this@LoginActivity, StartedActivity::class.java))
            }
            .addOnFailureListener {
                Log.i(TAG,"dang ki that bai ${it.message}")
                Toast.makeText(this, "Login failure due to ${it.message}",Toast.LENGTH_SHORT)
            }
    }
}