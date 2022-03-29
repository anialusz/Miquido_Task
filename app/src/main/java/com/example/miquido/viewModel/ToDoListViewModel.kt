package com.example.miquido.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.miquido.model.ToDoItem
import com.example.miquido.repository.FirebaseRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class ToDoListViewModel @Inject constructor() : ViewModel() {
    var savedToDoList: MutableLiveData<List<ToDoItem>> = MutableLiveData()
    private var firebaseRepository = FirebaseRepository()
    private var lastVisible: DocumentSnapshot? = null
    private var registeredSnapshotListener: ListenerRegistration? = null

    fun initializeToDoList() {
        firebaseRepository.getStartingToDoItems()
            .get().addOnSuccessListener { value ->
                savedToDoList.value = createTemporaryToDoList(value)
                if (value.size() > 0) {
                    this.lastVisible = value?.documents?.get(value.size() - 1)
                }
            }.addOnFailureListener {
                Log.e("Exception", it.message.toString())
            }
    }

    fun addNextPageToToDoList() {
        firebaseRepository.getNextBatchOfToDoItems(this.lastVisible!!).get()
            .addOnSuccessListener { value ->

                if (!value?.documents.isNullOrEmpty() && !value.metadata.hasPendingWrites()) {
                    val temporarySavedToDoList = createTemporaryToDoList(value)

                    val temporaryListWithAllValues: List<ToDoItem> =
                        savedToDoList.value!!.toMutableList()
                            .plus(temporarySavedToDoList)
                    savedToDoList.value = temporaryListWithAllValues

                    this.lastVisible = value.documents[value.documents.size - 1]
                }
            }.addOnFailureListener {
                Log.e("Exception", it.message.toString())
            }
    }

    fun provideLifeUpdatesForAllData(dataSize: Long) {
        if (registeredSnapshotListener == null) {
            registeredSnapshotListener = firebaseRepository.provideLifeUpdatesForAllData(dataSize)
                .addSnapshotListener { value, _ ->
                    val temporaryToDoList = createTemporaryToDoList(value)

                    if (temporaryToDoList != savedToDoList.value) {
                        savedToDoList.value = temporaryToDoList
                    }
                }
        }
    }

    fun deactivateOldSnapshotListener() {
        registeredSnapshotListener?.remove()
        registeredSnapshotListener = null
    }

    fun removeToDoItem(id: String) {
        firebaseRepository.deleteToDoItem(id)
    }

    private fun createTemporaryToDoList(value: QuerySnapshot?): List<ToDoItem> {
        val savedToDosList: MutableList<ToDoItem> = mutableListOf()

        if (value != null) {
            for (item in value) {
                val todoItem: ToDoItem = item.toObject(ToDoItem::class.java)
                todoItem.id = item.id
                savedToDosList.add(todoItem)
            }
        }
        return savedToDosList
    }
}