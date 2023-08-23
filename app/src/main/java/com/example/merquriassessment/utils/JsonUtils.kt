package com.example.merquriassessment.utils

import android.content.Context
import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader

class JsonUtils {
    fun file2Json(context: Context, fileName: String): String? {
        val stringBuilder = StringBuilder()
        try {
            val assetManager: AssetManager = context.assets
            val file = InputStreamReader(assetManager.open(fileName))
            val bf = BufferedReader(file)
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            bf.close()
            file.close()
            return stringBuilder.toString()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}