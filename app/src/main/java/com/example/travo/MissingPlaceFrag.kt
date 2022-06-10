package com.example.travo

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MissingPlaceFrag : Fragment() {

    private lateinit var btnAdd:Button
    private lateinit var edtPlace:TextInputEditText
    private lateinit var edtAddress:TextInputEditText
    private lateinit var edtCategory:TextInputEditText
    private lateinit var edtNumber:TextInputEditText
    private lateinit var db: FirebaseFirestore


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
        val view= inflater.inflate(R.layout.fragment_missing_place, container, false)

        btnAdd=view.findViewById(R.id.btnAddPlace)
        edtPlace=view.findViewById(R.id.edtPlaceName)
        edtAddress=view.findViewById(R.id.edtAddress)
        edtCategory=view.findViewById(R.id.edtCategory)
        edtNumber=view.findViewById(R.id.edtNumber)

        btnAdd.setOnClickListener {

            validateInputs()
        }

        return view
    }

    private fun validateInputs() {

        val name=edtPlace.text.toString().trim()
        val address=edtAddress.text.toString().trim()
        val category=edtCategory.text.toString().trim()
        val number=edtNumber.text.toString().trim()

        try {
            if(name.isNotEmpty() && address.isNotEmpty()){

                val userId= FirebaseAuth.getInstance().currentUser?.uid.toString()

                val missingPlace=MissingPlace(userId,name,address,category,number)

                db = FirebaseFirestore.getInstance()
                    db.collection("MissingPlace").add(missingPlace).addOnSuccessListener { documentReference ->

                        Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: $documentReference")
                        //Toast.makeText(activity,"Successfully added",Toast.LENGTH_LONG).show()

                        edtAddress.text = null
                        edtPlace.text= null
                        edtCategory.text = null
                        edtNumber.text = null

                        val builder= AlertDialog.Builder(activity)
                        builder.setTitle("Travo")
                        builder.setMessage("If the added place is validate, you will receive 5 Gems")
                        builder.setCancelable(true)
                        builder.setPositiveButton("Ok") { _, _ ->

                            val profileFrag=ProfileFrag()
                        fragmentManager?.beginTransaction()?.replace(R.id.fragContainer,profileFrag)?.commit()
                        }

                        val alertDialog: AlertDialog = builder.create()
                        alertDialog.show()



                    }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                        }


            }
            else{
                if (name.isEmpty()){

                    edtPlace.error="Enter Place Name"
                    edtPlace.requestFocus()
                    return

                }
                if (address.isEmpty()){
                    edtAddress.error="Enter Address"
                    edtAddress.requestFocus()
                    return
                }
            }
        }catch (ex:Exception){

        }

    }



}