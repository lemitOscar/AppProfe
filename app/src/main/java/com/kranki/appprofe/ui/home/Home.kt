package com.kranki.appprofe.ui.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kranki.appprofe.Camara
import com.kranki.appprofe.R
import com.kranki.appprofe.UsegpsActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


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
        var view = inflater.inflate(R.layout.fragment_homeo_oficial, container, false)

        ///------------- METODO PARA TOMAR FOTO
        var takefoto = view.findViewById<Button>(R.id.btncamara)
        takefoto.setOnClickListener {
            val intent = Intent(getActivity(), Camara::class.java)
            startActivity(intent)
        }

        //-------------METODO PARA ABRIR GOOGLE
        var gotogoogle = view.findViewById<Button>(R.id.btgps)
        gotogoogle.setOnClickListener {

            val intent = Intent(getActivity(), UsegpsActivity::class.java)
            startActivity(intent)

        //INICIAR GOOGLE
            /*val location = Uri.parse("geo:19.70260038242432,-98.98177092950176")
            val mapIntent = Intent(Intent.ACTION_VIEW, location)
            try {
                startActivity(mapIntent)
            } catch (e: ActivityNotFoundException) {
            }
            Toast.makeText(context, "si soy", Toast.LENGTH_SHORT).show()*/
        }
        return view;

    }


    companion object {
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