package com.example.travo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPwd: TextInputEditText
    private lateinit var btnSignIn: Button
    private lateinit var btnFpwd: Button
    private lateinit var btnSignUp: Button
    private lateinit var txtError: TextView
    private var uEmail:String=""
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var db: FirebaseFirestore
   // private lateinit var binding: ActivityMainBinding

    private lateinit var sharedPreferences: SharedPreferences
    private var isRemember=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        /*binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)*/

        sharedPreferences=getSharedPreferences("SHARED_PREF",Context.MODE_PRIVATE)
        isRemember=sharedPreferences.getBoolean("CHECKBOX",false)

        if(isRemember){
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        firebaseAuth= FirebaseAuth.getInstance()

        txtError = findViewById(R.id.txtError)
        btnSignUp = findViewById(R.id.btnSignup)
        btnSignIn = findViewById(R.id.btnLogin)
        btnFpwd = findViewById(R.id.btnForgetPwd)
        edtEmail = findViewById(R.id.edtEmail)
        edtPwd = findViewById(R.id.edtPwd)

    }

    private fun validateTxt() {

        val email=edtEmail.text.toString().trim()
        val pwd=edtPwd.text.toString().trim()

        if (email.isEmpty()){
            edtEmail.error="Email Required"
            edtEmail.requestFocus()
            return
        }
        if (pwd.isEmpty()){
            edtPwd.error="Password Required"
            edtPwd.requestFocus()
            return
        }
    }

    fun signUp(view: View) {

        startActivity(Intent(this,SignUp::class.java))

    }

    @SuppressLint("SetTextI18n")
    fun login(view: View) {

        validateTxt()
        val email=edtEmail.text.toString().trim()
        val pwd=edtPwd.text.toString().trim()
        val checked =true


        try{
            if (email.isNotEmpty() && pwd.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener{
                    if(it.isSuccessful){

                        val  user=FirebaseAuth.getInstance().currentUser

                        val editor:SharedPreferences.Editor=sharedPreferences.edit()
                        editor.putString("EMAIL",email)
                        editor.putBoolean("CHECKBOX",checked)
                        editor.apply()

                        if(user!!.isEmailVerified){
                            val intent=Intent(this,MainActivity::class.java)
                            intent.putExtra("email",email)
                            startActivity(intent)

                            finish()
                        }
                        else{
                            user.sendEmailVerification()
                            Toast.makeText(this,"Check your email to verify your account",Toast.LENGTH_LONG).show()
                        }


                    }else{
                        txtError.text="Check your email & password"
                        //Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }catch (ex:Exception){

        }

    }

    fun forgottenPwd(view: View) {
        startActivity(Intent(this,ForgottenPwd::class.java))
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }



}