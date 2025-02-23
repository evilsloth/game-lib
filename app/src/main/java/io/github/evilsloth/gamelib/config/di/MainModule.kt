package io.github.evilsloth.gamelib.config.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.evilsloth.gamelib.accounts.amazon.AmazonTokenApiService
import io.github.evilsloth.gamelib.accounts.egs.EgsTokenApiService
import io.github.evilsloth.gamelib.accounts.gog.GogTokenApiService
import io.github.evilsloth.gamelib.config.api.amazon.AmazonApiServiceFactory
import io.github.evilsloth.gamelib.config.api.amazon.AmazonAuthApiServiceFactory
import io.github.evilsloth.gamelib.config.api.egs.EgsAuthApiServiceFactory
import io.github.evilsloth.gamelib.config.api.egs.EgsLibraryApiServiceFactory
import io.github.evilsloth.gamelib.config.api.egs.EgsProductApiServiceFactory
import io.github.evilsloth.gamelib.config.api.gamedb.GameDbApiServiceFactory
import io.github.evilsloth.gamelib.config.api.gog.GogApiServiceFactory
import io.github.evilsloth.gamelib.config.api.gog.GogAuthApiServiceFactory
import io.github.evilsloth.gamelib.config.api.igdb.IgdbApiServiceFactory
import io.github.evilsloth.gamelib.config.api.steam.SteamApiServiceFactory
import io.github.evilsloth.gamelib.games.gamedb.GameDbApiService
import io.github.evilsloth.gamelib.library.amazon.AmazonLibraryApiService
import io.github.evilsloth.gamelib.library.egs.library.EgsLibraryApiService
import io.github.evilsloth.gamelib.library.egs.products.EgsProductApiService
import io.github.evilsloth.gamelib.library.gog.GogLibraryApiService
import io.github.evilsloth.gamelib.library.igdb.IgdbGamesApiService
import io.github.evilsloth.gamelib.library.steam.SteamLibraryApiService

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    fun provideGogTokenApiService(gogAuthApiServiceFactory: GogAuthApiServiceFactory): GogTokenApiService {
        return gogAuthApiServiceFactory.create(GogTokenApiService::class.java)
    }

    @Provides
    fun provideGogLibraryApiService(gogApiServiceFactory: GogApiServiceFactory): GogLibraryApiService {
        return gogApiServiceFactory.create(GogLibraryApiService::class.java)
    }

    @Provides
    fun provideEgsTokenApiService(egsAuthApiServiceFactory: EgsAuthApiServiceFactory): EgsTokenApiService {
        return egsAuthApiServiceFactory.create(EgsTokenApiService::class.java)
    }

    @Provides
    fun provideEgsLibraryApiService(egsLibraryApiServiceFactory: EgsLibraryApiServiceFactory): EgsLibraryApiService {
        return egsLibraryApiServiceFactory.create(EgsLibraryApiService::class.java)
    }

    @Provides
    fun provideEgsProductApiService(egsProductApiServiceFactory: EgsProductApiServiceFactory): EgsProductApiService {
        return egsProductApiServiceFactory.create(EgsProductApiService::class.java)
    }

    @Provides
    fun providesAmazonTokenApiService(amazonAuthApiServiceFactory: AmazonAuthApiServiceFactory): AmazonTokenApiService {
        return amazonAuthApiServiceFactory.create(AmazonTokenApiService::class.java)
    }

    @Provides
    fun providesAmazonLibraryApiService(amazonApiServiceFactory: AmazonApiServiceFactory): AmazonLibraryApiService {
        return amazonApiServiceFactory.create(AmazonLibraryApiService::class.java)
    }

    @Provides
    fun providesSteamLibraryApiService(steamApiServiceFactory: SteamApiServiceFactory): SteamLibraryApiService {
        return steamApiServiceFactory.create(SteamLibraryApiService::class.java)
    }

    @Provides
    fun provideIgdbGamesApiService(igdbApiServiceFactory: IgdbApiServiceFactory): IgdbGamesApiService {
        return igdbApiServiceFactory.create(IgdbGamesApiService::class.java)
    }

    @Provides
    fun provideGameDbApiService(dbApiServiceFactory: GameDbApiServiceFactory): GameDbApiService {
        return dbApiServiceFactory.create(GameDbApiService::class.java)
    }

}