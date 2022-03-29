package com.example.miquido.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FirebaseRepository {

    private val collectionName = "todoItems"
    private val collectionOrderBy = "title"
    private var firestoreDb = FirebaseFirestore.getInstance()

    fun addItem(todoItem: Map<String, String>): Task<DocumentReference> {
        return firestoreDb.collection(collectionName)
            .add(todoItem)
    }

    fun updateItem(todoItem: Map<String, String>, id: String): Task<Void> {
        return firestoreDb.collection(collectionName).document(id)
            .set(todoItem)
    }

    fun getStartingToDoItems(): Query {
        return firestoreDb.collection(collectionName).orderBy(collectionOrderBy).limit(30)
    }

    fun getNextBatchOfToDoItems(lastVisible: DocumentSnapshot): Query {
        return firestoreDb.collection(collectionName).orderBy(collectionOrderBy)
            .startAfter(lastVisible)
            .limit(30)
    }

    fun provideLifeUpdatesForAllData(numberOfItems: Long): Query {
        return firestoreDb.collection(collectionName)
            .orderBy(collectionOrderBy).limit(numberOfItems)
    }

    fun deleteToDoItem(id: String) {
        firestoreDb.collection(collectionName).document(id)
            .delete()
    }
}