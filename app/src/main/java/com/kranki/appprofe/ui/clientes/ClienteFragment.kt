package com.kranki.appprofe.ui.clientes

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kranki.appprofe.R
import com.kranki.appprofe.db.dbHelper
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

    //----------------------------------- clase ------------------------------------------------------------------
    class DatosProducto(
        var id: Int?,
        var codigo: String,
        var nombre: String,
        var marca: String,
        var descripcion: String,
        var precio: String,
        var cantidad: String,
        var estatus: String
    )

    fun sincronizar(listacliente: RecyclerView) {
        Toast.makeText(context, "Sincronizando", Toast.LENGTH_SHORT).show()
        var urldatos = "http://192.168.1.79:8000/api/listar_productos_filtro"

        //para primera conexion
        var tipopeticion = "application/json;charset=UTF-8".toMediaType()

        var gson = Gson()
        var datosjson = gson.toJson(datospeticion("%"))

        var request = Request.Builder().url(urldatos).post(datosjson.toRequestBody(tipopeticion))

        val dbHelp = dbHelper(context as Context)
        val dbRead = dbHelp.readableDatabase
        val cursor = dbRead.query(
            dbHelper.FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
            null,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,             // don't group the rows
            null,              // don't filter by row groups
            null               // The sort order
        )

        var token = ""

        with(cursor) {
            moveToNext()
            token =
                getString(getColumnIndexOrThrow(dbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_TOKEN))
        }
        request.addHeader("Accept", "application/json")
        request.addHeader("Authorization", "Bearer " + token)


        var cliente = OkHttpClient()
        cliente.newCall(request.build()).enqueue(object : Callback {
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


    data class datospeticion(
        var codigo: String
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