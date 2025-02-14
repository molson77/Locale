package com.example.locale.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.locale.data.db.LikedBusinessRepository
import com.example.locale.data.model.BusinessDetails
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
import javax.inject.Inject

@HiltViewModel(assistedFactory = BusinessDetailsViewModel.Factory::class)
class BusinessDetailsViewModel @AssistedInject constructor(
    private val yelpFusionRepository: YelpFusionRepository,
    private val likedBusinessRepository: LikedBusinessRepository,
    @Assisted private val id: String
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: String): BusinessDetailsViewModel
    }

    init {
        getBusinessDetails(id)
    }

    private val _uiState = MutableStateFlow(BusinessDetailsScreenUiState())
    val uiState: StateFlow<BusinessDetailsScreenUiState> = _uiState.asStateFlow()

    fun getBusinessDetails(id: String) = viewModelScope.launch {
        if (id.isEmpty()) {
            _uiState.update {
                return@update it.copy(
                    isLoading = false,
                    error = "Error - Invalid ID"
                )
            }
        } else {
            try {
                _uiState.update {
                    return@update it.copy(
                        isLoading = true,
                        error = null
                    )
                }

                val businessDetails = yelpFusionRepository.getBusinessDetails(id)

                if (businessDetails.isSuccessful) {
                    businessDetails.body()?.let { newBusinessDetails ->
                        val likedBusinesses = likedBusinessRepository.getAll()
                        _uiState.update {
                            return@update it.copy(
                                isLoading = false,
                                businessDetails = if(likedBusinesses.any { it.id == id })
                                    newBusinessDetails.copy(liked = true) else newBusinessDetails,
                                error = null
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        return@update it.copy(
                            isLoading = false,
                            error = "${businessDetails.code()} - ${businessDetails.errorBody().toString()}"
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
                        error = "Error - ${e.message}"
                    )
                }
            }
        }
    }

    fun likeBusiness(businessDetails: BusinessDetails) = viewModelScope.launch {
        likedBusinessRepository.like(businessDetails.id)
        _uiState.update {
            return@update it.copy(
                businessDetails = businessDetails.copy(liked = true)
            )
        }
    }

    fun unlikeBusiness(businessDetails: BusinessDetails) = viewModelScope.launch {
        likedBusinessRepository.unlike(businessDetails.id)
        _uiState.update {
            return@update it.copy(
                businessDetails = businessDetails.copy(liked = false)
            )
        }
    }

}

data class BusinessDetailsScreenUiState(
    val id: String = "",
    val isLoading: Boolean = false,
    val businessDetails: BusinessDetails? = null,
    val error: String? = null
)