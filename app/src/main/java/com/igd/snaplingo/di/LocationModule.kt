package com.igd.snaplingo.di

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.igd.snaplingo.core.location.ILocationService
import com.igd.snaplingo.core.location.LocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Singleton
    @Provides
    fun provideLocationClient(
        @ApplicationContext context: Context
    ): ILocationService = LocationService(
        context,
        LocationServices.getFusedLocationProviderClient(context)
    )
}