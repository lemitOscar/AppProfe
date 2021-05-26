package com.kranki.appprofe.ui.clientes


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kranki.appprofe.R

class ClientesAdapter(val datos: Array<ClienteFragment.DatosProducto>) :
    RecyclerView.Adapter<CustomView>() {

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.row_layout, parent, false)
        return CustomView(cellForRow)

    }

    override fun onBindViewHolder(holder: CustomView, position: Int) {
        var nombre = holder.itemView.findViewById(R.id.txt_nombre) as TextView
        var usuario = holder.itemView.findViewById(R.id.txt_usuario) as TextView
        var correo = holder.itemView.findViewById(R.id.txt_email) as TextView
        holder.itemView.setOnClickListener {
            val navController = holder.itemView.findNavController()
            var objson = Gson();
            var datos = objson.toJson(datos[position]);

            var bundle = bundleOf("dproductos" to datos)
            navController.navigate(R.id.nav_regis_datos, bundle)
        }

        //codigo
        nombre.text = datos[position].codigo
        //nombre
        usuario.text = datos[position].nombre
        //marca
        correo.text = datos[position].marca
    }


}

class CustomView(varV: View) : RecyclerView.ViewHolder(varV)