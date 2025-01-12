package com.ynov.showroom

interface CarListRepository {
    suspend fun getCars(): List<Car>
    suspend fun insertCars(cars: List<Car>)
}
