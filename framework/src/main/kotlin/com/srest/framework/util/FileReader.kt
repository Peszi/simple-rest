package com.srest.framework.util

import java.io.FileNotFoundException
import java.io.FileReader

object FileReader {

    fun loadFileText(path: String): String? {
        try {
            val htmlReader = FileReader("application/$path")
            var webPage = ""; htmlReader.readLines().forEach { webPage += it }
            htmlReader.close(); return webPage
        } catch (e: FileNotFoundException) {}
        return null
    }
}