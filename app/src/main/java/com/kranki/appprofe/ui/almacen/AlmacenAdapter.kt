package com.kranki.appprofe.ui.almacen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kranki.appprofe.R

class AlmacenAdapter (val datos: Array<fragment_almacen.DatosAlmacen>):

    RecyclerView.Adapter<CustomView>() {

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.row_layout_almacen, parent, false)
        return CustomView(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomView, position: Int) {
        var nombre = holder.itemView.findViewById(R.id.txt_nombre_a) as TextView
        var telefono = holder.itemView.findViewById(R.id.txt_telef_a) as TextView
        var localidad = holder.itemView.findViewById(R.id.locali_A) as TextView
        //var codio = holder.itemView.findViewById(R.id.txt_email) as TextView
        holder.itemView.setOnClickListener {
            val navController = holder.itemView.findNavController()
            var objson = Gson();
            var datos = objson.toJson(datos[position]);

            var bundle = bundleOf("dpalmacen" to datos)
            navController.navigate(R.id.nav_regis_datos_Almacen, bundle)
        }

        //codigo
        nombre.text = datos[position].nombre
        //nombre
        telefono.text = datos[position].telefono
        //marca
        localidad.text = datos[position].localidad
    }

}


class CustomView(varV: View) : RecyclerView.ViewHolder(varV)