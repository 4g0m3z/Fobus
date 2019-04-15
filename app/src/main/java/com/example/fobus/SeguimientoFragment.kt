package com.example.fobus

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SeguimientoFragment : Fragment(), OnMapReadyCallback {

    private var database = FirebaseDatabase.getInstance()
    private var map: GoogleMap?=null
    var ubicacion : LatLng? = null


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        map!!.uiSettings.isZoomControlsEnabled = true
    }
    private fun placeMarkerOnMap(location: LatLng) {
        map!!.clear()
        val markerOptions = MarkerOptions().position(location)
        markerOptions.title("Abi")
        map!!.addMarker(markerOptions)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val mapFragment = activity!!.supportFragmentManager.findFragmentById(R.id.map_fragment_seguimiento) as SupportMapFragment

        //mapFragment.getMapAsync(this)
        //var myRef = database.getReference("lastLocation")
        var myRef = database.getReference("lastLocation")
        //myRef.setValue( "Hello, World X!")
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                var value = dataSnapshot.getValue(true)
                try {
                    var hashMap= value as HashMap<String,Double>
                    ubicacion  = LatLng(hashMap.get("latitude")!!, hashMap.get("longitude")!!)
                    placeMarkerOnMap(ubicacion!!)
                    map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 12f))
                }catch (ex : Exception){
                    println(ex.message)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Tag" , "Failed to read value.", error.toException())
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_seguimiento, container, false)
        try {
            val mapFragment =childFragmentManager!!.findFragmentById(R.id.map_fragment_seguimiento) as SupportMapFragment?
            mapFragment!!.getMapAsync(this)
        }catch (ex : Exception){
            println(ex.message)
        }

        return v
    }

}
