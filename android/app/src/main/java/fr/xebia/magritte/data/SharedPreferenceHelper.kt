package fr.xebia.magritte.data

import android.content.Context
import android.preference.PreferenceManager
import fr.xebia.magritte.SP_MAGRITTE_MODEL_FILE

class SharedPreferenceHelper(val context: Context) {

    fun storeFile(filePath: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit {
            put(SP_MAGRITTE_MODEL_FILE to filePath)
        }
    }

    fun getModelFilePath(): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(SP_MAGRITTE_MODEL_FILE, null)
    }
}