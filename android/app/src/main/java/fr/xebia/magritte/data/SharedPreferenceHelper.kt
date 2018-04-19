package fr.xebia.magritte.data

import android.content.Context
import android.content.SharedPreferences
import fr.xebia.magritte.MAGRITTE_SHARED_PREFERENCE
import fr.xebia.magritte.SP_MAGRITTE_INIT_DATA_LOADED

class SharedPreferenceHelper(val context: Context) {

    private val prefs: SharedPreferences =
            context.getSharedPreferences(String.format(MAGRITTE_SHARED_PREFERENCE), Context.MODE_PRIVATE)

    fun storeFile(modelId: String, filePath: String) {
        prefs.edit {
            put(modelId to filePath)
        }
    }

    fun getModelFilePath(modelId: String): String? {
        return prefs.getString(modelId, null)
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