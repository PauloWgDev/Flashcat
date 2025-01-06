package com.example.ultimate_flashcard_app.data.import_export

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.ultimate_flashcard_app.dao.FlashcardDao
import com.example.ultimate_flashcard_app.dao.FlashcardGroupDao
import com.example.ultimate_flashcard_app.data.Entities.Flashcard
import com.example.ultimate_flashcard_app.data.Entities.FlashcardGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.random.Random

class Import(private val context: Context, private val flashcardDao: FlashcardDao, private val groupDao: FlashcardGroupDao) {

    fun importFLSHG(uri: Uri) {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.let {
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                var groupTitle = ""
                var groupDescription = ""
                val groupId = Random.nextInt(9999999)

                val elements = reader.readText().split("▼")

                for ((i, element) in elements.withIndex())
                {
                    val fields = element.split("▲")

                    println("--------\n" + i.toString() + "\n\nTitle: " + fields.getOrNull(0) + "\n\n" + fields.getOrNull(1))

                    if (i == 0 && fields.size >= 2)
                    {
                        // First element always correspond to Group Title and Description
                        groupTitle = fields[0].trim()
                        groupDescription = fields[1]

                        val flashcardGroup = FlashcardGroup(
                            groupId = groupId,
                            title = groupTitle,
                            description = groupDescription,
                            date = System.currentTimeMillis(),
                            templateId = null)

                        runBlocking {
                            insertGroupInDatabase(flashcardGroup)
                        }
                    }
                    else if (fields.size >= 2)
                    {
                        // process flashcard section
                        val title = fields[0].trim()
                        val content = fields[1]
                        if (title.isNotBlank()) {
                            val flashcard = Flashcard(
                                groupId = groupId,
                                title = title,
                                content = content,
                                answer = content,
                                date = System.currentTimeMillis())
                            insertFlashcardInDatabase(flashcard)
                        }
                    }
                }
                reader.close()
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error importing FLSHG: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun updateGroupInDatabase(group: FlashcardGroup) {
        // Use coroutine scope to call suspend function
        CoroutineScope(Dispatchers.IO).launch {
            groupDao.updateGroup(group)
        }
    }

    suspend fun insertGroupInDatabase(group: FlashcardGroup) {
        groupDao.insertGroup(group)
    }

    private fun insertFlashcardInDatabase(flashcard: Flashcard) {
        // Use coroutine scope to call suspend function
        CoroutineScope(Dispatchers.IO).launch {
            flashcardDao.insertFlashcard(flashcard)
        }
    }

    companion object {
        private const val TAG = "Import"
    }
}
