package com.example.travo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import location.LocationAdapter
import location.LocationDataClass
import java.util.*

class LocationFrag : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var locationArraylist: ArrayList<LocationDataClass>
    private lateinit var locationsArray: ArrayList<String>
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var searchList: ArrayList<LocationDataClass>
    private lateinit var searchView: SearchView


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
        val view= inflater.inflate(R.layout.fragment_location, container, false)

        locationsArray= arrayListOf(
            "Colombo", "Gampaha", "Kalutara" ,"Kandy", "Matale" ,"Nuwara Eliya" ,"Galle" ,
            "Matara" ,"Hambantota" ,"Jaffna" ,"Kilinochchi" ,"Mannar", "Vavuniya","Mullaitivu",
            "Batticaloa", "Ampara" ,"Trincomalee", "Kurunegala","Puttalam","Anuradhapura",
            "Polonnaruwa","Badulla", "Moneragala", "Ratnapura", "Kegalle"
        )

        searchView=view.findViewById(R.id.searchLocation)
        recyclerView=view.findViewById(R.id.rvLocations)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        locationArraylist= arrayListOf()
        searchList= arrayListOf()

        locationAdapter= LocationAdapter(activity,searchList)
        recyclerView.adapter=locationAdapter

        searchItems()
        getLocationData()
        itemClick()

        return view
    }
    private fun getLocationData() {

        for(i in locationsArray.indices){
            val locationDataClass= LocationDataClass(locationsArray[i])
            locationArraylist.add(locationDataClass)

        }
        locationAdapter.setOnItemClickListener(object: LocationAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(activity,"$position", Toast.LENGTH_SHORT).show()
            }

        })
        searchList.addAll(locationArraylist)
    }

    private fun searchItems() {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {

                searchList.clear()
                val searchText= newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()){
                    locationArraylist.forEach{
                        if(it.location.lowercase(Locale.getDefault()).contains(searchText)){

                            searchList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                }else{
                    searchList.clear()
                    searchList.addAll(locationArraylist)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }

        })

    }

    private fun itemClick(){

        locationAdapter.setOnItemClickListener(object : LocationAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                //Toast.makeText(this@Location,"$position",Toast.LENGTH_SHORT).show()

                /*val intent = Intent(activity, PlaceAct::class.java)
                intent.putExtra("location", locationArraylist[position].location)
                startActivity(intent)*/

                val bundle=Bundle()
                bundle.putString("location",searchList[position].location)
                val placeFrag=PlaceFrag()
                placeFrag.arguments=bundle

                fragmentManager?.beginTransaction()?.replace(R.id.fragContainer,placeFrag)?.addToBackStack(null)?.commit()
            }
        })

    }


}