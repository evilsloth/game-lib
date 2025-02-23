package io.github.evilsloth.gamelib.config.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.evilsloth.gamelib.config.room.AppDatabase
import io.github.evilsloth.gamelib.library.egs.products.EgsProductDao
import io.github.evilsloth.gamelib.library.model.LibraryItemDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideEgsProductDao(appDatabase: AppDatabase): EgsProductDao {
        return appDatabase.egsProductDao()
    }

    @Provides
    fun provideLibraryItemDao(appDatabase: AppDatabase): LibraryItemDao {
        return appDatabase.libraryItemDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "GameLib")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

}