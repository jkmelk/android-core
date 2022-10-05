package com.yt.utils.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


fun Activity.bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? {
    var file: File? = null
    return try {
        file = getExternalFilesDir(null)
        file?.createNewFile()
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, bos)
        val bitmapdata = bos.toByteArray()
        val fos = FileOutputStream(file)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        file
    } catch (e: Exception) {
        e.printStackTrace()
        file
    }
}

fun Activity.persistImage(bitmap: Bitmap, name: String): File? {
    val filesDir: File = filesDir
    val imageFile = File(filesDir, "$name.jpg")
    val os: OutputStream
    try {
        os = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.flush()
        os.close()
        return imageFile
    } catch (e: java.lang.Exception) {
        Log.e("UPAy", "Error writing bitmap", e)
        return null
    }
}

fun Activity.uriToMultipartBody(uri: Uri): Bitmap {
    val imageStream = contentResolver.openInputStream(uri)
    return BitmapFactory.decodeStream(imageStream)
}


fun Context.getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
    val drawable = ContextCompat.getDrawable(this, drawableId)
    val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
    drawable.draw(canvas)
    return bitmap
}

fun Bitmap.getResizedBitmap(maxSize: Int): Bitmap {
    var width = width
    var height = height
    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 1) {
        width = maxSize
        height = (width / bitmapRatio).toInt()
    } else {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(this, width, height, true)
}

