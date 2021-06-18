package com.kranki.appprofe

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.graphics.toColor
import com.google.gson.Gson
import com.kranki.appprofe.databinding.ActivityLoginBinding
import com.kranki.appprofe.db.dbHelper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1000)
        //setTheme(R.style.Theme_AppProfe)
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        //enlazo el layout
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAccesar.setOnClickListener {

            val usuario = binding.txtusuariologin.editText?.text.toString()
            val password = binding.txtpasswordlogin.editText?.text.toString()

            var url = "http://192.168.1.79:8000/api/login"

            var gJson = Gson()

            val tipoPet = "application/json; charset=utf-8".toMediaType()

            var datosJsonProd = gJson.toJson(datosLogin(usuario, password))

            var request = Request.Builder().url(url).post(datosJsonProd.toRequestBody(tipoPet))

            request.addHeader("X-Requested-With", "XMLHttpRequest")
            var client = OkHttpClient()

            client.newCall(request.build()).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    var datos = response?.body?.string()

                    println("Respuesta Login-> " + datos)

                    val respuesta = gJson?.fromJson(datos, datosWS::class.java)

                    if (respuesta.token == "") {
                        runOnUiThread {
                            Toast.makeText(baseContext, respuesta.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {

                        val dbHelp = dbHelper(baseContext)
                        val dbRead = dbHelp.readableDatabase

                        val projection = arrayOf(
                            BaseColumns._ID,
                            dbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_IDUSR,
                            dbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_NAME,
                            dbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_TOKEN
                        )

                        val sortOrder =
                            "${dbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_IDUSR} DESC"

                        val cursor = dbRead.query(
                            dbHelper.FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                            projection,             // The array of columns to return (pass null to get all)
                            null,              // The columns for the WHERE clause
                            null,          // The values for the WHERE clause
                            null,             // don't group the rows
                            null,              // don't filter by row groups
                            sortOrder               // The sort order
                        )

                        val existeReg: Boolean


                        with(cursor) {
                            existeReg = moveToNext()

                            //if(existeReg)
                            //Toast.makeText(baseContext, getString(getColumnIndexOrThrow(dbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_TOKEN)), Toast.LENGTH_SHORT).show()

                        }

                        val db = dbHelp.writableDatabase
                        val values = ContentValues().apply {
                            put(
                                dbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_IDUSR,
                                respuesta.userId
                            )
                            put(
                                dbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_NAME,
                                respuesta.displayName
                            )
                            put(
                                dbHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_TOKEN,
                                respuesta.token
                            )
                        }

                        if (existeReg) {
                            db.update(
                                dbHelper.FeedReaderContract.FeedEntry.TABLE_NAME,
                                values,
                                null,
                                null
                            )

                        } else {
                            val newRowId = db?.insert(
                                dbHelper.FeedReaderContract.FeedEntry.TABLE_NAME,
                                null,
                                values
                            )
                        }

                        val intent = Intent(baseContext, MainActivity::class.java)
                        intent.putExtra("Acceso", "Ok")
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    println("Error 2 " + e.message.toString())
                }
            })

        }

    }//finBudle

    data class datosLogin(
        val email: String,
        val password: String
    )

    data class datosWS(
        val displayName: String,
        val userId: Int,
        val token: String,
        val message: String
    )

}