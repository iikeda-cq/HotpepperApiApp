package com.google.codelab.hotpepperapiapp.model.businessmodel

import com.google.codelab.hotpepperapiapp.model.response.StoresResponse
import retrofit2.Response

/**
 * APIから取得したデータ[StoresResponse]をBusinessModelに変換するクラス
 * Repository[SearchDataManagerImpl]で[StoresResponse]から[StoreListBusinessModel]に変換することで
 * UseCase → Repository　の依存関係が Repository → UseCase になり依存性が逆転している…のかな？
 */
data class StoreListBusinessModel(
    val totalPages: Int,
    val store: List<Store>
) {
    companion object {
        fun transform(response: Response<StoresResponse>): StoreListBusinessModel? {
            return response.body()?.results?.let {
                StoreListBusinessModel(
                    totalPages = it.totalPages,
                    store = transformStore(response.body())
                )
            }
        }

        private fun transformStore(response: StoresResponse?): List<Store> {
            return response?.results?.store?.let {
                it.map {
                    Store(
                        id = it.id,
                        name = it.name,
                        lat = it.lat,
                        lng = it.lng,
                        genre = it.genre.name,
                        budget = it.budget.average,
                        urls = it.urls.url,
                        photo = it.photo.photo.logo
                    )
                }
            } ?: emptyList()
        }
    }
}

data class Store(
    val id: String,
    val name: String,
    val lat: Double,
    val lng: Double,
    val genre: String,
    val budget: String,
    val urls: String,
    val photo: String
)

