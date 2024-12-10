package com.tms.targetedmoneysaver.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import androidx.core.content.FileProvider
import com.tms.targetedmoneysaver.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmm"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

// get Image uri for android API > 29 (API > Q)
fun getImageUri(context: Context): Uri {
    var uri: Uri? = null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }

        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        // content://media/external/images/media/1000000062 (Content URI)
        // storage/emulated/0/Pictures/MyCamera/20230825_155303.jpg (FilePath)
    }
    return uri ?: getImageUriForPreQ(context)
}

// get image uri for Android API < 29 (API < Q)
fun getImageUriForPreQ(context: Context): Uri {
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")

    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.fileprovider",
        imageFile
    )
    //content://package_name.fileprovider/my_images/MyCamera/20230825_133659.jpg
}


fun uriToBase64(contentResolver: ContentResolver, uri: Uri): String? {
    return try {
        // Open the input stream for the given URI
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        if (inputStream != null) {
            // Convert input stream to a byte array
            val byteArray = inputStreamToByteArray(inputStream)
            // Encode byte array to Base64
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun inputStreamToByteArray(inputStream: InputStream): ByteArray {
    val buffer = ByteArrayOutputStream()
    val data = ByteArray(1024)
    var bytesRead: Int
    while (inputStream.read(data).also { bytesRead = it } != -1) {
        buffer.write(data, 0, bytesRead)
    }
    return buffer.toByteArray()
}