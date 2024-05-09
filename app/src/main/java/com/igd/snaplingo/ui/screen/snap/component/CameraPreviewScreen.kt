package com.igd.snaplingo.ui.screen.snap.component

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import com.igd.snaplingo.R
import com.igd.snaplingo.ui.screen.snap.util.getRealPathFromURI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraPreviewScreen(
    onImageCapturedSuccess: (file: File) -> Unit,
) {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture = remember {
        ImageCapture.Builder().apply {
            setJpegQuality(30)
        }
            .build()
    }

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
        Icon(
            painter = painterResource(id = R.drawable.four_corner),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
        )
        Box(
            modifier = Modifier
                .padding(32.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                shape = CircleShape,
                containerColor = Color.White,
                contentColor = Color.Gray,
                onClick = {
                    captureImage(
                        imageCapture = imageCapture,
                        context = context,
                        onImageCapturedSuccess = { onImageCapturedSuccess(it) }
                    )
                }) {}
        }
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

private fun captureImage(
    imageCapture: ImageCapture,
    context: Context,
    onImageCapturedSuccess: (file: File) -> Unit,
) {
    val name = "${System.currentTimeMillis()}.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Snaplingo")
        }
    }
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .setMetadata(ImageCapture.Metadata())
        .build()
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val file = outputFileResults.savedUri?.let { uri ->
                    File(getRealPathFromURI(context, uri))
                }
                CoroutineScope(Dispatchers.IO).launch {
                    rotateImageCorrectly(file!!)
                    onImageCapturedSuccess(file)
                }
                Timber.d("Image saved successfully")
            }

            suspend fun rotateImageCorrectly(file: File) = withContext(Dispatchers.IO) {
                val sourceBitmap =
                    MediaStore.Images.Media.getBitmap(context.contentResolver, file.toUri())

                val exif = ExifInterface(file.inputStream())
                val rotation =
                    exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                val rotationInDegrees = when (rotation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270
                    ExifInterface.ORIENTATION_TRANSVERSE -> -90
                    ExifInterface.ORIENTATION_TRANSPOSE -> -270
                    else -> 0
                }
                val matrix = Matrix().apply {
                    if (rotation != 0) preRotate(rotationInDegrees.toFloat())
                }

                val rotatedBitmap =
                    Bitmap.createBitmap(
                        sourceBitmap,
                        0,
                        0,
                        sourceBitmap.width,
                        sourceBitmap.height,
                        matrix,
                        true
                    )

                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))

                sourceBitmap.recycle()
                rotatedBitmap.recycle()
            }

            override fun onError(exception: ImageCaptureException) {
                Timber.e(exception)
            }

        })
}