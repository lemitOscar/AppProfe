package com.kranki.appprofe.ui.clientes

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kranki.appprofe.R
import okhttp3.*
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClienteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClienteFragment : Fragment() {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var listaClientes = (activity as Activity).findViewById<RecyclerView>(R.id.listaCliente)

        when (item.itemId) {
            R.id.action_settings -> {
                //sincronizar(listaClientes)
            }

            R.id.action_sincro -> {
                sincronizar(listaClientes)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_productos, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_cliente, container, false)
        //var btnjson = view.findViewById<Button>(R.id.btnjson);
        var listacliente = view.findViewById<RecyclerView>(R.id.listaCliente)

        /*btnjson.setOnClickListener() {
            sincronizar(listacliente)
        }*/
        listacliente.layoutManager = LinearLayoutManager(context)
        return view
    }

    fun sincronizar(listacliente: RecyclerView) {
        Toast.makeText(context, "btn click", Toast.LENGTH_SHORT).show()
        var urldatos = "http://192.168.1.79:8000/api/listar_productos"
        var request = Request.Builder().url(urldatos).build()
        var cliente = OkHttpClient()
        cliente.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                var textojson = response?.body?.string()
                //print(textojson)
                //castear para acceder a un metodo
                val actMain = activity as Activity
                actMain.runOnUiThread {
                    var datosjson = Gson()
                    var clientes = datosjson?.fromJson(textojson, Array<DatosProducto>::class.java)
                    //definir adaptador
                    listacliente.adapter = ClientesAdapter(clientes)
                    //Toast.makeText(context, "Sincroniza al 100", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                val actMain = activity as Activity
                actMain.runOnUiThread {
                    Toast.makeText(context, "no jale bro sorry" + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    //----------------------------------- clase ------------------------------------------------------------------
    class DatosProducto(
        var id: Int,
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
         * @return A new instance of fragment ClienteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClienteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}