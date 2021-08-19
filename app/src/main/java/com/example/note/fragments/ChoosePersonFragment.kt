package com.example.note.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.api.InterfaceAPI
import com.example.note.Communicator
import com.example.note.adapters.PersonAdapter
import com.example.note.R
import com.example.note.activity.MainActivity
import com.example.note.model.ApiResponse
import com.example.note.model.Results
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class ChoosePersonFragment : Fragment(), Communicator {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_person, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerPersons)
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val requestInterface = retrofit.create(InterfaceAPI::class.java)
        val call: Call<ApiResponse> = requestInterface.getPersonsJson()
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                val data: ApiResponse? = response.body()
                val personAdapter = data?.let {
                    PersonAdapter(
                        it.results as ArrayList<Results>,
                        this@ChoosePersonFragment
                    )
                }
                recyclerView.adapter = personAdapter
                recyclerView.layoutManager = LinearLayoutManager(activity)
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
            }
        })
    }

    override fun passData(name: String, email: String, image: String) {
        val bundle = Bundle()
        bundle.putString("Name",name)
        bundle.putString("Email",email)
        bundle.putString("Image",image)

        (activity as MainActivity).navController.navigate(R.id.action_choosePersonFragment_to_createNoteFragment,bundle)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_cpfragment,menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home ->{
                findNavController().navigate(R.id.action_choosePersonFragment_to_createNoteFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}