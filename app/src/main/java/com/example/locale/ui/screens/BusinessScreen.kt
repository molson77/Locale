package com.example.locale.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.locale.R
import com.example.locale.data.model.Business
import com.example.locale.data.model.Location
import com.example.locale.ui.theme.LocaleTheme
import com.example.locale.ui.viewmodels.BusinessScreenUiState
import com.example.locale.ui.viewmodels.BusinessViewModel

@Composable
fun BusinessScreen(
    location: String,
    onBusinessClicked: (Business) -> Unit,
    onBackClicked: () -> Unit
) {

    val viewModel: BusinessViewModel = hiltViewModel<BusinessViewModel, BusinessViewModel.Factory>(
        creationCallback = { factory -> factory.create(location = location) }
    )
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = null) {
        viewModel.refreshLikedBusinesses()
    }

    BusinessScreenContent(
        uiState = uiState.value,
        onBusinessClicked = {
            onBusinessClicked.invoke(it)
        },
        onBackClicked = {
            onBackClicked.invoke()
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun BusinessScreenContent(
    uiState: BusinessScreenUiState,
    onBusinessClicked: (Business) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        if(uiState.isLoading) {
            LoadingScreen()
        } else if(!uiState.error.isNullOrEmpty()) {
            ErrorScreen(description = uiState.error)
        } else {
            BusinessesList(
                location = uiState.location,
                businesses = uiState.businesses,
                onBusinessClicked = {
                    onBusinessClicked.invoke(it)
                }
            )
        }
        BackButtonOverlay {
            onBackClicked()
        }
    }
}

@Composable
fun BusinessesList(
    location: String,
    businesses: List<Business>,
    onBusinessClicked: (Business) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 18.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.whats_around),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    fontSize = TextUnit(16F, TextUnitType.Sp),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = location,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(26F, TextUnitType.Sp),
                    textAlign = TextAlign.Center,
                )
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(businesses) {
                BusinessItem(
                    business = it,
                    onBusinessClick = { business ->
                        onBusinessClicked(business)
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun BusinessItem(
    business: Business,
    onBusinessClick: (Business) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.clickable {
            onBusinessClick.invoke(business)
        }
    ) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(140.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(business.imageUrl)
                        .build(),
                    onError = {
                              Log.d("Coil", it.result.toString())
                    },
                    contentDescription = "Business Image",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 24.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(0.7F)
                ) {
                    Text(
                        text = business.name,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(18F, TextUnitType.Sp),
                        lineHeight = TextUnit(17F, TextUnitType.Sp),
                        textAlign = TextAlign.Left,
                        maxLines = 3,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )
                }
                Box(
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    if(business.liked) {
                        Icon(
                            painter = painterResource(id = R.drawable.like),
                            contentDescription = "Liked",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.like),
                            contentDescription = "Not Liked",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun ErrorScreen(description: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.error),
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = description,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                fontSize = TextUnit(18F, TextUnitType.Sp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun BackButtonOverlay(
    onBackClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 24.dp, bottom = 24.dp)
    ) {
        FloatingActionButton(
            onClick = { onBackClicked() },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Preview
@Composable
fun BusinessItemPreview() {
    LocaleTheme {
        Column(Modifier.fillMaxSize()) {
            BusinessItem(
                business = Business(
                    id = "",
                    alias = "",
                    name = "Zahav",
                    imageUrl = "https://s3-media2.fl.yelpcdn.com/bphoto/it0vY1UBcjXyIDbg54c0vQ/o.jpg",
                    isClosed = false,
                    url = "",
                    reviewCount = null,
                    categories = null,
                    rating = "4.5",
                    coordinates = null,
                    transactions = null,
                    price = null,
                    location = Location(
                        address1="237 St James Pl",
                        address2 = "",
                        address3 = "",
                        city = "Philadelphia",
                        zipCode = "19106",
                        country = "US",
                        state = "PA",
                        displayAddress = listOf(
                            "237 St James Pl",
                            "Philadelphia, PA 19106"
                        ),
                        crossStreets = ""),
                    phone = "+12156258800",
                    businessHours = null,
                    displayPhone = "(215) 625-8800",
                    distance = ""
                ),
                onBusinessClick = {}
            )
        }
    }
}