package io.github.evilsloth.gamelib.library.egs

import io.github.evilsloth.gamelib.accounts.egs.EgsAuthTokenRepository
import io.github.evilsloth.gamelib.games.GameRepository
import io.github.evilsloth.gamelib.library.egs.library.EgsLibraryApiService
import io.github.evilsloth.gamelib.library.egs.library.EgsUserLibraryItem
import io.github.evilsloth.gamelib.library.egs.products.EgsProductApiService
import io.github.evilsloth.gamelib.library.egs.products.EgsProductDao
import io.github.evilsloth.gamelib.library.egs.products.EgsProductEntity
import io.github.evilsloth.gamelib.library.model.LibraryItem
import javax.inject.Inject

private const val IDS_CHUNK_SIZE = 100

class EgsLibraryRepository @Inject constructor(
    private val egsLibraryApiService: EgsLibraryApiService,
    private val egsAuthTokenRepository: EgsAuthTokenRepository,
    private val egsProductDao: EgsProductDao,
    private val egsProductApiService: EgsProductApiService,
    private val gameRepository: GameRepository
) {

    suspend fun getUserGames(): List<LibraryItem> {
        if (egsAuthTokenRepository.getAccessToken() == null) {
            return listOf()
        }

        val userLibraryItems = mutableListOf<EgsUserLibraryItem>()
        var cursor: String? = null

        do {
            val userLibraryResponse = egsLibraryApiService.getUserLibrary(cursor = cursor)
            userLibraryItems.addAll(userLibraryResponse.records)

            cursor = userLibraryResponse.responseMetadata?.nextCursor
        } while (cursor != null)

        val egsProducts = userLibraryItems.filter { it.recordType == "APPLICATION" }.distinctBy { it.productId }
        val egsProductsIds = egsProducts.map { it.productId }

        if (egsProductsIds.isEmpty()) {
            return listOf()
        }

        val dbGames = egsProductsIds.chunked(IDS_CHUNK_SIZE).flatMap { idsChunk ->
            gameRepository.getGames(epicIds = idsChunk)
        }

        val dbGamesByEpicId = dbGames.associateBy { it.ids.epicId }

        return egsProducts.map {
            val dbGame = dbGamesByEpicId[it.productId]

            LibraryItem(
                igdbId = dbGame?.ids?.igdbId,
                externalId = it.productId,
                name = dbGame?.name ?: getProductName(it),
                platform = LibraryItem.Platform.EGS,
                url = dbGame?.url,
                rating = dbGame?.rating,
                userRating = dbGame?.userRating,
                coverUrl = dbGame?.coverUrl,
                thumbnailUrl = dbGame?.thumbnailUrl
            )
        }
    }

    private suspend fun getProductName(item: EgsUserLibraryItem): String {
        if (item.sandboxName != "Live") {
            return item.sandboxName
        }

        val product = egsProductDao.getById(item.productId)
        if (product != null) {
            return product.title
        } else {
            val productDetails = egsProductApiService.getProductDetails(item.productId)
            val newProduct = EgsProductEntity(id = item.productId, title = productDetails.title, slug = productDetails.mapping.slug)
            egsProductDao.insert(newProduct)
            return newProduct.title
        }
    }

}