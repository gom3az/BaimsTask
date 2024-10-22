package com.baimstask.data.local

import java.io.IOException
import java.io.InputStream

fun interface AssetManager {
    @Throws(IOException::class)
    fun open(fileName: String): InputStream
}