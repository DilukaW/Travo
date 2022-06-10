package com.example.travo

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {
    private lateinit var edtEmail:TextInputEditText
    private lateinit var edtUname:TextInputEditText
    private lateinit var edtPwd:TextInputEditText
    private lateinit var edtCpwd:TextInputEditText
    private lateinit var btnSignUp:Button
    private lateinit var btnSignIn:Button
    private lateinit var txtError: TextView
    private lateinit var firebaseAuth:FirebaseAuth

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)


        txtError = findViewById(R.id.txtError)
        btnSignIn = findViewById(R.id.btnSignin)
        btnSignUp = findViewById(R.id.btnSignup)
        edtUname = findViewById(R.id.edtUname)
        edtEmail = findViewById(R.id.edtEmail)
        edtPwd = findViewById(R.id.edtPwd)
        edtCpwd = findViewById(R.id.edtCPwd)


        firebaseAuth= FirebaseAuth.getInstance()

    }


    @SuppressLint("SetTextI18n")
    private fun registerUser() {


        val email=edtEmail.text.toString().trim()
        val pwd=edtPwd.text.toString().trim()
        val cPwd=edtCpwd.text.toString().trim()
        val uName=edtUname.text.toString().trim()

        if(uName.isEmpty()){
            edtUname.error="User Name Required"
            edtUname.requestFocus()
            return
        }
        if(email.isEmpty()){
            edtEmail.error="Email Required"
            edtEmail.requestFocus()
            return
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.error="Enter Valid Email"
            edtEmail.requestFocus()
            return
        }
        if(pwd.isEmpty()){
            txtError.text="Password Required"
            edtPwd.requestFocus()
            return
        }
        if(cPwd.isEmpty()){
            txtError.text="Confirm Password Required"
            edtCpwd.requestFocus()
            return
        }
        if(pwd.length<6){
            txtError.text="Password must have at least 6 characters"
            edtPwd.requestFocus()
            return
        }
        if(cPwd.isEmpty()){
            edtCpwd.error="Email Required"
            edtCpwd.requestFocus()
            return
        }


    }

    fun login(view: View) {
        startActivity(Intent(this@SignUp,Login::class.java))
        finish()
    }

    fun signUp(view: View) {

        registerUser()
        val email=edtEmail.text.toString().trim()
        val pwd=edtPwd.text.toString().trim()
        val cPwd=edtCpwd.text.toString().trim()
        val uName=edtUname.text.toString().trim()

        try {
            if (email.isNotEmpty() && pwd.isNotEmpty() && cPwd.isNotEmpty() && uName.isNotEmpty()){
                if (pwd == cPwd){

                    firebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener{
                        if(it.isSuccessful){

                            val users=Users(uName,email,"100","")

                            db = FirebaseFirestore.getInstance()
                            FirebaseAuth.getInstance().currentUser?.uid?.let { it1 ->
                                db.collection("Users").document(it1).set(users).addOnSuccessListener { documentReference ->
                                    Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
                                }
                                    .addOnFailureListener { e ->
                                        Log.w(TAG, "Error adding document", e)
                                    }
                            }

                            startActivity(Intent(this@SignUp,Login::class.java))
                            finish()
                            Toast.makeText(this,"Registration Successful",Toast.LENGTH_SHORT).show()

                        }else{
                            //txtError.text="Something went wrong"
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        }catch (ex:Exception){

        }


    }
}