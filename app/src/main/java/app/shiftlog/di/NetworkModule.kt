package app.shiftlog.di

import android.net.http.HttpResponseCache.install
import app.shiftlog.data.remote.ShiftApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true   // не падать если сервер добавит новые поля
                isLenient = true   // прощать мелкие отклонения от JSON стандарта
            })
        }
    }

    @Provides
    @Singleton
    fun provideShiftApiService(client: HttpClient): ShiftApiService = ShiftApiService(client)
}