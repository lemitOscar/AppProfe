package com.kranki.appprofe.ui.clientes

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kranki.appprofe.R

class ClientesAdapter(val datos: Array<ClienteFragment.DatosModel>) :
    RecyclerView.Adapter<CustomView>() {

    override fun getItemCount(): Int {
        return datos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.row_layout, parent, false)
        return CustomView(cellForRow)

    }

    override fun onBindViewHolder(holder: CustomView, position: Int) {
        //nombre
        var nombre = holder?.itemView.findViewById(R.id.txt_nombre) as TextView
        nombre.text = datos[position].name

        //usuario
        var usuario = holder?.itemView.findViewById(R.id.txt_usuario) as TextView
        usuario.text = datos[position].username

        //correo
        var correo = holder?.itemView.findViewById(R.id.txt_email) as TextView
        correo.text = datos[position].email
    }


}

class CustomView(varV: View) : RecyclerView.ViewHolder(varV) {

}