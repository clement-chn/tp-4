package com.ynov.showroom

data class Car(
    val make: String,
    val model: String,
    val year: Int,
    val picture: String,
    val equipments: List<String>?
)