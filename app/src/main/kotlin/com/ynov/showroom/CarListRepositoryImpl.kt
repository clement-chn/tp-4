package com.ynov.showroom

import javax.inject.Inject

class CarListRepositoryImpl @Inject constructor(
    private val carApi: CarApi,
    private val carDao: CarDao
) : CarListRepository {

    override suspend fun getCars(): List<Car> {
        val carsFromDb = carDao.getAllCars()

        return if (carsFromDb.isNotEmpty()) {
            carsFromDb
        } else {
            val carsFromApi = carApi.getCars()
            insertCars(carsFromApi)  // Insertion dans Room
            carsFromApi
        }
    }

    override suspend fun insertCars(cars: List<Car>) {
        carDao.insertAll(cars)
    }
}
