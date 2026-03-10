package com.example.gestorinventariotienda.di

import android.app.Application
import androidx.room.Room
import com.example.gestorinventariotienda.data.local.AppDatabase
import com.example.gestorinventariotienda.data.local.IncidentDao
import com.example.gestorinventariotienda.data.local.ProductDao
import com.example.gestorinventariotienda.data.preferences.SettingsManager
import com.example.gestorinventariotienda.data.remote.FakeStoreApi
import com.example.gestorinventariotienda.data.repository.InventoryRepositoryImpl
import com.example.gestorinventariotienda.domain.repository.InventoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Módulo de Dagger Hilt.
// @InstallIn(SingletonComponent::class) indica que las dependencias durarán toda la vida de la app.
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // 1. Proveemos la instancia única (Singleton) de la base de datos Room.
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "inventory_database"
        ).build()
    }

    // 2. Extraemos el ProductDao de la base de datos para inyectarlo donde haga falta.
    @Provides
    @Singleton
    fun provideProductDao(db: AppDatabase): ProductDao {
        return db.productDao()
    }

    // 3. Extraemos el IncidentDao.
    @Provides
    @Singleton
    fun provideIncidentDao(db: AppDatabase): IncidentDao {
        return db.incidentDao()
    }

    // 4. Preparamos y configuramos Retrofit con su URL base y su convertidor Gson para JSON.
    @Provides
    @Singleton
    fun provideFakeStoreApi(): FakeStoreApi {
        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FakeStoreApi::class.java)
    }

    // 5. Unificamos todas las fuentes de datos dentro de nuestro Repositorio, indicándole a Hilt
    // que cuando pidamos un 'InventoryRepository' nos devuelva su implementación 'InventoryRepositoryImpl'.
    @Provides
    @Singleton
    fun provideInventoryRepository(
        productDao: ProductDao,
        incidentDao: IncidentDao,
        api: FakeStoreApi
    ): InventoryRepository {
        return InventoryRepositoryImpl(productDao, incidentDao, api)
    }

    // 6. Proveemos nuestro gestor de preferencias (DataStore). Recibe el Application Context.
    @Provides
    @Singleton
    fun provideSettingsManager(app: Application): SettingsManager {
        return SettingsManager(app)
    }
}
