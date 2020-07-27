package com.example.bookarena.adapter

import android.content.Context
import android.icu.text.StringSearch
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookarena.R
import com.example.bookarena.model.Book
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import org.w3c.dom.Text

class DashboardRecyclerAdapter (val context:Context , val itemlist: ArrayList<Book>): RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {
    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var textBookName : TextView = view.findViewById(R.id.txtBookName)
        val textBookAuthor : TextView = view.findViewById(R.id.txtBookAuthor)
        val textBookPrice : TextView = view.findViewById(R.id.txtBookPrice)
        val textBookRating : TextView = view.findViewById(R.id.txtBookRating)
        val imgBookImage : ImageView = view.findViewById(R.id.imgBookImage)
        val llcontent : LinearLayout = view.findViewById(R.id.llcontent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  itemlist.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemlist[position]
        holder.textBookName.text = book.bookName
        holder.textBookAuthor.text = book.authorName
        holder.textBookPrice.text = book.bookPrice
        holder.textBookRating.text = book.bookRating
        holder.imgBookImage.setImageResource(book.bookImage)
        holder.llcontent.setOnClickListener(
            View.OnClickListener {
                Toast.makeText(context,"Clicked on ${holder.textBookName.text}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}