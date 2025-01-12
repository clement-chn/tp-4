package com.ynov.showroom

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext
import com.ynov.showroom.CarDao

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCarApi(): CarApi {
        return Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/ncltg/6a74a0143a8202a5597ef3451bde0d5a/raw/8fa93591ad4c3415c9e666f888e549fb8f945eb7/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CarApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCarRepository(api: CarApi, carDao: CarDao): CarListRepository {
        return CarListRepositoryImpl(api, carDao)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "showroom_database"
        ).build()
    }

    @Provides
    fun provideCarDao(appDatabase: AppDatabase): CarDao {
        return appDatabase.carDao()
    }
}
