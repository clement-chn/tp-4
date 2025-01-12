package com.ynov.showroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class CarListViewModel @Inject constructor(
    private val carListRepository: CarListRepository,
    private val filterCarListUseCase: FilterCarListUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow(CarListViewState())
    val viewState: StateFlow<CarListViewState> = _viewState

    fun fetchCars() {
        viewModelScope.launch {
            _viewState.value = CarListViewState(isLoading = true)
            try {
                val cars = carListRepository.getCars()
                _viewState.value = CarListViewState(cars = cars, isLoading = false)
            } catch (e: Exception) {
                _viewState.value = CarListViewState(isLoading = false, error = e.message)
            }
        }
    }

    fun filterCars(year: Int) {
        viewModelScope.launch {
            _viewState.value = CarListViewState(isLoading = true)
            try {
                val cars = carListRepository.getCars()
                val filteredCars = filterCarListUseCase(cars, year)
                _viewState.value = CarListViewState(cars = filteredCars, isLoading = false)
            } catch (e: Exception) {
                _viewState.value = CarListViewState(isLoading = false, error = e.message)
            }
        }
    }

    fun addCarToDatabase(car: Car) {
        viewModelScope.launch {
            try {
                carListRepository.insertCars(listOf(car))
            } catch (e: Exception) {
                //
            }
        }
    }
}
