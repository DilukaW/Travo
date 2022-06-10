package user_chat

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

class Message {
    var message: String? = null
    var senderId: String? = null
    var time:Timestamp?=null
}