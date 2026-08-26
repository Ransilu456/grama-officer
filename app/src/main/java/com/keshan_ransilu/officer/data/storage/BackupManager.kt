package com.keshan_ransilu.officer.data.storage

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class BackupManager(private val context: Context) {

    fun exportBackup(outputFile: File): Boolean {
        val registerDir = File(context.filesDir, "registers")
        if (!registerDir.exists() || !registerDir.isDirectory) return false

        return try {
            ZipOutputStream(FileOutputStream(outputFile)).use { zipOut ->
                registerDir.listFiles()?.forEach { file ->
                    if (file.isFile) {
                        FileInputStream(file).use { fis ->
                            val zipEntry = ZipEntry(file.name)
                            zipOut.putNextEntry(zipEntry)
                            fis.copyTo(zipOut)
                            zipOut.closeEntry()
                        }
                    }
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    // Import logic would be similar (unzip)
}
