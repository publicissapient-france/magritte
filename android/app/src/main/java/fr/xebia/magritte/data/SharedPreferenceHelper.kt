package fr.xebia.magritte.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import fr.xebia.magritte.SP_MAGRITTE_INIT_DATA_LOADED
import fr.xebia.magritte.SP_MAGRITTE_MODEL_FILE

class SharedPreferenceHelper(val context: Context) {

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun storeFile(filePath: String) {
        prefs.edit {
            put(SP_MAGRITTE_MODEL_FILE to filePath)
        }
    }

    fun getModelFilePath(): String? {
        return prefs.getString(SP_MAGRITTE_MODEL_FILE, null)
    }

    fun setInitDataStatus(loaded: Boolean) {
        prefs.edit {
            put(SP_MAGRITTE_INIT_DATA_LOADED to loaded)
        }
    }

    fun getInitDataStatus(): Boolean {
        return prefs.getBoolean(SP_MAGRITTE_INIT_DATA_LOADED, false)
    }
}