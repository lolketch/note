package com.example.note.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.note.adapters.NoteAdapter
import com.example.note.MainViewModel
import com.example.note.R
import com.example.note.activity.MainActivity
import com.example.note.model.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adapter: NoteAdapter
    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<FloatingActionButton>(R.id.btn_new_note).setOnClickListener {
            findNavController().navigate(R.id.action_notesFragment_to_createNoteFragment)
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.list)
        linearLayoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
        recyclerView.layoutManager = linearLayoutManager
        adapter = NoteAdapter()
        recyclerView.adapter = adapter

        val itemSwipe = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.delete_note(viewHolder.adapterPosition)
            }

        }
        val swap = ItemTouchHelper(itemSwipe)
        swap.attachToRecyclerView(recyclerView)

        mainViewModel = ViewModelProviders.of(this).get<MainViewModel>(MainViewModel::class.java)
        mainViewModel.noteLiveData?.observe(viewLifecycleOwner, Observer<List<Any?>?> { reminders -> adapter.setItems(reminders as List<Note?>?) })

    }
}