package com.example.locale.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.locale.R
import com.example.locale.data.model.Business
import com.example.locale.data.model.BusinessDetails
import com.example.locale.data.model.Category
import com.example.locale.data.model.Coordinates
import com.example.locale.data.model.Location
import com.example.locale.ui.theme.LocaleTheme
import com.example.locale.ui.viewmodels.BusinessDetailsScreenUiState
import com.example.locale.ui.viewmodels.BusinessDetailsViewModel
import com.example.locale.ui.viewmodels.BusinessScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun BusinessDetailsScreen(
    id: String,
    onBackClicked: () -> Unit
) {

    val viewModel = hiltViewModel<BusinessDetailsViewModel, BusinessDetailsViewModel.Factory>(
        creationCallback = { factory -> factory.create(id = id) }
    )

    // TODO: Fix uistate collection with assisted injection
    val uiState = viewModel.uiState.collectAsState()

    BusinessDetailsScreenContent(
        uiState = uiState.value,
        onBusinessLiked = {
            viewModel.likeBusiness(it)
        },
        onBusinessUnliked = {
            viewModel.unlikeBusiness(it)
        },
        onBackClicked = {
            onBackClicked()
        }
    )
}

@Composable
fun BusinessDetailsScreenContent(
    uiState: BusinessDetailsScreenUiState,
    onBusinessLiked: (BusinessDetails) -> Unit,
    onBusinessUnliked: (BusinessDetails) -> Unit,
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
        } else if(uiState.businessDetails == null) {
            ErrorScreen(description = "Error - No Business Found")
        } else {
            BusinessDetails(
                businessDetails = uiState.businessDetails,
                onBusinessLiked = {
                    onBusinessLiked.invoke(it)
                },
                onBusinessUnliked = {
                    onBusinessUnliked.invoke(it)
                }
            )
        }
        BackButtonOverlay {
            onBackClicked()
        }
    }
}

@Composable
fun BusinessDetails(
    businessDetails: BusinessDetails,
    onBusinessLiked: (BusinessDetails) -> Unit,
    onBusinessUnliked: (BusinessDetails) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(0.38F)
        ) {
            AsyncImage(
                model = businessDetails.imageUrl,
                contentDescription = "Business Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(36.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(18.dp)))
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth(0.75F)) {
                Text(
                    text = businessDetails.name,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(26F, TextUnitType.Sp),
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            Box(
                contentAlignment = Alignment.CenterEnd,
            ) {
                if(businessDetails.liked) {
                    Icon(
                        painter = painterResource(id = R.drawable.like),
                        contentDescription = "Liked",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            onBusinessUnliked.invoke(businessDetails)
                        }
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.like),
                        contentDescription = "Not Liked",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.clickable {
                            onBusinessLiked.invoke(businessDetails)
                        }
                    )
                }
            }
        }
        Box(modifier = Modifier
            .padding(vertical = 14.dp)
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rated: ${businessDetails.rating.toString()}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(16F, TextUnitType.Sp),
                    textAlign = TextAlign.Left,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "Star",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Box(modifier = Modifier
            .padding(vertical = 14.dp)
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "Categories: ${businessDetails.categories?.joinToString(", ")}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(16F, TextUnitType.Sp),
                    textAlign = TextAlign.Left,
                    //lineHeight = TextUnit(13F, TextUnitType.Sp),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
        Box(modifier = Modifier
            .padding(vertical = 14.dp)
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = businessDetails.location?.displayAddress?.joinToString(", ") ?: "",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(16F, TextUnitType.Sp),
                    textAlign = TextAlign.Left,
                    //lineHeight = TextUnit(13F, TextUnitType.Sp),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}