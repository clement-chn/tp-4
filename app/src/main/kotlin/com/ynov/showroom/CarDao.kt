package com.ynov.showroom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CarDao {
    @Insert
    suspend fun insertAll(cars: List<Car>)

    @Query("SELECT * FROM cars")
    suspend fun getAllCars(): List<Car>

    @Query("SELECT * FROM cars WHERE id = :id")
    suspend fun getCarById(id: Int): Car?

    @Update
    suspend fun updateCar(car: Car)
}
