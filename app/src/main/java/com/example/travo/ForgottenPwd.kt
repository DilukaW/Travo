package com.example.travo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ForgottenPwd : AppCompatActivity() {

    private lateinit var edtEmail: TextInputEditText
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgotten_pwd)

        edtEmail = findViewById(R.id.edtEmail)
        firebaseAuth= FirebaseAuth.getInstance()
    }

    fun rest(view: View) {

        val email=edtEmail.text.toString().trim()

        if (email.isEmpty()){
            edtEmail.error="Required"
            edtEmail.requestFocus()
            return
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.error = "Enter Valid Email"
            edtEmail.requestFocus()
            return
        }


        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this,"Check your email to reset password",Toast.LENGTH_LONG).show()
                startActivity(Intent(this,Login::class.java))
            }
            else{
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
            }
        }

    }
}