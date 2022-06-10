package com.example.travo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import hotel.Hotel
import hotel.HotelAdapter
import hotel.HotelIntent
import java.util.*
import kotlin.collections.ArrayList


class AllHotelFrag : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var hotelArrayList: ArrayList<Hotel>
    //private lateinit var hotelDetailsArrayList: ArrayList<HotelDetails>
    private lateinit var searchList: ArrayList<Hotel>
    private lateinit var searchView: SearchView
    //private lateinit var docIdList: ArrayList<String>
    private lateinit var hotelAdapter: HotelAdapter
    private lateinit var btnPlace: Button

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
        val view= inflater.inflate(R.layout.fragment_all_hotel, container, false)

        btnPlace=view.findViewById(R.id.btnPlace)
        searchView=view.findViewById(R.id.searchHotel)

        recyclerView=view.findViewById(R.id.rvCards)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        hotelArrayList= arrayListOf()
        searchList= arrayListOf()
        //hotelDetailsArrayList= arrayListOf()
        //docIdList= arrayListOf()

        hotelAdapter= HotelAdapter(activity,searchList)
        recyclerView.adapter=hotelAdapter

        btnPlace.setOnClickListener {

            val allPlaceFrag=AllPlaceFrag()
            fragmentManager?.beginTransaction()?.replace(R.id.fragContainer,allPlaceFrag)?.commit()
        }

        searchItems()
        setHotelData()

        return view
    }

    private fun searchItems() {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                searchList.clear()
                val searchText= newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()){
                    hotelArrayList.forEach{
                        if(it.name!!.lowercase(Locale.getDefault()).contains(searchText)){
                            searchList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                }else{
                    searchList.clear()
                    searchList.addAll(hotelArrayList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }

        })

    }

    private fun setHotelData() {

        try{
            db = FirebaseFirestore.getInstance()
            db.collection("Hotel")
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null) {
                            Log.e("fire tore error", error.message.toString())
                            return
                        }
                        for (dc: DocumentChange in value?.documentChanges!!) {
                            if (dc.type == DocumentChange.Type.ADDED) {
                                hotelArrayList.add(dc.document.toObject(Hotel::class.java).withId(dc.document.id))
                                //hotelDetailsArrayList.add(dc.document.toObject(HotelDetails::class.java))
                                //docIdList.add(dc.document.id)
                            }
                        }
                        searchList.addAll(hotelArrayList)
                        hotelAdapter.notifyDataSetChanged()
                    }
                })
            hotelAdapter.setOnItemClickListener(object : HotelAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    //Toast.makeText(this@HotelAct,"$position", Toast.LENGTH_SHORT).show()
                    val intent= Intent(activity, HotelIntent::class.java)
                    intent.putExtra("hotelName",searchList[position].name)
                    intent.putExtra("hotelPrice",searchList[position].price)
                    intent.putExtra("hotelStar",searchList[position].star)
                    //intent.putExtra("hotelImage",hotelArrayList[position].image)
                    intent.putExtra("phone", searchList[position].phone)
                    intent.putExtra("hotelAddress",searchList[position].address)
                    intent.putExtra("hotelDescription",searchList[position].description)
                    //intent.putExtra("id", hotelDetailsArrayList[position].id)
                    intent.putExtra("docId",searchList[position].id)
                    startActivity(intent)
                }

            })
        }catch (ex:Exception){

        }


    }


}