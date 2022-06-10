package place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travo.R

class PlaceAdapter(private val context: FragmentActivity?, private val placeList:ArrayList<Place>):RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener=listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {

        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.place_list_items,parent,false)
        return PlaceViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {

        val place: Place =placeList[position]
        holder.heading.text=place.heading
        holder.description.text=place.category

        if (context != null) {
            Glide.with(context)
                .load(place.mainImage)
                .into(holder.image)
        }
    }

    override fun getItemCount(): Int {

        return placeList.size
    }

    class PlaceViewHolder(itemView:View,listener: onItemClickListener):RecyclerView.ViewHolder(itemView){

        val heading:TextView=itemView.findViewById(R.id.txtHeading)
        val description:TextView=itemView.findViewById(R.id.txtDescription)
        val image:ImageView=itemView.findViewById(R.id.imgCard)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}