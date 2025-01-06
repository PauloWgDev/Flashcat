package com.example.ultimate_flashcard_app.data.import_export

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.ultimate_flashcard_app.data.Entities.Flashcard
import com.example.ultimate_flashcard_app.data.Entities.FlashcardGroup
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter

class Export {

    @SuppressLint("InlinedApi")
    fun exportGroup(context: Context, group: FlashcardGroup, flashcards: List<Flashcard>) {
        val fileName = "${group.title}_export.txt"
        val file: File?

        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
            }
            val resolver = context.contentResolver
            val contentUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val uri = resolver.insert(contentUri, contentValues)

            if (uri == null) {
                Log.e(TAG, "Failed to create file URI")
                return
            }

            val outputStream = resolver.openOutputStream(uri)
            if (outputStream == null) {
                Log.e(TAG, "Failed to open output stream")
                return
            }

            val writer = OutputStreamWriter(outputStream)

            // Write group details
            writer.write("${group.title}▲${group.description}▼\n")

            // Write flashcards
            for (flashcard in flashcards) {
                // Escape commas in title and content
                val escapedTitle = flashcard.title
                val escapedContent = flashcard.content
                writer.write("$escapedTitle▲$escapedContent▼\n")
            }

            writer.flush()
            writer.close()

            // Export successful
            val exportedFile = File(uri.toString())
            println("File exported successfully to: ${exportedFile.absolutePath}")
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle IOException
        }
    }
}
