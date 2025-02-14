package com.example.locale.data

sealed interface LocaleCity {
    val name: String
    val latitude: Double
    val longitude: Double
}

data object NewYorkCity: LocaleCity {
    override val name: String = "New York City"
    override val latitude: Double = 40.7128
    override val longitude: Double = 74.0060
}

data object LosAngeles: LocaleCity {
    override val name: String = "Los Angeles"
    override val latitude: Double = 34.0549
    override val longitude: Double = 118.2426
}

data object Philadelphia: LocaleCity {
    override val name: String = "Philadelphia"
    override val latitude: Double = 39.9526
    override val longitude: Double = 75.1652
}

data object Miami: LocaleCity {
    override val name: String = "Miami"
    override val latitude: Double = 25.7617
    override val longitude: Double = 80.1918
}

data object SanFrancisco: LocaleCity {
    override val name: String = "San Francisco"
    override val latitude: Double = 37.7749
    override val longitude: Double = 122.4194
}