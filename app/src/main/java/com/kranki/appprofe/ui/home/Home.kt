package com.kranki.appprofe.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.kranki.appprofe.Camara
import com.kranki.appprofe.R
import com.kranki.appprofe.databinding.ActivityCamara2Binding
import com.kranki.appprofe.databinding.ActivityLoginBinding
import com.kranki.appprofe.ui.productos.DatosProducto
import kotlinx.android.synthetic.main.fragment_homeo_oficial.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
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
        lateinit var binding: ActivityCamara2Binding
        binding = ActivityCamara2Binding.inflate(layoutInflater)
        //setContentView(binding.root)

        var view = inflater.inflate(R.layout.fragment_homeo_oficial, container, false)

        var btntomarfotos = view.findViewById<Button>(R.id.btncamara)
        btntomarfotos.setOnClickListener{
            val intent = Intent(getActivity(), Camara::class.java)
            startActivity(intent)

        }

        return view;

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}