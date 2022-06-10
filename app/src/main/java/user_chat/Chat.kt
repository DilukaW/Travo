package user_chat

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class Chat : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var img: CircleImageView
    private lateinit var txtName: TextView

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var db: FirebaseFirestore

    private lateinit var name:String
    private lateinit var image:String
    private lateinit var receiverUid:String
    private lateinit var senderUid:String

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        img=findViewById(R.id.img)
        txtName=findViewById(R.id.txtName)

        name = intent.getStringExtra("name").toString()
        image = intent.getStringExtra("image").toString()
        receiverUid = intent.getStringExtra("uid").toString()
        senderUid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        txtName.text=name
        if (image.isNotEmpty()){
            Glide.with(this).load(image).into(img)
        }

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        setChat()
        sendButton.setOnClickListener {

         addChat()
        }


    }

    private fun addChat(){

        try {
            val message = messageBox.text.toString()
            //val messageObject = Message(message,senderUid)

            val data = HashMap<String, Any>()
            data.put("message", message)
            data.put("senderId", senderUid)
            data.put("time",FieldValue.serverTimestamp())

            db = FirebaseFirestore.getInstance()
            db.collection("Chats").document(senderRoom!!).collection("messages").document().set(data)
                .addOnSuccessListener { documentReference ->

                    db.collection("Chats").document(receiverRoom!!).collection("messages").document().set(data)
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: $documentReference")

                }.addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }

            messageBox.setText("")
        }catch (ex:Exception){

        }


    }

    private fun setChat(){
        try {
            db = FirebaseFirestore.getInstance()
            db.collection("Chats").document(senderRoom!!).collection("messages").orderBy("time",Query.Direction.ASCENDING)
                .addSnapshotListener(object : EventListener<QuerySnapshot> {

                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        //
                        if (error != null) {
                            Log.e("fireStore error", error.message.toString())
                            return
                        }
                        for (dc: DocumentChange in value?.documentChanges!!) {

                            if (dc.type == DocumentChange.Type.ADDED) {
                                messageList.add(dc.document.toObject(Message::class.java))
                            }
                        }
                        messageAdapter.notifyDataSetChanged()
                    }
                })
        }catch (ex:Exception){

            //Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show()
        }


    }














}