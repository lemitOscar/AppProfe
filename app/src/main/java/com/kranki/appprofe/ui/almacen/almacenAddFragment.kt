package com.kranki.appprofe.ui.almacen

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.kranki.appprofe.R
import com.kranki.appprofe.ui.producto.DatosProducto
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [almacenAddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class almacenAddFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_almacen_add, container, false)

        //botones
        var btnguardar = view.findViewById<Button>(R.id.idtxtbtnenviar)
        var btnelimiar = view.findViewById<Button>(R.id.idtxteliminarr)

        var nombre  = view.findViewById<TextView>(R.id.idtxtnom_a)
        var telefono  = view.findViewById<TextView>(R.id.idtxttel_a)
        var pais  = view.findViewById<TextView>(R.id.idtxtpais_a)
        var muncipio = view.findViewById<TextView>(R.id.idtxtmuni_a)
        var localidad = view.findViewById<TextView>(R.id.idtxtloca_a)
        var cp = view.findViewById<TextView>(R.id.idtxtcodi_a)
        var calle = view.findViewById<TextView>(R.id.idtxtcalle_a)
        //mandar los datos
        var datosGson = Gson();
        var datoProdd = datosGson.fromJson(arguments?.getString("dpalmacen"), datosalmacen::class.java)

        nombre.text =  datoProdd?.nombre
        telefono.text = datoProdd?.telefono
        pais.text = datoProdd?.pais
        muncipio.text = datoProdd?.municipio
        localidad.text = datoProdd?.localidad
        cp.text = datoProdd?.codigo_postal
        calle.text = datoProdd?.calle

        //---------------------------------------- metdods para guardar y actualizar
        btnguardar.setOnClickListener{
            var url = "http://192.168.1.79:8000/api/guardar_almacenes"
            var gson = Gson()
            val tipopet = "application/json;charset=UTF-8".toMediaType()
            var datosendjson = gson.toJson(
                datosalmacen(
                    datoProdd?.id,
                    nombre.text.toString(),
                    telefono.text.toString(),
                    pais.text.toString(),
                    muncipio.text.toString(),
                    localidad.text.toString(),
                    cp.text.toString(),
                    calle.text.toString()
                )
            )
            var request = Request.Builder().url(url).post(datosendjson.toRequestBody(tipopet))
            var client = OkHttpClient()
            client.newCall(request.build()).enqueue(responseCallback = object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val actMain = activity as Activity
                    actMain.runOnUiThread {
                        Toast.makeText(context, "Gurdado correctamente...", Toast.LENGTH_SHORT).show()
                        val nav = view.findNavController()
                        nav.navigate(R.id.nav_almacen)
                        println("ok")
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    println("salio mal")
                }
            })
        }

        //metodo para borrar
        btnelimiar.setOnClickListener {
            var url = "http://192.168.1.79:8000/api/eliminar_almacen";
            var gson = Gson()
            val tipopet = "application/json;charset=UTF-8".toMediaType()
            var datosendjson = gson.toJson(DatosProducto.datoeliminar(datoProdd.id))
            var request = Request.Builder().url(url).delete(datosendjson.toRequestBody(tipopet))
            var client = OkHttpClient()
            client.newCall(request.build()).enqueue(responseCallback = object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    val actMain = activity as Activity
                    actMain.runOnUiThread {
                        Toast.makeText(context, "Borrado Correctamente", Toast.LENGTH_SHORT).show()
                        val nav = view.findNavController()
                        nav.navigate(R.id.nav_almacen)
                        println("borrado ok")
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    val actMain = activity as Activity
                    actMain.runOnUiThread {
                        Toast.makeText(context, "no se pudo", Toast.LENGTH_SHORT).show()
                        println("no ok")
                    }
                }
            })
        }

        return view
    }

    data class  datosalmaceneliminar(
        var id: Int?
    )

    data class datosalmacen(
        var id: Int?,
        var nombre: String,
        var telefono: String,
        var pais: String,
        var municipio: String,
        var localidad: String,
        var codigo_postal: String,
        var calle: String
    )

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment almacenAddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            almacenAddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}