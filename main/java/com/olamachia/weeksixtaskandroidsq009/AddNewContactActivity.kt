package com.olamachia.weeksixtaskandroidsq009

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.olamachia.weeksixtaskandroidsq009.Validator.emailValidate
import com.olamachia.weeksixtaskandroidsq009.Validator.mobileValidate
import kotlinx.android.synthetic.main.activity_add_new_contact.*

class AddNewContactActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_contact)

        // function to validate email and number
        validateEmail()
        validatePhoneNumber()

        // instantiate firebase database
        database = FirebaseDatabase.getInstance().reference

        val key = intent.getStringExtra("KEY")
        if (key == null) { saveContact() } else { saveEditedContact() }
    }

    // function to save contact
    private fun saveContact() {
        findViewById<Button>(R.id.save_contact_button).setOnClickListener {
            val contactName = findViewById<EditText>(R.id.idEdtName).text.toString()
            val contactPhoneNumber = idEdtPhoneNumber.text.toString()
            val contactEmail = idEdtEmail.text.toString()
            val contact = NewContactModel(newContactName = contactName, newContactEmail = contactEmail, newContactPhoneNumber = contactPhoneNumber)

            contact.id = database.child("Users").push().key
            database.child("Users").child(contact.id!!).setValue(contact)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            onBackPressed()
            finish()
        }
    }

    // function to save edited contact
    private fun saveEditedContact() {
        val contactName = intent.getStringExtra("ContactName")
        val contactPhoneNumber = intent.getStringExtra("ContactPhoneNumber")
        val contactEmail = intent.getStringExtra("ContactsEmail")
        val id = intent.getStringExtra("ID")

        idEdtName.setText(contactName)
        idEdtPhoneNumber.setText(contactPhoneNumber)
        idEdtEmail.setText(contactEmail)

        // onclick listener to the save_contact
        save_contact_button.setOnClickListener {
            val editedName = idEdtName.text.toString().trim()
            val editedPhoneNumber = idEdtPhoneNumber.text.toString().trim()
            val editedEmail = idEdtEmail.text.toString().trim()

            if (editedName.isEmpty() && editedPhoneNumber.isEmpty() && editedEmail.isEmpty()) {
                Toast.makeText(applicationContext, "please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }

            if (editedName.isNotEmpty() && editedPhoneNumber.isNotEmpty() && editedEmail.isNotEmpty()) {

                database.child("contacts").child(id!!).setValue(
                    NewContactModel(
                        id = id,
                        newContactName = editedName,
                        newContactPhoneNumber = editedPhoneNumber,
                        newContactEmail = editedEmail
                    )
                )

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    // validate email function
    private fun validateEmail() {
        // adds a listener to the email and validates on text changed
        idEdtEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (emailValidate(idEdtEmail.text.toString())) {
                    save_contact_button.isEnabled = true
                } else {
                    idEdtEmail.error = "Enter a valid Email"
                    save_contact_button.isEnabled = false
                }
            }
        })
    }

    // validate phoneNumber function
    private fun validatePhoneNumber() {
        // add a listener to the phoneNumber edit text to perform validation on text changed
        idEdtPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (mobileValidate(idEdtPhoneNumber.text.toString())) {
                    save_contact_button.isEnabled = true
                } else {
                    idEdtPhoneNumber.error = "Enter a valid phone number"
                    save_contact_button.isEnabled = false
                }
            }
        })
    }
}
