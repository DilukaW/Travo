package com.example.travo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

class SupportFrag : Fragment() {

    private lateinit var btnSend:Button
    private lateinit var edtSubject:TextInputEditText
    private lateinit var edtMsg:TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_support, container, false)

        btnSend=view.findViewById(R.id.btnSend)
        edtSubject=view.findViewById(R.id.edtSubject)
        edtMsg=view.findViewById(R.id.edtMsg)

        btnSend.setOnClickListener {
            sendData()
        }

        return view
    }

    private fun sendData() {

        val subject=edtSubject.text.toString().trim()
        val msg=edtMsg.text.toString().trim()
        val to="travo.admin@gmail.com"


        if (subject.isNotEmpty() && msg.isNotEmpty()){


            val address=to.split(",".toRegex()).toTypedArray()

            val mIntent=Intent(Intent.ACTION_SEND)

                mIntent.data= Uri.parse("mailto:")
            mIntent.type="text/plain"

                mIntent.putExtra(Intent.EXTRA_EMAIL,address)
                mIntent.putExtra(Intent.EXTRA_SUBJECT,subject)
                mIntent.putExtra(Intent.EXTRA_TEXT,msg)

            try {
                startActivity(Intent.createChooser(mIntent,"Choose Email Client"))


            }catch (e:Exception){
                Toast.makeText(activity,"Required App is not installed",Toast.LENGTH_SHORT).show()
            }

        }
        else{
            if (subject.isEmpty()){

                edtSubject.error="Required"
                edtSubject.requestFocus()
                return
            }
            if (msg.isEmpty()){
                edtMsg.error="Required"
                edtMsg.requestFocus()
                return
            }
        }
    }




}