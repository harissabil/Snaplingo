package com.igd.snaplingo.ui.screen.home.util

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun CameraPositionState.centerOnLocation(
    location: LatLng,
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(location, 17.5f),
    durationMs = 500
)

fun getExpandedBounds(currentLocationState: LatLng, expansionFactor: Double = 0.005): LatLngBounds {
    // Adjust the expansion factor according to your needs
    val southwest = LatLng(
        currentLocationState.latitude - expansionFactor,
        currentLocationState.longitude - expansionFactor
    )
    val northeast = LatLng(
        currentLocationState.latitude + expansionFactor,
        currentLocationState.longitude + expansionFactor
    )
    return LatLngBounds.Builder()
        .include(southwest)
        .include(northeast)
        .build()
}

suspend fun loadBitmapDescriptorFromUrl(context: Context, imageUrl: String): BitmapDescriptor {
    return withContext(Dispatchers.IO) {
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .circleCrop()
            .submit()
            .get()
    }
        .let { bitmap ->
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 130, 130, false)
            BitmapDescriptorFactory.fromBitmap(resizedBitmap)
        }
}