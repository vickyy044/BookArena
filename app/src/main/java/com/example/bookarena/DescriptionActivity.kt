package com.example.bookarena

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookarena.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text

class DescriptionActivity : AppCompatActivity() {

    lateinit var txtBookName : TextView
    lateinit var txtBookAuthor : TextView
    lateinit var txtBookPrice : TextView
    lateinit var txtBookRating : TextView
    lateinit var imgBookImage : ImageView
    lateinit var txtBookDescription : TextView
    lateinit var btAddToFavourite : Button
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var toolbar: Toolbar

    var bookID :String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDescription = findViewById(R.id.txtBookDescription)
        btAddToFavourite = findViewById(R.id.btAddToFavourites)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"

        if(intent !=null){
            bookID= intent.getStringExtra("book_id")
        }else{
            Toast.makeText(this@DescriptionActivity , "Intent was null, som unexpected error occurred",Toast.LENGTH_SHORT).show()
        }

        if(bookID=="100"){
            Toast.makeText(this@DescriptionActivity , " some unexpected error occurred",Toast.LENGTH_SHORT).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id",bookID)
        if(ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJSONObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE
                            progressBar.visibility = View.GONE
                            Picasso.get().load(bookJSONObject.getString("image"))
                                .error(R.drawable.default_book_cover).into(imgBookImage)
                            txtBookAuthor.text = bookJSONObject.getString("author")
                            txtBookName.text = bookJSONObject.getString("name")
                            txtBookPrice.text = bookJSONObject.getString("price")
                            txtBookRating.text = bookJSONObject.getString("rating")
                            txtBookDescription.text = bookJSONObject.getString("description")
                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some Error Occurred!!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Volley error occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "d2b74110761343"
                        return headers
                    }
                }
            queue.add(jsonRequest)
        }else{
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("ERROR")
            dialog.setMessage("Internet connection NOT found")
            dialog.setPositiveButton("Open Settings"){
                    text , listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
                //do nothing
            }
            dialog.setNegativeButton("Exit"){
                    text , listener->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
                //do nothing
            }
            dialog.create()
            dialog.show()
        }
    }
}
