package com.example.locale.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locale.data.db.LikedBusiness
import com.example.locale.data.db.LikedBusinessRepository
import com.example.locale.data.model.Business
import com.example.locale.network.YelpFusionRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

@HiltViewModel(assistedFactory = BusinessViewModel.Factory::class)
class BusinessViewModel @AssistedInject constructor(
    private val yelpFusionRepository: YelpFusionRepository,
    private val likedBusinessRepository: LikedBusinessRepository,
    @Assisted private val location: String // runtime assisted injection parameter
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(location: String): BusinessViewModel
    }

    private val _uiState = MutableStateFlow(BusinessScreenUiState())
    val uiState: StateFlow<BusinessScreenUiState> = _uiState.asStateFlow()

    init {
        loadLocation(location)
    }

    private fun loadLocation(location: String) = viewModelScope.launch {
        _uiState.update {
            return@update it.copy(
                location = location,
                isLoading = true,
                error = ""
            )
        }
        getBusinessesByLocationName(location)
    }

    private suspend fun getBusinessesByLocationName(location: String) {
        try {
            val businessSearchResponse = yelpFusionRepository.getBusinessesByLocationName(location)
            if(businessSearchResponse.isSuccessful) {
                businessSearchResponse.body()?.let { response ->
                    val likedBusinesses = likedBusinessRepository.getAll()
                    val updatedBusinesses = updatedBusinessesWithLikes(
                        response.businesses,
                        likedBusinesses
                    )
                    _uiState.update {
                        return@update it.copy(
                            isLoading = false,
                            businesses = updatedBusinesses,
                            error = ""
                        )
                    }
                }
            } else {
                _uiState.update {
                    return@update it.copy(
                        isLoading = false,
                        error = "${businessSearchResponse.code()} - ${businessSearchResponse.errorBody().toString()}"
                    )
                }
            }
        } catch (e: HttpException) {
            _uiState.update {
                return@update it.copy(
                    isLoading = false,
                    error = "Error - HTTP Exception"
                )
            }
        } catch (e: Throwable) {
            _uiState.update {
                return@update it.copy(
                    isLoading = false,
                    businesses = listOf(),
                    error = "Error - ${e.message}"
                )
            }
        }
    }

    /**
     * Business like refreshing - liking cannot be done on this screen
     */

    // Updates current UIState Businesses list with correctly attributed liked status
    fun refreshLikedBusinesses() = viewModelScope.launch {
        val likedBusinesses = likedBusinessRepository.getAll()
        _uiState.update {
            val updatedBusinesses = updatedBusinessesWithLikes(
                it.businesses,
                likedBusinesses
            )
            return@update it.copy(
                businesses = updatedBusinesses
            )
        }
    }

    // Helper function to return a new list of Businesses with correctly attributed liked status
    private fun updatedBusinessesWithLikes(
        businesses: List<Business>,
        likedBusinesses: List<LikedBusiness>
    ): List<Business> {
        return businesses.map {  business ->
            if (likedBusinesses.contains(LikedBusiness(business.id))) {
                business.copy(liked = true)
            } else {
                business
            }
        }
    }
}

data class BusinessScreenUiState(
    val location: String = "",
    val isLoading: Boolean = false,
    val businesses: List<Business> = listOf(),
    val error: String? = null
)