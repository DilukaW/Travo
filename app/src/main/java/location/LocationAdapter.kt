package location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.travo.R

class LocationAdapter(private val context: FragmentActivity?,private val locationList: ArrayList<LocationDataClass>):RecyclerView.Adapter<LocationAdapter.LocationViewHolder> (){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {

        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.location_list_item,parent,false)
        return LocationViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {

        val loc: LocationDataClass =locationList[position]
        holder.locationTxt.text=loc.location
    }

    override fun getItemCount(): Int {

        return locationList.size
    }


    class LocationViewHolder(itemView: View,listener: onItemClickListener):RecyclerView.ViewHolder(itemView){

        val locationTxt:TextView=itemView.findViewById(R.id.txtLocation)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}