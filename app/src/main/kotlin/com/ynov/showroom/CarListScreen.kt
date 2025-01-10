package com.ynov.showroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter

fun NavGraphBuilder.carListScreen(
    navController: NavController,
    onNavigateToCarDetails: (Car) -> Unit
) {
    composable("car_list") {
        CarListRoute(
            onNavigateToCarDetails = onNavigateToCarDetails
        )
    }
}

@Composable
private fun CarListRoute(
    onNavigateToCarDetails: (Car) -> Unit,
    viewModel: CarListViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchCars()
    }

    CarListScreen(
        viewState = viewState,
        onNavigateToCarDetails = onNavigateToCarDetails,
        onFilterCars = { viewModel.filterCars(it) }
    )
}

@Composable
fun CarListScreen(
    viewState: CarListViewState,
    onNavigateToCarDetails: (Car) -> Unit,
    onFilterCars: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cars = viewState.cars
    val isLoading = viewState.isLoading
    val error = viewState.error
    val years = cars.map { it.year }.distinct().sorted()
    var selectedYear by remember { mutableStateOf<Int?>(null) }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Showroom",
            style = TextStyle(fontSize = 30.sp),
            modifier = Modifier
                .padding(42.dp)
                .align(Alignment.CenterHorizontally)
        )

        Box(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = selectedYear?.toString() ?: "",
                onValueChange = {},
                label = { Text("Filter By year") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                readOnly = true
            )

            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Reset Filter") },
                    onClick = {
                        selectedYear = null
                        expanded = false
                        onFilterCars(0)  // Pass 0 or another value that would show all cars
                    })
                years.forEach { year ->
                    DropdownMenuItem(
                        text = { Text(text = year.toString()) },
                        onClick = {
                            selectedYear = year
                            expanded = false
                            onFilterCars(year)
                        })
                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            if (cars.isNotEmpty()) {
                val pagerState = rememberPagerState { cars.size }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) { page ->
                    CarImageView(car = cars[page])
                }

                Spacer(modifier = Modifier.height(16.dp))

                CarDescription(car = cars[pagerState.currentPage])

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onNavigateToCarDetails(cars[pagerState.currentPage]) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Go to Car Details")
                }
            } else {
                Text(
                    text = "No cars available",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        error?.let {
            Text(text = "Error: $it", color = Color.Red, modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun CarImageView(car: Car) {
    Image(
        painter = rememberAsyncImagePainter(car.picture),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun CarDescription(car: Car) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "${car.make} ${car.model}",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Year: ${car.year}",
            fontSize = 18.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        car.equipments?.let {
            Text(
                text = "Equipments:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            EquipmentList(equipments = it)
        }
    }
}

@Composable
fun EquipmentList(equipments: List<String>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(equipments.size) { index ->
            EquipmentItem(equipment = equipments[index])
        }
    }
}

@Composable
fun EquipmentItem(equipment: String) {
    Card(
        modifier = Modifier
            .wrapContentWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray.copy(alpha = 0.3f)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Check Icon",
                tint = Color.Green,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = equipment,
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarListScreenPreview() {
    val mockViewState = CarListViewState(
        cars = listOf(
            Car(
                make = "Renault",
                model = "Clio III",
                year = 2017,
                picture = "https://www.travelcar.com/api/media/20161108/vOpojibhaVoH717MoKak8IcbIdZNFjrXVA_mvw1Kr8NUE4OKIC8alCCLfLC4GxgUQVdRUu0shyOrVAfe-tPIiuQQuE9ZlGd6Jc9OwnRhVTBLrQOGFs_muLakYyAhDm3U/renault-clio-iii.jpg",
                equipments = listOf("GPS", "Climatisation", "ABS", "Airbags")
            ),
            Car(
                make = "Citroen",
                model = "C1 II",
                year = 2003,
                picture = "https://www.travelcar.com/api/media/20170911/MDkWGH4LlpEY0U1SsYGPMYa_tKKMp_GJwfHZJFtKBOYk2h9Lo8OmI1VJCZHGe_TY0oeHsbcWAdrbkD8rX43sOg6-E6EarDkn5-3jTF4EFVH81kWnqW11WIS_JtPgSAzg/citroen-c-1-verte-trip-n-drive.png",
                equipments = listOf("GPS", "Climatisation")
            )
        ),
        isLoading = false,
        error = null
    )

    CarListScreen(
        viewState = mockViewState,
        onNavigateToCarDetails = {},
        onFilterCars = {}
    )
}
