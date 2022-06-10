package user_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travo.R
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(val context:FragmentActivity?, private val userList: ArrayList<UserChat>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView= LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textName.text = currentUser.name

        if (context != null) {
            if (currentUser.image!!.isNotEmpty()){
                Glide.with(context)
                    .load(currentUser.image)
                    .into(holder.image)
            }

        }

    }

    override fun getItemCount(): Int {
        return userList.size

    }

    class UserViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val textName: TextView = itemView.findViewById(R.id.txtHeading)
        val image: CircleImageView= itemView.findViewById(R.id.img)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}