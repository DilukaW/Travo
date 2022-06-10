package place

import com.google.firebase.firestore.Exclude

class Place {

    var description:String?=null
    var address:String?=null
    var phone:String?=null
    var heading:String?=null
    var category :String?=null
    var mainImage:String?=null

    @Exclude
    var id: String? = null
    fun <T : Place?> withId(id: String): T {
        this.id = id
        return this as T
    }
}
