package com.ynov.showroom

data class CarListViewState(
    val cars: List<Car> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)