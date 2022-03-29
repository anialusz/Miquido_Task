package com.example.miquido.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.miquido.repository.FirebaseRepository
import javax.inject.Inject

class AddToDoItemViewModel @Inject constructor() : ViewModel() {
    private var firebaseRepository = FirebaseRepository()

    val operationSuccess = MutableLiveData<Boolean>()

    fun addOrUpdateItem(mapOfToDoFields: Map<String, String>, id: String?) {
        if (id != null) {
            firebaseRepository.updateItem(mapOfToDoFields, id)
                .addOnSuccessListener {
                    operationSuccess.postValue(true)
                }
                .addOnFailureListener { e ->
                    operationSuccess.postValue(false)
                    Log.w("Firebase", "Error adding document", e)
                }
        } else {
            firebaseRepository.addItem(mapOfToDoFields)
                .addOnSuccessListener {
                    operationSuccess.postValue(true)
                }
                .addOnFailureListener { e ->
                    operationSuccess.postValue(false)
                    Log.w("Firebase", "Error adding document", e)
                }
        }

    }
}
