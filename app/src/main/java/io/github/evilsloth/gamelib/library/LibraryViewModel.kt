package io.github.evilsloth.gamelib.library

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.evilsloth.gamelib.R
import io.github.evilsloth.gamelib.library.amazon.AmazonLibraryRepository
import io.github.evilsloth.gamelib.library.egs.EgsLibraryRepository
import io.github.evilsloth.gamelib.library.gog.GogLibraryRepository
import io.github.evilsloth.gamelib.library.model.LibraryItem
import io.github.evilsloth.gamelib.library.model.LibraryItemDao
import io.github.evilsloth.gamelib.library.model.SortingOption
import io.github.evilsloth.gamelib.library.steam.SteamLibraryRepository
import io.github.evilsloth.gamelib.preferences.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LibraryViewModel"

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LibraryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gogLibraryRepository: GogLibraryRepository,
    private val egsLibraryRepository: EgsLibraryRepository,
    private val amazonLibraryRepository: AmazonLibraryRepository,
    private val steamLibraryRepository: SteamLibraryRepository,
    private val libraryItemDao: LibraryItemDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val filterActive = MutableStateFlow(false)
    val searchOpened = MutableStateFlow(false)
    val searchValue = MutableStateFlow("")
    val sortingBy = MutableStateFlow(SortingOption.NAME_ASC)

    val gridView = MutableStateFlow(userPreferencesRepository.gridView)

    val refreshing = MutableStateFlow(false)
    val games: StateFlow<List<LibraryItem>?> = combine(searchValue, sortingBy) { query, sort -> Pair(query, sort) }
        .flatMapLatest { pair ->
            val results = if (pair.first.isEmpty()) libraryItemDao.getAll() else libraryItemDao.getAllByNameLike(pair.first)
            results.map { it -> it.sortedWith(pair.second.comparator) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    fun loadLibraryGames() {
        refreshing.value = true
        clearSearch()

        viewModelScope.launch {
            try {
                val allGames = gogLibraryRepository.getUserGames() +
                        egsLibraryRepository.getUserGames() +
                        amazonLibraryRepository.getUserGames() +
                        steamLibraryRepository.getUserGames()
                libraryItemDao.deleteAll()
                libraryItemDao.insertAll(allGames)
            } catch (e: Exception) {
                Log.e(TAG, "loadLibraryGames", e)
                Toast.makeText(context, R.string.library_error, Toast.LENGTH_LONG).show()
            } finally {
                refreshing.value = false
            }
        }
    }

    fun openSearch() {
        searchOpened.value = true
    }

    fun search(text: String) {
        searchValue.value = text
        filterActive.value = text.isNotEmpty()
    }

    fun clearSearch() {
        search("")
        searchOpened.value = false
    }

    fun sortBy(option: SortingOption) {
        sortingBy.value = option
    }

    fun toggleView() {
        gridView.value = !gridView.value
        userPreferencesRepository.gridView = gridView.value
    }

}