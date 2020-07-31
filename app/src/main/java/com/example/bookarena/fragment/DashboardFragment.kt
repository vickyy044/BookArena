package com.example.bookarena.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textclassifier.TextLinks
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookarena.R
import com.example.bookarena.adapter.DashboardRecyclerAdapter
import com.example.bookarena.model.Book
import com.example.bookarena.util.ConnectionManager
import org.json.JSONException


class DashboardFragment : Fragment() {
    lateinit var recyclerDashboard : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    val bookInfoList = arrayListOf<Book>()
    lateinit var  recyclerAdapter : DashboardRecyclerAdapter
    lateinit var  progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_dashboard, container, false)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books"
        try{
            progressLayout.visibility=View.GONE
            if(ConnectionManager().checkConnectivity(activity as Context)){

                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET,url,null,Response.Listener {
                    // Here we will handle the response of api
                    println("Response it $it")          // 'it' is the object in which response of api is stored

                    val success = it.getBoolean("success")
                    if(success){
                        val data = it.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )
                            bookInfoList.add(bookObject)
                            recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
                            layoutManager = LinearLayoutManager(activity)
                            recyclerAdapter = DashboardRecyclerAdapter(activity as Context , bookInfoList)
                            recyclerDashboard.adapter = recyclerAdapter
                            recyclerDashboard.layoutManager= layoutManager
                            recyclerDashboard.addItemDecoration(                            // this adds 'line' seperation between views
                                DividerItemDecoration(
                                    recyclerDashboard.context,
                                    (layoutManager as LinearLayoutManager).orientation
                                )
                            )
                        }

                    }else{
                        Toast.makeText(activity as Context, "Some error has occured-> Volley error",Toast.LENGTH_SHORT).show()
                    }
                },Response.ErrorListener {
                    // Here we will handle the errors
                    println("Volley error occurred")          // 'it' is the object in which response of api is storeda
                }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String,String>()
                        headers["Content-type"] = "application/json"
                        headers["token"]="d2b74110761343"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)
            }else{
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("ERROR")
                dialog.setMessage("Internet connection NOT found")
                dialog.setPositiveButton("Open Settings"){
                        text , listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    activity?.finish()
                    //do nothing
                }
                dialog.setNegativeButton("Exit"){
                        text , listener->
                    ActivityCompat.finishAffinity(activity as Activity)
                    //do nothing
                }
                dialog.create()
                dialog.show()

            }

        }catch (e: JSONException){
            Toast.makeText(activity as Context, " Some e=unexpeced error occureed-> JSON Exception",Toast.LENGTH_SHORT).show()
        }


        return view
    }

}
