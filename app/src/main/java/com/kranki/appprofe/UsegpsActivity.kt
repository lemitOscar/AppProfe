package com.kranki.appprofe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.*
import com.kranki.appprofe.databinding.ActivityUsegpsBinding


class UsegpsActivity : AppCompatActivity() {
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val PERMISSION_ID = 42

    // Iniciación tardía del viewBinding
    lateinit var binding : ActivityUsegpsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsegpsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Preguntar si se tiene permisos.
        if (allPermissionsGrantedGPS()){
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        } else {
            // Si no hay permisos solicitarlos al usuario.
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
        }

            leerubicacionactual()
    }

    private fun allPermissionsGrantedGPS() = REQUIRED_PERMISSIONS_GPS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun leerubicacionactual(){
        if (checkPermissions()){
            if (isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.lastLocation.addOnCompleteListener(this){ task ->
                        var location: Location? = task.result
                        if (location == null){
                            requestNewLocationData()
                        } else {
                            var lati = location.latitude.toString()
                            var longi = location.longitude.toString()
                            val location = Uri.parse("geo:"+lati+","+longi)
                            val mapIntent = Intent(Intent.ACTION_VIEW, location)
                            try {
                                startActivity(mapIntent)
                                finish()
                            } catch (e: ActivityNotFoundException) {
                            }
                            /*binding.lbllatitud.text = "LATITUD = " + location.latitude.toString()
                            binding.lbllongitud.text = "LONGITUD = " + location.longitude.toString()*/

                        }
                    }
                }
            } else {
                Toast.makeText(this, "Activar ubicación", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                this.finish()
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(){
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallBack, Looper.myLooper())
    }

    private val mLocationCallBack = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation : Location = locationResult.lastLocation
            binding.lbllatitud.text = "LATITUDddddd = " + mLastLocation.latitude.toString()
            binding.lbllongitud.text = "LONGITUDdddddd = "+ mLastLocation.longitude.toString()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    companion object {
        private val REQUIRED_PERMISSIONS_GPS= arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    }
}