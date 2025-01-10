package com.ynov.showroom

import javax.inject.Inject

class FilterCarListUseCase @Inject constructor() {
    operator fun invoke(cars: List<Car>, year: Int): List<Car> {
        return if (year == 0) {
            cars
        } else {
            cars.filter { it.year == year }
        }
    }
}