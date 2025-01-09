package com.example.menuseafood

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListMenuSeafoodAdapter(private val listMenuSeafood: ArrayList<MenuSeafood>) : RecyclerView.Adapter<ListMenuSeafoodAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_seafood, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val seafood = listMenuSeafood[position]
        holder.imgPhoto.setImageResource(seafood.photo)
        holder.tvName.text = seafood.name
        holder.tvDescription.text = seafood.description

        // Menangani aksi klik pada setiap item
        holder.itemView.setOnClickListener {
            // Membuat Intent untuk membuka DetailActivity dan mengirim data Seafood
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("key_Seafood", seafood) // Mengirimkan objek seafood
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listMenuSeafood.size

    // Inner class untuk menyimpan referensi elemen tampilan dalam setiap item RecyclerView
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
        val tvDataDetail: TextView = itemView.findViewById(R.id.tv_item_description)

    }
}
