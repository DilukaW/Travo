package user_chat

import com.google.firebase.firestore.Exclude

class UserChat{

    var name:String?=null
    var image:String?=null


    @Exclude
    var id: String? = null
    fun <T : UserChat?> withId(id: String): T {
        this.id = id
        return this as T
    }
}
