package com.navi.file.hilt

import com.navi.file.BuildConfig
import com.navi.file.repository.server.folder.FolderRepository
import com.navi.file.repository.server.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun createRetrofit(): Retrofit {
        val httpUrl = HttpUrl.Builder()
            .scheme("http")
            .host(BuildConfig.SERVER_URL)
            .port(BuildConfig.SERVER_PORT)
            .build()

        return Retrofit.Builder()
            .baseUrl(httpUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun createFolderRepository(retrofit: Retrofit): FolderRepository = FolderRepository(retrofit)

    @Provides
    @Singleton
    fun createUserRepository(retrofit: Retrofit): UserRepository = UserRepository(retrofit)
}