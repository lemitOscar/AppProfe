package com.kranki.appprofe.ui.clientes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kranki.appprofe.R
import com.kranki.appprofe.ui.productos.CustomView

class GalleryFragmentAdapter(val datos: Array<GalleryFragment.DatosClient>) :
    RecyclerView.Adapter<CustomView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.row_layout_client, parent, false)
        return CustomView(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomView, position: Int) {
        var nombre = holder.itemView.findViewById<TextView>(R.id.txt_nombre_c)
        var email = holder.itemView.findViewById<TextView>(R.id.txt_email_c)
        var telefono = holder.itemView.findViewById<TextView>(R.id.txt_tel_c)
        holder.itemView.setOnClickListener {
            val navController = holder.itemView.findNavController()
            var objson = Gson();
            var datos = objson.toJson(datos[position]);
            var bundle = bundleOf("dpdatosclient" to datos)
            navController.navigate(R.id.nav_client_resgis_datos, bundle)
        }

        nombre.text = datos[position].nombre;
        email.text = datos[position].email;
        telefono.text = datos[position].telefono;

    }

    override fun getItemCount(): Int {
        return datos.size
    }

}