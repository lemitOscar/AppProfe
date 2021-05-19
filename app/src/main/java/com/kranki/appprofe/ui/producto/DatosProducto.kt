package com.kranki.appprofe.ui.producto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.kranki.appprofe.R
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
 * Use the [DatosProducto.newInstance] factory method to
 * create an instance of this fragment.
 */
class DatosProducto : Fragment() {
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
        var view = inflater.inflate(R.layout.fragment_datos_producto, container, false)
        var btnguardar = view.findViewById<Button>(R.id.idtxtbtnenviar)
        //recibo los id de la vista
        var tcodigo = view.findViewById<TextView>(R.id.idtxtCode)
        var tnombre = view.findViewById<TextView>(R.id.idtxtnom)
        var tmarca = view.findViewById<TextView>(R.id.idtxtmarca)
        var tdescrip = view.findViewById<TextView>(R.id.idtxtdesc)
        var tprecio = view.findViewById<TextView>(R.id.idtxtprec)
        var tcanti = view.findViewById<TextView>(R.id.idtxtcant)
        var testat = view.findViewById<TextView>(R.id.idtxtestat)
        btnguardar.setOnClickListener {
            var url = "http://192.168.1.79:8000/api/guardar_productos"
            var gson = Gson()
            val tipopet = "application/json;charset=UTF-8" .toMediaType()
            var datosendjson = gson.toJson(
                datosproducto(
                    tcodigo.text.toString(),
                    tnombre.text.toString(),
                    tmarca.text.toString(),
                    tdescrip.text.toString(),
                    tprecio.text.toString().toFloat(),
                    tcanti.text.toString().toInt(),
                    testat.text.toString().toInt()
                )
            )
            var request = Request.Builder().url(url).post(datosendjson.toRequestBody(tipopet))
            var client = OkHttpClient()
            client.newCall(request.build()).enqueue(responseCallback = object : Callback {

                override fun onResponse(call: Call, response: Response) {
                    println("ok")
                }

                override fun onFailure(call: Call, e: IOException) {
                    println("salio mal")
                }

            })

        }
        return view
    }

    //creo la clase con la que modelo los datos a mandar
    data class datosproducto(
        var codigo: String,
        var nombre: String,
        var marca: String,
        var descripcion: String,
        var precio: Float,
        var cantidad: Int,
        var estatus: Int
    )


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DatosProducto.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DatosProducto().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}