package com.example.note.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.note.Communicator
import com.example.note.R
import com.example.note.downloadAndSetImage
import com.example.note.model.Results
import kotlinx.coroutines.*
import java.util.*

class PersonAdapter(private val results: ArrayList<Results>,
                    private val listener: Communicator
                    ): RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_person_list, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        uiScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                holder.photo.downloadAndSetImage(results[position].picture.thumbnail)
            }
        }
        holder.email.text = "Email: "+ results[position].email
        holder.name.text = "Name: "+ results[position].name.first + " " + results[position].name.last
        holder.itemView.setOnClickListener(){
            val name = results[position].name.first + " " + results[position].name.last
            val email = results[position].email
            val image = results[position].picture.thumbnail
            if (position != RecyclerView.NO_POSITION) {
                listener.passData(name, email, image)
            }
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo: ImageView = itemView.findViewById(R.id.photo)
        val name: TextView = itemView.findViewById(R.id.name)
        val email: TextView = itemView.findViewById(R.id.email)
    }

}