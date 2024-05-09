package com.igd.snaplingo.ui.screen.snap.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import androidx.camera.core.ImageProxy
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


fun ImageProxy.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    // Decode the image with EXIF orientation information
    val options = BitmapFactory.Options()
    options.inSampleSize = 1
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)

    // Check and handle EXIF orientation
    val exif = ExifInterface(bytes.inputStream())
    val orientation =
        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
    val rotatedBitmap = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
        else -> bitmap
    }

    return rotatedBitmap
}

// Rotate the bitmap to the desired orientation
private fun rotateBitmap(source: Bitmap, degrees: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees.toFloat())
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}

fun getRealPathFromURI(context: Context, contentUri: Uri?): String {
    var cursor: Cursor? = null
    try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    } finally {
        cursor?.close()
    }
}

private const val MAXIMAL_SIZE = 1000000

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 50
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}