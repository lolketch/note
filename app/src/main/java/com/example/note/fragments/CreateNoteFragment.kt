package com.example.note.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.note.App
import com.example.note.R
import com.example.note.SharedViewModel
import com.example.note.activity.MainActivity
import com.example.note.downloadAndSetImage
import com.example.note.model.Note
import com.example.note.workmanager.RemainderWorker
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class CreateNoteFragment : Fragment(){
    private lateinit var tv_name:TextView
    private lateinit var tv_email:TextView
    private lateinit var civ_image:CircleImageView
    private lateinit var cv_setreminder:CardView
    private lateinit var tv_date:TextView
    private lateinit var tv_time:TextView
    private lateinit var cardView:CardView
    private lateinit var tv_addContact:TextView

    lateinit var calendar: Calendar

    private val sharedViewModel: SharedViewModel by activityViewModels()

    lateinit var note: Note
    lateinit var editText: EditText


    val DATE_COMPONENT_FORMAT = "dd-MM-yyyy"
    val TIME_COMPONENT_FORMAT = "HH:mm:ss"


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        sharedViewModel.data.observe(viewLifecycleOwner, { data ->
            view?.findViewById<EditText>(R.id.text)?.setText(data)
        })
        sharedViewModel.reminderDate.observe(viewLifecycleOwner, { reminderDate ->
            view?.findViewById<TextView>(R.id.tv_date)?.text = reminderDate
        })
        sharedViewModel.reminderTime.observe(viewLifecycleOwner, { reminderTime ->
            view?.findViewById<TextView>(R.id.tv_time)?.text = reminderTime
        })
        sharedViewModel.save_calendar.observe(viewLifecycleOwner, { save_calendar ->
            calendar = save_calendar
        })
        sharedViewModel.nameContact.observe(viewLifecycleOwner,{ nameContact ->
            view?.findViewById<TextView>(R.id.tv_name)?.text = nameContact
        })
        sharedViewModel.emailContact.observe(viewLifecycleOwner,{ emailContact ->
            view?.findViewById<TextView>(R.id.tv_email)?.text = emailContact
        })
        sharedViewModel.imageContact.observe(viewLifecycleOwner,{ imageContact ->
            view?.findViewById<CircleImageView>(R.id.civ_image)?.downloadAndSetImage(imageContact)
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        note = Note()
        editText = view.findViewById(R.id.text)
        tv_name = view.findViewById(R.id.tv_name)
        tv_email = view.findViewById(R.id.tv_email)
        civ_image = view.findViewById(R.id.civ_image)
        cv_setreminder = view.findViewById(R.id.cv_setreminder)
        tv_date = view.findViewById(R.id.tv_date)
        tv_time = view.findViewById(R.id.tv_time)
        cardView = view.findViewById<CardView>(R.id.cardview)
        tv_addContact = view.findViewById(R.id.tv_addContact)
        if (arguments != null || sharedViewModel.nameContact.value?.length != 0){
            tv_addContact.visibility = View.INVISIBLE
        }
        if (arguments != null) {
//            tv_name.text = arguments?.getString("Name").toString()
//            tv_email.text = arguments?.getString("Email").toString()
//            civ_image.downloadAndSetImage(arguments?.getString("Image").toString())
            sharedViewModel.saveNameContact(arguments?.getString("Name").toString())
            sharedViewModel.saveEmailContact(arguments?.getString("Email").toString())
            sharedViewModel.saveImageContact(arguments?.getString("Image").toString())
        }

        cardView.setOnClickListener {
            if (this::calendar.isInitialized){
                sharedViewModel.saveCalendar(calendar)
            }
            sharedViewModel.saveDate(tv_date.text.toString())
            sharedViewModel.saveTime(tv_time.text.toString())
            sharedViewModel.saveTitle(editText.text.toString())
            findNavController().navigate(R.id.action_createNoteFragment_to_choosePersonFragment)
        }
        cv_setreminder.setOnClickListener {
            calendar = Calendar.getInstance()
            val constraintBuilder = CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .build()
            val materialTimePicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                    .setMinute(calendar.get(Calendar.MINUTE))
                    .setTitleText("Установите дату")
                    .build()
            val materialDatePicker = MaterialDatePicker.Builder.datePicker()
                    .setCalendarConstraints(constraintBuilder)
                    .setTitleText("Выберите дату")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

            materialDatePicker.addOnPositiveButtonClickListener(MaterialPickerOnPositiveButtonClickListener<Long?> {
                calendar.timeInMillis = materialDatePicker.selection!!
                materialTimePicker.show(requireActivity().supportFragmentManager, "tag")
            })
            materialDatePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")

            materialTimePicker.addOnPositiveButtonClickListener {
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.set(Calendar.MINUTE, materialTimePicker.minute)
                calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.hour)
                val date = calendar.time
                val format_date = SimpleDateFormat(DATE_COMPONENT_FORMAT)
                val format_time = SimpleDateFormat(TIME_COMPONENT_FORMAT)
                tv_date.text = format_date.format(date)
                tv_time.text = format_time.format(date)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_cnfragment, menu)
    }

    @SuppressLint("ResourceType")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_save -> {
                if (editText.text.isNotEmpty()) {
                    note.text = editText.text.toString()
                    note.done = false
                    note.timestamp = System.currentTimeMillis()
                    if (sharedViewModel.nameContact.value?.length!! > 0 &&
                            sharedViewModel.emailContact.value?.length!! > 0) {
                        note.name_person = sharedViewModel.nameContact.value
                        note.email_person = sharedViewModel.emailContact.value
                    }
                    note.image_person = sharedViewModel.imageContact.value
                    val id = App.instance?.noteDao?.insert(note)!!
                    setRemainderNotification(id)
                    sharedViewModel.clearTitle()
                    sharedViewModel.clearDate()
                    sharedViewModel.clearContact()
                    findNavController().navigate(R.id.action_createNoteFragment_to_notesFragment)
                }
            }
            android.R.id.home -> {
                sharedViewModel.clearContact()
                sharedViewModel.clearDate()
                sharedViewModel.clearTitle()
                findNavController().navigate(R.id.action_createNoteFragment_to_notesFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setRemainderNotification(id: Long) {
        val data = Data.Builder()
                .putLong("id", id)
                .build()
        if (this::calendar.isInitialized && (calendar.timeInMillis > 0)) {
            val timeDiff = calendar.timeInMillis - System.currentTimeMillis() - 3600000
            App.instance?.noteDao?.updateReminderDate(id, calendar.timeInMillis)
            val dailyWorkRequest = OneTimeWorkRequest.Builder(RemainderWorker::class.java)
                    .setInputData(data)
                    .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                    .build()
            WorkManager.getInstance(requireContext()).enqueue(dailyWorkRequest)
            calendar.clear()
        }
    }
}