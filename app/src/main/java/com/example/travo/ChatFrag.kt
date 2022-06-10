package com.example.travo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import de.hdodenhof.circleimageview.CircleImageView
import user_chat.Chat
import user_chat.UserAdapter
import user_chat.UserChat

class ChatFrag : Fragment() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<UserChat>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var cUserImg: CircleImageView
    private lateinit var cUserName:TextView

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
        val view= inflater.inflate(R.layout.fragment_chat, container, false)

        cUserName=view.findViewById(R.id.txtUname)
        cUserImg=view.findViewById(R.id.uImg)


        mAuth = FirebaseAuth.getInstance()

        userList = arrayListOf()
        adapter = UserAdapter(activity,userList)

        userRecyclerView = view.findViewById(R.id.userRecyclerView)

        userRecyclerView.layoutManager = LinearLayoutManager(activity)
        userRecyclerView.adapter =adapter


        user()
        showUsers()

        return view
    }

    private fun user(){

        try {
            db = FirebaseFirestore.getInstance()
            FirebaseAuth.getInstance().currentUser?.uid?.let {
                db.collection("Users").document(it)
                    .get().addOnSuccessListener { document ->
                        if (document != null) {

                            val users = document.toObject(Users::class.java)
                            cUserName.text = users?.name.toString()

                            val img=users?.image.toString()

                            if(img.isNotEmpty()){
                                activity?.let { it1 -> Glide.with(it1).load(img).into(cUserImg) }
                            }

                        } else {
                            Log.d(ContentValues.TAG, "No such document")
                        }
                    }.addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "get failed with ", exception)
                    }
            }
        }catch (ex:java.lang.Exception){

        }

    }

    private fun showUsers(){

        try {
            db = FirebaseFirestore.getInstance()
            db.collection("Users")
                .addSnapshotListener(object : EventListener<QuerySnapshot> {

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null) {
                            Log.e("fireStore error", error.message.toString())
                            return
                        }
                        //userList.clear()
                        for (dc: DocumentChange in value?.documentChanges!!) {
                            if (dc.type == DocumentChange.Type.ADDED) {

                                if(dc.document.id!=FirebaseAuth.getInstance().currentUser?.uid)
                                {
                                    userList.add(dc.document.toObject(UserChat::class.java).withId(dc.document.id))
                                }
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }

                })

            adapter.setOnItemClickListener(object : UserAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    //Toast.makeText(this@HotelAct,"$position", Toast.LENGTH_SHORT).show()
                    val intent= Intent(activity, Chat::class.java)

                    intent.putExtra("name",userList[position].name)
                    intent.putExtra("uid",userList[position].id )
                    intent.putExtra("image",userList[position].image )

                    startActivity(intent)
                }

            })
        }catch (ex:Exception){

        }

    }

}