package com.example.ultimate_flashcard_app.di

import android.content.Context
import androidx.room.Room
import com.example.ultimate_flashcard_app.data.Database.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, database::class.java, "FlashcardAppDatabase")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideFlashcardDao(db: database) = db.folderDao()

    @Provides
    @Singleton
    fun provideTemplateDao(db: database) = db.templateDao()

    @Provides
    @Singleton
    fun provideGroupDao(db: database) = db.groupDao()

    @Provides
    @Singleton
    fun provideHistoryDao(db: database) = db.historyDao()

}