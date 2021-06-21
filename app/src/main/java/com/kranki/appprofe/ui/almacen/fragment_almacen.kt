package com.kranki.appprofe.ui.almacen

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kranki.appprofe.R
import com.kranki.appprofe.ui.clientes.ClienteFragment
import com.kranki.appprofe.ui.clientes.ClientesAdapter
import okhttp3.*
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_almacen.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_almacen : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    //----------------------------- para genenrar el boton de listar almacenes
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //var listaAlmacen = (activity as Activity).findViewById<RecyclerView>(R.id.listaAlmacen)

        when (item.itemId) {
            R.id.action_settings -> {
                //sincronizar(listaClientes)
            }
            R.id.action_sincro -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_productos, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //------------------------------------------------------ llamar btn
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_almacen, container, false)
        var listaAlmacen = view.findViewById<RecyclerView>(R.id.listaAlmacen)
        listaAlmacen.layoutManager = LinearLayoutManager(context)
        sincronizar(listaAlmacen)
        return view;
    }

    //------------------------------ funcion para listar
    public fun sincronizar(listaAlmacen: RecyclerView) {

        // inyectar datos
        var urldatos = "http://192.168.1.79:8000/api/listar_almacenes"
        var request = Request.Builder().url(urldatos).build()
        var cliente = OkHttpClient()

        //sobrescribir metodos

        cliente.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                var textojson = response?.body?.string()
                val actMain = activity as Activity
                actMain.runOnUiThread {
                    var datosjson = Gson()
                    var clientes = datosjson?.fromJson(textojson, Array<DatosAlmacen>::class.java)
                    listaAlmacen.adapter = AlmacenAdapter(clientes)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                val actMain = activity as Activity
                actMain.runOnUiThread {
                    Toast.makeText(context, "no jale bro sorry" + e.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    //----------------------------------- clase  para manejar datos------------------------------------------------------------------
    class DatosAlmacen(
        var id: Int,
        var nombre: String,
        var telefono: String,
        var pais: String,
        var municipio: String,
        var localidad: String,
        var codigo_postal: String,
        var calle: String,
        var num_ext: String,
        var state_id: String
    )


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_almacen.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_almacen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}