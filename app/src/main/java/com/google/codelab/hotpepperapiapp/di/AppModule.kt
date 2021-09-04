package com.google.codelab.hotpepperapiapp.di

import android.content.Context
import com.google.codelab.hotpepperapiapp.data.SearchDataManager
import com.google.codelab.hotpepperapiapp.data.SearchDataManagerImpl
import com.google.codelab.hotpepperapiapp.usecase.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

// 参考:https://qiita.com/uhooi/items/2a1ccb3fab9afd539203
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindSearchDataManager(searchDataManagerImpl: SearchDataManagerImpl): SearchDataManager

    @Singleton
    @Binds
    abstract fun bindMapsUseCase(mapsUseCaseImpl: MapsUseCaseImpl): MapsUseCase

    @Singleton
    @Binds
    abstract fun bindStoreListUseCase(storeListUseCaseImpl: StoreListUseCaseImpl): StoreListUseCase

    @Singleton
    @Binds
    abstract fun bindStoreWebViewUseCase(storeWebViewUseCaseImpl: StoreWebViewUseCaseImpl): StoreWebViewUseCase

    @Singleton
    @Binds
    abstract fun bindFavoriteStoreListUseCase(favoriteStoresUseCaseImpl: FavoriteStoresUseCaseImpl): FavoriteStoreUseCase
}

