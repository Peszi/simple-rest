package com.srest.framework.util

import java.io.FileReader

object FileReader {

    fun loadFileText(path: String): String {
        val htmlReader = FileReader("application/web/$path")
        var webPage = ""; htmlReader.readLines().forEach { webPage += it }
        htmlReader.close(); return webPage;
    }
}