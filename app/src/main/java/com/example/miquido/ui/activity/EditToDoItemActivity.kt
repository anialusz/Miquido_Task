package com.example.miquido.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.miquido.dagger.AppComponent
import com.example.miquido.databinding.ActivityCreateToDoItemBinding
import com.example.miquido.viewModel.AddToDoItemViewModel
import java.util.*

class EditToDoItemActivity : AppCompatActivity() {

    private val id = "id"
    private val title = "title"
    private val description = "description"
    private val icon = "icon"
    private val timestamp = "timestamp"
    private lateinit var binding: ActivityCreateToDoItemBinding
    private lateinit var addToDoItemViewModel: AddToDoItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = AppComponent.Manager.init()
        addToDoItemViewModel =
            appComponent.viewModelFactory.create(AddToDoItemViewModel::class.java)

        binding = ActivityCreateToDoItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etTitle.setText(intent.getStringExtra(title))
        binding.etDescription.setText(intent.getStringExtra(description))
        binding.etImageUrl.setText(intent.getStringExtra(icon))

        binding.btnAddItem.setOnClickListener {
            addToDoItemViewModel.addOrUpdateItem(
                createToDoItemToSend(),
                intent.getStringExtra(id)
            )
            finish()
        }

        addToDoItemViewModel.operationSuccess.observe(this) {
            if (it) {
                Toast.makeText(this, "Item added", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Problem occurred", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createToDoItemToSend(): Map<String, String> {
        return hashMapOf(
            icon to binding.etImageUrl.text.toString(),
            title to binding.etTitle.text.toString(),
            description to binding.etDescription.text.toString(),
            timestamp to Calendar.getInstance().time.toString()
        )
    }
}