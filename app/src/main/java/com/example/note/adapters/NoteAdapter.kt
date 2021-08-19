package com.example.note.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.example.note.App
import com.example.note.R
import com.example.note.downloadAndSetImage
import com.example.note.model.Note
import java.text.SimpleDateFormat

class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteViewHolder?>() {


    private val sortedList:SortedList<Note> = SortedList(Note::class.java,
        object : SortedList.Callback<Note>() {
            override fun compare(o1: Note?, o2: Note?): Int {
                if (!o2!!.done && o1!!.done) {
                    return 1;
                }
                if (o2.done && !o1!!.done) {
                    return -1;
                }
                return (o2.timestamp - o1!!.timestamp).toInt();
            }

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: Note?, newItem: Note?): Boolean {
                return oldItem!!.equals(newItem)
            }

            override fun areItemsTheSame(item1: Note?, item2: Note?): Boolean {
                return item1!!.uid == item2!!.uid;
            }
        })
    fun delete_note(note:Int){
        App.instance?.noteDao?.delete(sortedList[note])
    }
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val reminderText: TextView = itemView.findViewById<TextView>(R.id.note_title)
        private val nameText: TextView = itemView.findViewById<TextView>(R.id.name_text)
        private val emailText: TextView = itemView.findViewById<TextView>(R.id.email_text)
        private val imagePerson: ImageView = itemView.findViewById(R.id.photo)
        private val dateTextView: TextView = itemView.findViewById(R.id.date_text)
        private val formatter: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        private val completed: CheckBox = itemView.findViewById<CheckBox>(R.id.completed)
        var reminder: Note? = null
        var silentUpdate = false


        init {
            completed.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(compoundButton: CompoundButton, checked: Boolean) {
                    if (!silentUpdate) {
                        reminder!!.done = checked
                        App.instance?.noteDao?.update(reminder)
                    }
                    updateStrokeOut()
                }
            })
        }

        @SuppressLint("SetTextI18n")
        fun bind(reminder: Note) {
            this.reminder = reminder
            if (reminder.date_reminder > 0) dateTextView.text = "Дата: " + formatter.format(reminder.date_reminder)
            if (reminder.name_person != "null") nameText.text = "Name: " + reminder.name_person else nameText.text = "Контакт не выбран"
            if (reminder.email_person != "null") emailText.text = "Email: " + reminder.email_person else emailText.text = ""
            reminderText.text = reminder.text
            imagePerson.downloadAndSetImage(reminder.image_person!!)
            updateStrokeOut()
            silentUpdate = true
            completed.isChecked = reminder.done
            silentUpdate = false
        }
        private fun updateStrokeOut() {
            if (reminder!!.done) {
                reminderText.setPaintFlags(reminderText.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            } else {
                reminderText.setPaintFlags(reminderText.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_note_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(sortedList.get(position))
    }

    override fun getItemCount(): Int = sortedList.size()

    fun setItems(notes: List<Note?>?) {
        if (notes != null) {
            sortedList.replaceAll(notes)
        }
    }
}