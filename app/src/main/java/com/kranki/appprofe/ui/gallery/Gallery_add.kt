package com.kranki.appprofe.ui.gallery

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
import com.kranki.appprofe.ui.almacen.almacenAddFragment
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
 * Use the [Gallery_add.newInstance] factory method to
 * create an instance of this fragment.
 */
class Gallery_add : Fragment() {
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
        var view = inflater.inflate(R.layout.fragment_gallery_add, container, false)
        //botones
        var btnguardar = view.findViewById<Button>(R.id.idtxtbtnenviar)
        var btnelimiar = view.findViewById<Button>(R.id.idtxteliminarr)

        var nombre = view.findViewById<TextView>(R.id.idtxtnom_c)
        var email = view.findViewById<TextView>(R.id.idtxtemail_c)
        var telefono = view.findViewById<TextView>(R.id.idtxtel_c)
        var pais = view.findViewById<TextView>(R.id.idtxtpais_c)
        var municipio = view.findViewById<TextView>(R.id.idtxtmunic_c)
        var localidad = view.findViewById<TextView>(R.id.idtxtlocli_c)
        var cp = view.findViewById<TextView>(R.id.idtxtcp_c)

        //mandar los datos
        var datosGson = Gson();
        var datoProc = datosGson.fromJson(arguments?.getString("dpdatosclient"), datosclientes::class.java)

        nombre.text = datoProc?.nombre
        email.text = datoProc?.email
        telefono.text = datoProc?.telefono
        pais.text = datoProc?.pais
        municipio.text = datoProc?.municipio
        localidad.text = datoProc?.localidad
        cp.text = datoProc?.codigo_postal

        btnguardar.setOnClickListener{
            var url = "http://192.168.1.79:8000/api/guardar_clientes"
            var gson = Gson()
            val tipopet = "application/json;charset=UTF-8".toMediaType()
            var datosendjson = gson.toJson(
                datosclientes(
                    datoProc?.id,
                    nombre.text.toString(),
                    email.text.toString(),
                    telefono.text.toString(),
                    pais.text.toString(),
                    municipio.text.toString(),
                    localidad.text.toString(),
                    cp.text.toString()
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
                        nav.navigate(R.id.nav_client)
                        println("ok")
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    val actMain = activity as Activity
                    actMain.runOnUiThread {
                        Toast.makeText(context, "salio mal correctamente...", Toast.LENGTH_SHORT).show()
                        println("no ok")
                    }
                }
            })
        }

        //---------------btn eliminar
        btnelimiar.setOnClickListener{
            var url = "http://192.168.1.79:8000/api/eliminar_clientes";
            var gson = Gson()
            val tipopet = "application/json;charset=UTF-8".toMediaType()
            var datosendjson = gson.toJson(Gallery_add.datoclientEliminar(datoProc.id))
            var request = Request.Builder().url(url).delete(datosendjson.toRequestBody(tipopet))
            var client = OkHttpClient()
            client.newCall(request.build()).enqueue(responseCallback = object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val actMain = activity as Activity
                    actMain.runOnUiThread {
                        Toast.makeText(context, "Borrado Correctamente", Toast.LENGTH_SHORT).show()
                        println("borrado correcto")
                        val nav = view.findNavController()
                        nav.navigate(R.id.nav_client)
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

        return  view
    }

    data class datoclientEliminar(
        var id: Int?
    )



    data class datosclientes(
        var id: Int?,
        var nombre: String,
        var email: String,
        var telefono: String,
        var pais: String,
        var municipio: String,
        var localidad: String,
        var codigo_postal: String
    )


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Gallery_add.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Gallery_add().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}