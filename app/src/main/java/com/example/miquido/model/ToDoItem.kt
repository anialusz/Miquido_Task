package com.example.miquido.model

import java.io.Serializable

data class ToDoItem(
    var id: String = "",
    var icon: String = "",
    var title: String = "",
    var description: String = "",
    val timestamp: String = ""
) : Serializable
