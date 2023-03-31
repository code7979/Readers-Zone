package com.code7979.readerszone.di

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.code7979.Database
import com.code7979.readerszone.data.local.LocalBookRemoteDataSource
import com.code7979.readerszone.data.local.LocalDataSource
import com.code7979.readerszone.data.remote.RemoteBookRemoteDataSource
import com.code7979.readerszone.data.remote.RemoteDataSource
import com.code7979.readerszone.data.remote.domain.BookApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getRemoteBookDataSource(bookApi: BookApi): RemoteDataSource {
        return RemoteBookRemoteDataSource(bookApi)
    }

    @Provides
    @Singleton
    fun getLocalBookDataSource(driver: SqlDriver): LocalDataSource {
        return LocalBookRemoteDataSource(Database(driver))
    }

    @Provides
    @Singleton
    fun provideSqlDriver(app: Application): SqlDriver {
        return AndroidSqliteDriver(
            schema = Database.Schema,
            context = app,
            name = "person.db"
        )
    }
}