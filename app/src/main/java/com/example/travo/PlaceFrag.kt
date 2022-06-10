package com.example.travo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import place.Place
import place.PlaceAdapter
import place.PlaceIntent
import java.util.*

class PlaceFrag : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var placeArrayList: ArrayList<Place>
    //private lateinit var placeDetailsArrayList: ArrayList<PlaceDetails>
    private lateinit var searchList: ArrayList<Place>
    //private lateinit var docIdList: ArrayList<String>
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var searchView: SearchView
    private lateinit var btnHotel: Button

    private lateinit var db: FirebaseFirestore

    private lateinit var clickLocation:String

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
        val view= inflater.inflate(R.layout.fragment_place, container, false)



        val args=this.arguments
        val loc=args?.get("location")
        clickLocation=loc.toString()

        btnHotel=view.findViewById(R.id.btnHotel)
        searchView=view.findViewById(R.id.searchPlace)

        recyclerView=view.findViewById(R.id.rvCards)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        placeArrayList= arrayListOf()
        searchList= arrayListOf()
        //placeDetailsArrayList= arrayListOf()
        //docIdList= arrayListOf()

        placeAdapter= PlaceAdapter(activity,searchList)
        recyclerView.adapter=placeAdapter


        btnHotel.setOnClickListener {
            /*val intent= Intent(this, HotelAct::class.java)
            intent.putExtra("location",clickLocation)
            startActivity(intent)
            finish()*/


            val bundle=Bundle()
            bundle.putString("location",clickLocation)
            val hotelFrag=HotelFrag()
            hotelFrag.arguments=bundle

            fragmentManager?.beginTransaction()?.replace(R.id.fragContainer,hotelFrag)?.commit()

        }
        searchItems()
        setPlaceData()


        return view
    }

    private fun searchItems() {

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {

                searchList.clear()
                val searchText= newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()){
                    placeArrayList.forEach{
                        if(it.heading!!.lowercase(Locale.getDefault()).contains(searchText)){

                            searchList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                }else{
                    searchList.clear()
                    searchList.addAll(placeArrayList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }

        })

    }

    private fun setPlaceData() {


        try{
            db = FirebaseFirestore.getInstance()
            db.collection("Place").whereEqualTo("location",clickLocation)
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        if (error != null) {
                            Log.e("fireStore error", error.message.toString())
                            return
                        }
                        for (dc: DocumentChange in value?.documentChanges!!) {
                            if (dc.type == DocumentChange.Type.ADDED) {
                                placeArrayList.add(dc.document.toObject(Place::class.java).withId(dc.document.id))
                                //placeDetailsArrayList.add(dc.document.toObject(PlaceDetails::class.java))
                                //docIdList.add(dc.document.id)

                            }

                        }
                        searchList.addAll(placeArrayList)
                        placeAdapter.notifyDataSetChanged()
                    }

                })
            placeAdapter.setOnItemClickListener(object : PlaceAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {

                    //Toast.makeText(this@MainActivity2,"$position",Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, PlaceIntent::class.java)
                    intent.putExtra("heading", searchList[position].heading)
                    intent.putExtra("category", searchList[position].category)
                    intent.putExtra("description", searchList[position].description)
                    intent.putExtra("address", searchList[position].address)
                    intent.putExtra("phone", searchList[position].phone)
                    // intent.putExtra("id", placeDetailsArrayList[position].id)
                    intent.putExtra("docId", searchList[position].id)
                    //intent.putExtra("image", placeArrayList[position].mainImage)


                    startActivity(intent)


                }
            })
        }catch (ex:Exception){

        }




    }

}