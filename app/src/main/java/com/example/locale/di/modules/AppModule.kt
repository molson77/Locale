package com.example.locale.di.modules

import android.content.Context
import androidx.room.Room
import com.example.locale.Constant
import com.example.locale.data.db.LikedBusinessDatabase
import com.example.locale.network.AuthInterceptor
import com.example.locale.network.YelpFusionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideYelpFusionBaseUrl(): String {
        return Constant.YELP_FUSION_BASE_URL
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideLoggingInterceptor())
            .addInterceptor(provideAuthInterceptor())
            .build()
    }

    @Provides
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor(Constant.YELP_FUSION_API_KEY)
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .client(provideOkHttpClient())
            .baseUrl(provideYelpFusionBaseUrl())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    fun provideYelpFusionService(retrofit: Retrofit) : YelpFusionService {
        return retrofit.create(YelpFusionService::class.java)
    }

    @Singleton
    @Provides
    fun provideLikedBusinessDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        LikedBusinessDatabase::class.java,
        "liked_business"
    ).build()

    @Singleton
    @Provides
    fun provideLikedBusinessDao(
        db: LikedBusinessDatabase
    ) = db.likedBusinessDao()

}