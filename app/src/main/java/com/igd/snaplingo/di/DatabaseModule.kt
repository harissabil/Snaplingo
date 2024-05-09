package com.igd.snaplingo.di

import android.content.Context
import androidx.room.Room
import com.igd.snaplingo.data.local.room.SnapDao
import com.igd.snaplingo.data.local.room.SnapDatabase
import com.igd.snaplingo.data.local.room.TaskDao
import com.igd.snaplingo.data.local.room.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSnapDatabase(@ApplicationContext context: Context): SnapDatabase =
        Room.databaseBuilder(
            context = context,
            klass = SnapDatabase::class.java,
            name = "Snap.db"
        )
            .createFromAsset("database/snap.db")
            .build()

    @Provides
    fun provideSnapDao(database: SnapDatabase): SnapDao = database.snapDao()

    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase =
        Room.databaseBuilder(
            context = context,
            klass = TaskDatabase::class.java,
            name = "Task.db"
        )
            .createFromAsset("database/task.db")
            .build()

    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao = database.taskDao()
}