package com.devdtoo.keepnotes.ui

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.devdtoo.keepnotes.R
import com.devdtoo.keepnotes.db.Note
import com.devdtoo.keepnotes.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch


class AddNoteFragment : BaseFragment() {

    private var note : Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            edit_text_title.setText(note?.title)
            edit_text_note.setText(note?.note)
        }

        button_save.setOnClickListener { view ->

            val noteTitle = edit_text_title.text.toString().trim()
            val noteBody = edit_text_note.text.toString().trim()

            if (noteTitle.isEmpty()) {
                edit_text_title.error = "title required"
                edit_text_title.requestFocus()
                return@setOnClickListener
            }
            if (noteBody.isEmpty()) {
                edit_text_note.error = "note required"
                edit_text_note.requestFocus()
                return@setOnClickListener
            }

            launch {


                context.let {
                    val mNote = Note(noteTitle, noteBody)

                    if (note == null) {
                        NoteDatabase(it!!).getNoteDao().addNote(mNote)
                        it.toast("Note saved")
                    } else {
                        mNote.id = note!!.id
                        NoteDatabase(it!!).getNoteDao().updateNote(mNote)
                        it.toast("Note Updated")
                    }



                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view).navigate(action)
                }

            }

        }

    }

    private fun deleteNote() {
        AlertDialog.Builder(context).apply {
            setTitle("Are you sure?")
            setMessage("You cannot undo this operation")
            setPositiveButton("Yes") { _, _ ->
                launch {
                    NoteDatabase(context).getNoteDao().deleteNote(note!!)
                    context?.toast("Note Deleted!")
                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view!!).navigate(action)
                }
            }
            setNegativeButton("No") { _, _ ->

            }
        }.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete -> if (note != null) deleteNote() else context?.toast("Cannot Delete")
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

}
