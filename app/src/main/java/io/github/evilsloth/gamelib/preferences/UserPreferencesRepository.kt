package io.github.evilsloth.gamelib.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

const val GRID_VIEW_KEY = "GRID_VIEW"

class UserPreferencesRepository @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPref = context.getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)

    var gridView: Boolean
        get() = sharedPref.getBoolean(GRID_VIEW_KEY, false)
        set(value) {
            with (sharedPref.edit()) {
                putBoolean(GRID_VIEW_KEY, value)
                apply()
            }
        }

}