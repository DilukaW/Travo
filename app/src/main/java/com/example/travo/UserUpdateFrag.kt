package com.example.travo

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView


class UserUpdateFrag : Fragment() {

    private lateinit var btnSelect: FloatingActionButton
    private lateinit var btnUpload: Button
    private lateinit var image: CircleImageView
    private lateinit var edtUname: TextInputEditText
    private lateinit var name: String
    private lateinit var db: FirebaseFirestore
    private var imageUri: Uri? = null


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
        val view = inflater.inflate(R.layout.fragment_user_update, container, false)

        btnSelect = view.findViewById(R.id.btnSetImage)
        btnUpload = view.findViewById(R.id.btnUpdate)
        image = view.findViewById(R.id.imgProfile)
        edtUname = view.findViewById(R.id.edtUname)


        btnSelect.setOnClickListener {
            selectImage()
        }

        btnUpload.setOnClickListener {
            upload()

        }

        setImageAndText()

        return view


    }


    private fun upload() {


        name = edtUname.text.toString().trim()

        if (name.isNotEmpty()) {

            try {
                if (imageUri != null) {

                    val progressDialog = ProgressDialog(activity)
                    progressDialog.setMessage("updating")
                    progressDialog.setCancelable(false)
                    progressDialog.show()

                    val fileName = FirebaseAuth.getInstance().currentUser?.uid.toString()

                    val imageRef= FirebaseStorage.getInstance().reference.child("images/$fileName.jpg")

                    imageRef.putFile(imageUri!!)
                        .addOnSuccessListener {

                            imageRef.downloadUrl.addOnSuccessListener { uri->



                                db = FirebaseFirestore.getInstance()
                                FirebaseAuth.getInstance().currentUser?.uid?.let {
                                    db.collection("Users").document(it)
                                        .update(
                                            mapOf(
                                                "name" to name,
                                                "image" to uri.toString()
                                            )
                                        ).addOnSuccessListener {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!")
                                            
                                            Toast.makeText(activity, "Successfully updated", Toast.LENGTH_SHORT).show()
                                            progressDialog.dismiss()

                                            val profileFrag = ProfileFrag()
                                            val transaction = fragmentManager?.beginTransaction()
                                            transaction?.replace(R.id.fragContainer, profileFrag)
                                                ?.addToBackStack(null)
                                                ?.commit()

                                        }.addOnFailureListener { exception ->
                                            Toast.makeText(activity, exception.toString(),Toast.LENGTH_LONG).show()
                                            progressDialog.dismiss()
                                        }
                                }
                            }


                        }.addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(activity, "uploadFail", Toast.LENGTH_SHORT).show()

                        }.addOnProgressListener { p0 ->
                            val progress: Double = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                            progressDialog.setMessage("Uploading ${progress.toInt()}%")
                        }
              }else {

                    val progressDialog = ProgressDialog(activity)
                    progressDialog.setMessage("updating")
                    progressDialog.setCancelable(false)
                    progressDialog.show()

                    db = FirebaseFirestore.getInstance()
                    FirebaseAuth.getInstance().currentUser?.uid?.let {
                        db.collection("Users").document(it)
                            .update("name", name)
                            .addOnSuccessListener {
                                Log.d(TAG, "DocumentSnapshot successfully updated!")

                                Toast.makeText(activity, "Successfully updated", Toast.LENGTH_SHORT)
                                    .show()
                                progressDialog.dismiss()

                                val profileFrag = ProfileFrag()
                                val transaction = fragmentManager?.beginTransaction()
                                transaction?.replace(R.id.fragContainer, profileFrag)
                                    ?.addToBackStack(null)
                                    ?.commit()

                            }.addOnFailureListener { exception ->
                                Toast.makeText(activity, exception.toString(), Toast.LENGTH_LONG)
                                    .show()
                                progressDialog.dismiss()
                            }
                    }
                }

            } catch (ex: Exception) {

                Toast.makeText(activity, ex.toString(), Toast.LENGTH_LONG).show()
            }

        } else {
            edtUname.error = "Required"
            edtUname.requestFocus()
            return
        }

    }

    private fun setImageAndText(){
        try {

            db = FirebaseFirestore.getInstance()
            FirebaseAuth.getInstance().currentUser?.uid?.let {
                db.collection("Users").document(it)
                    .get().addOnSuccessListener { document ->
                        if (document != null) {

                            val users=document.toObject(Users::class.java)
                            edtUname.setText(users?.name.toString())
                            val img= users?.image.toString()

                            if (img.isNotEmpty()){
                                activity?.let { it1 -> Glide.with(it1).load(img).into(image) }
                            }


                        } else {
                            Log.d(TAG, "No such document")
                        }
                    }.addOnFailureListener { exception ->
                        Log.d(TAG, "get failed with ", exception)
                    }
            }
        }catch (ex:Exception){

        }
    }


    private fun selectImage() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        resultLauncher.launch(intent)


    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                // There are no request codes
                val data: Intent? = result.data

                imageUri = data?.data!!
                image.setImageURI(imageUri)

            }
        }






}



