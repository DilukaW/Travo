package com.example.travo

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView


class ProfileFrag : Fragment() {

    private lateinit var edtProfile:CardView
    private lateinit var addPlace:CardView
    private lateinit var support:CardView
    private lateinit var logout:CardView
    private lateinit var txtName:TextView
    private lateinit var txtGem:TextView
    private lateinit var imgProfile:CircleImageView
    private var x=0

    private  lateinit var preferences: SharedPreferences
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
        val view=inflater.inflate(R.layout.fragment_profile, container, false)

        edtProfile=view.findViewById(R.id.edtProfile)
        addPlace=view.findViewById(R.id.addPlace)
        support=view.findViewById(R.id.support)
        logout=view.findViewById(R.id.logout)
        txtName=view.findViewById(R.id.txtUname)
        txtGem=view.findViewById(R.id.txtGem)
        imgProfile=view.findViewById(R.id.imgProfile)




        edtProfile.setOnClickListener {

            val userUpdateFrag=UserUpdateFrag()
            fragmentManager?.beginTransaction()?.replace(R.id.fragContainer,userUpdateFrag)?.addToBackStack(null)?.commit()
        }

        addPlace.setOnClickListener {

            val missingPlaceFrag=MissingPlaceFrag()
            fragmentManager?.beginTransaction()?.replace(R.id.fragContainer,missingPlaceFrag)?.addToBackStack(null)?.commit()
        }

        support.setOnClickListener {

            val supportFrag=SupportFrag()
            fragmentManager?.beginTransaction()?.replace(R.id.fragContainer,supportFrag)?.addToBackStack(null)?.commit()

        }

        logout.setOnClickListener {

            val builder=AlertDialog.Builder(activity)
            builder.setTitle("Log Out")
            builder.setMessage("Do you want to log out")
            builder.setCancelable(true)
            builder.setPositiveButton("Yes"){ _, _ ->

                preferences= this.requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
                val email=preferences.getString("EMAIL","")

                val editor:SharedPreferences.Editor=preferences.edit()
                editor.clear()
                editor.apply()
                FirebaseAuth.getInstance().signOut()
                val intent=Intent(activity,Login::class.java)
                startActivity(intent)
                activity?.finish()



            }
            builder.setNegativeButton("No"){ _, _ ->

            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()


        }


        setProfileData()
        addGems()

        return view
    }

    private fun setProfileData() {

        try {
            db = FirebaseFirestore.getInstance()
            FirebaseAuth.getInstance().currentUser?.uid?.let {
                db.collection("Users").document(it)
                    .get().addOnSuccessListener { document ->
                        if (document != null) {


                            val users = document.toObject(Users::class.java)
                            txtName.text = users?.name.toString()
                            //txtGem.text = users?.gem.toString()
                            var gem= users?.gem?.toInt()

                            if(x == 0){
                                txtGem.text=gem.toString()
                            }

                                repeat(x) {
                                    gem = gem?.plus(5)
                                    txtGem.text = gem.toString()
                                }
                            x=0


                            val img=users?.image.toString()

                            if(img.isNotEmpty()){
                                activity?.let { it1 -> Glide.with(it1).load(img).into(imgProfile) }
                            }



                        } else {
                            Log.d(ContentValues.TAG, "No such document")
                        }
                    }.addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "get failed with ", exception)
                    }
            }
        } catch (ex: Exception) {

        }

    }

    private fun addGems(){

        try{
            val userId= FirebaseAuth.getInstance().currentUser?.uid.toString()

            db.collection("MissingPlace").whereEqualTo("userId",userId).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val missingPlace=document.toObject(MissingPlace::class.java)
                        if (missingPlace.status == true){
                            x+=1


                        }

                        Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    }

                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
        }catch (ex:Exception){

        }

    }





}