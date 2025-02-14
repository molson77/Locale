package com.example.locale.ui.viewmodels

import dagger.assisted.AssistedFactory

@AssistedFactory
interface BusinessDetailsViewModelFactory {
    fun create(id: String): BusinessDetailsViewModel
}