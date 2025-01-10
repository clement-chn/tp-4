package com.ynov.showroom

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class FilterCarListUseCaseTest {

    private lateinit var filterCarListUseCase: FilterCarListUseCase

    @Before
    fun setUp() {
        filterCarListUseCase = FilterCarListUseCase()
    }

    @Test
    fun testFilterCarsByYear_withValidYear() {
        val car1 = Car(
            make = "Renault",
            model = "Clio III",
            year = 2017,
            picture = "url",
            equipments = listOf("GPS")
        )
        val car2 = Car(
            make = "Citroen",
            model = "C1 II",
            year = 2003,
            picture = "url",
            equipments = listOf("ABS")
        )
        val car3 = Car(
            make = "Toyota",
            model = "Corolla",
            year = 2017,
            picture = "url",
            equipments = listOf("Airbags")
        )

        val cars = listOf(car1, car2, car3)

        val filteredCars = filterCarListUseCase(cars, 2017)

        assertEquals(2, filteredCars.size)
        assertTrue(filteredCars.all { it.year == 2017 })
    }

    @Test
    fun testFilterCarsByYear_withReset() {
        val car1 = Car(
            make = "Renault",
            model = "Clio III",
            year = 2017,
            picture = "url",
            equipments = listOf("GPS")
        )
        val car2 = Car(
            make = "Citroen",
            model = "C1 II",
            year = 2003,
            picture = "url",
            equipments = listOf("ABS")
        )
        val car3 = Car(
            make = "Toyota",
            model = "Corolla",
            year = 2017,
            picture = "url",
            equipments = listOf("Airbags")
        )

        val cars = listOf(car1, car2, car3)

        val filteredCars = filterCarListUseCase.invoke(cars, 0)

        assertEquals(3, filteredCars.size)
    }

    @Test
    fun testFilterCarsByYear_withNoMatches() {
        val car1 = Car(
            make = "Renault",
            model = "Clio III",
            year = 2017,
            picture = "url",
            equipments = listOf("GPS")
        )
        val car2 = Car(
            make = "Citroen",
            model = "C1 II",
            year = 2003,
            picture = "url",
            equipments = listOf("ABS")
        )

        val cars = listOf(car1, car2)

        val filteredCars = filterCarListUseCase.invoke(cars, 2022)

        assertTrue(filteredCars.isEmpty())
    }
}
