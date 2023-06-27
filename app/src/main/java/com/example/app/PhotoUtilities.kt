package com.example.app

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.yagmurerdogan.toasticlib.Toastic
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun saveImage(contentResolver: ContentResolver, capturedImageUri: Uri): String {
    val bitmap = getBitmap(capturedImageUri, contentResolver)

    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    values.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${SystemClock.uptimeMillis()}")

    val imageUri =
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    val outputStream = imageUri?.let { contentResolver.openOutputStream(it) }
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream?.close()

    return imageUri.toString()
}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}

private fun getBitmap(selectedPhotoUri: Uri, contentResolver: ContentResolver): Bitmap {
    val bitmap = when {
        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
            contentResolver,
            selectedPhotoUri
        )
        else -> {
            val source = ImageDecoder.createSource(contentResolver, selectedPhotoUri)
            ImageDecoder.decodeBitmap(source)
        }
    }
    return bitmap
}

fun showToast(context: Context) {
    val activity = context as Activity
    val toast = Toastic.toastic(
        context = activity,
        message = "Nuovo Badge Ottenuto",
        duration = Toastic.LENGTH_SHORT,
        type = Toastic.DEFAULT,
        isIconAnimated = true,
        customIcon = R.drawable.logo_icon,
        customIconAnimation = com.yagmurerdogan.toastic.R.anim.pulse,
        customBackground = R.drawable.toast_bg,
        textColor = Color(0xFFE65B00).toArgb()
    )
    toast.setGravity(Gravity.TOP, 0, 300)
    toast.show()
}