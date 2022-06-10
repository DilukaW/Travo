package comment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.travo.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommentAdapter(private val context: Context, private val commentList:ArrayList<Comment>):
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {

        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.comment,parent,false)
        return CommentViewHolder(itemView)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {

        val current: Comment =commentList[position]

        val timestamp = current.time as com.google.firebase.Timestamp
        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
        val sdf = SimpleDateFormat("MMM d h:mm a ")
        val netDate = Date(milliseconds)
        val date = sdf.format(netDate).toString()


        holder.time.text=date
        holder.txtCom.text=current.comm
        holder.txtUname.text=current.Uname
    }

    override fun getItemCount(): Int {

        return commentList.size
    }

    class CommentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val time: TextView =itemView.findViewById(R.id.txtTime)
        val txtCom: TextView =itemView.findViewById(R.id.txtComment)
        val txtUname: TextView =itemView.findViewById(R.id.txtUname)
    }
}