package hotel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travo.R

class HotelAdapter(private val context: FragmentActivity?,private val hotelList: ArrayList<Hotel>):RecyclerView.Adapter<HotelAdapter.HotelViewHolder> (){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {

        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.hotels_list_items,parent,false)
        return HotelViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {

        val hotel: Hotel =hotelList[position]
        holder.hotelName.text=hotel.name
        holder.hotelStar.text=hotel.star
        holder.hotelPrice.text=hotel.price

        if (context != null) {
            Glide.with(context)
                .load(hotel.image)
                .into(holder.hotelImage)
        }
    }

    override fun getItemCount(): Int {

      return hotelList.size
    }

    class HotelViewHolder(itemView:View,listener: onItemClickListener):RecyclerView.ViewHolder(itemView){

            val hotelName: TextView =itemView.findViewById(R.id.txtHotelName)
            val hotelStar: TextView =itemView.findViewById(R.id.txtStar)
            val hotelPrice: TextView =itemView.findViewById(R.id.txtPrice)
            val hotelImage: ImageView =itemView.findViewById(R.id.imgHotelCard)

            init {
                itemView.setOnClickListener {
                    listener.onItemClick(adapterPosition)
                }
            }
        }

}