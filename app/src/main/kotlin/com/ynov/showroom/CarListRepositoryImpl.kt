package com.ynov.showroom

import javax.inject.Inject

class CarListRepositoryImpl @Inject constructor(private val carApi: CarApi) : CarListRepository {
    override suspend fun getCars(): List<Car> = carApi.getCars()
}