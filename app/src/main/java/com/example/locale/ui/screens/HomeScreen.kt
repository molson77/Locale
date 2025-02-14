package com.example.locale.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.locale.R
import com.example.locale.data.LocaleCity
import com.example.locale.data.LosAngeles
import com.example.locale.data.Miami
import com.example.locale.data.NewYorkCity
import com.example.locale.data.Philadelphia
import com.example.locale.data.SanFrancisco

@Composable
fun HomeScreen(
    onCityClicked: (LocaleCity) -> Unit
) {
    val cities = listOf(
        NewYorkCity,
        LosAngeles,
        Philadelphia,
        Miami,
        SanFrancisco
    )

    HomeScreenContent(cities = cities) {
        onCityClicked.invoke(it)
    }
}

@Composable
fun HomeScreenContent(
    cities: List<LocaleCity>,
    onCityClicked: (LocaleCity) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(Modifier.fillMaxSize()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35F)
            ) {
                Logo()
            }
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.city_prompt),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                        fontSize = TextUnit(16F, TextUnitType.Sp),
                        textAlign = TextAlign.Center,
                    )
                    CityList(cities = cities) {
                        onCityClicked.invoke(it)
                    }
                }
            }
        }
    }
}

@Composable
fun CityList(
    cities: List<LocaleCity>,
    onCityClicked: (LocaleCity) -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(cities) {
            CitySelection(city = it) { city ->
                onCityClicked.invoke(city)
            }
        }
    }
}

@Composable
fun CitySelection(
    city: LocaleCity,
    onCityClicked: (LocaleCity) -> Unit
) {
    Text(
        text = city.name,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        fontSize = TextUnit(26F, TextUnitType.Sp),
        textAlign = TextAlign.Center,
        modifier = Modifier.clickable {
            onCityClicked.invoke(city)
        }
    )
}

@Composable
fun Logo(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(65.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_business_24),
            contentDescription = "Locale logo",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(45.dp)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(45F, TextUnitType.Sp),
            textAlign = TextAlign.Left
        )
    }
}