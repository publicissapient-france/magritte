package fr.xebia.magritte.factory

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import fr.xebia.magritte.model.MagritteModel
import java.io.InputStreamReader
import java.util.*

class MagritteModelFactory {

    fun getModels(): List<MagritteModel> {
        val inputStream = this::class.java.classLoader.getResource("static/models.json").openStream()
        val gson = Gson()
        val reader = JsonReader(InputStreamReader(inputStream, "UTF-8"))
        val models = ArrayList<MagritteModel>()
        reader.beginArray()
        while (reader.hasNext()) {
            val model: MagritteModel = gson.fromJson(reader, MagritteModel::class.java)
            models.add(model)
        }
        reader.endArray()
        reader.close()
        return models
    }
}