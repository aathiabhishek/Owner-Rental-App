package com.example.firestorervtemplate

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.firestorervtemplate.databinding.ActivityEditScreenBinding

class PropertyDetails : AppCompatActivity() {

    lateinit var binding: ActivityEditScreenBinding

    // By default, this screen is used for adding items
    // true = adding an item
    // false = editing an item
    var isAdding:Boolean = true

    // By default, we are adding, so there is no pre-defined document id
    // This value should be set by an intent when we are updating a document
    // Data type is set to String? because intent.getStringExtra() returns String?
    var docId:String = ""

    // TODO: Add a reference to the database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set the title and button text based on whether this screen
        // is used for adding vs. editing
        if (intent != null) {
            isAdding = intent.getBooleanExtra("KEY_IS_ADDING", true)

            var idFromIntent = intent.getStringExtra("KEY_DOCUMENT_ID")
            if (idFromIntent == null) {
                docId = ""
            } else {
                docId = idFromIntent
            }
        }


        // Setup the UI for an "Add"
        if (isAdding == true) {
            binding.tvTitle.text = "Add Student Form"
            binding.btnSubmit.text = "Add Student"
            // disable the document id text box because we want Firestore to
            // autogenerate the document id
            binding.etDocId.isVisible = false
        }
        // Setup the UI for an "Edit"
        else {
            binding.tvTitle.text = "Edit Student Form"
            binding.btnSubmit.text = "Edit Student"

            // populate the document id text box with the document id
            // for the item we want to update

            // remember its possible for the docId to be null
            if (docId == "") {
                Log.d("TESTING", "ERROR: Document id is empty...")
                return
            }

            // show the document id in the UI for debugging purposes,
            // but don't let the user modify it

            binding.etDocId.setText(docId)
            binding.etDocId.isEnabled = false

            // get the data
            getDocument(docId)
        }











        // click handler
        binding.btnSubmit.setOnClickListener {
            if (isAdding == true) {
                addStudent()
            } else {
                updateStudent()
            }
            // go back to previous screen
            finish()
        }
    }

    fun getDocument(documentIdToFind:String) {

        // TODO: Get the document and populate the form fields
    }

    fun addStudent() {

        // get values from form
        val nameFromUI:String = binding.etName.text.toString()
        val gpaFromUI:Double = binding.etGpa.text.toString().toDouble()
        val hasCarFromUI:Boolean = binding.swHasCar.isChecked


        // TODO: create a data structure with the data to insert



        // TODO: insert the data into the collection

    }
    fun updateStudent() {

        if (this.docId == "") {
            Log.d("TESTING", "ERROR, document id is null, cannot update")
            return
        }


        // get values from form
        val nameFromUI:String = binding.etName.text.toString()
        val gpaFromUI:Double = binding.etGpa.text.toString().toDouble()
        val hasCarFromUI:Boolean = binding.swHasCar.isChecked

        // TODO: create a data structure with the data to insert

        // TODO: update the document with the specified id



    }

}