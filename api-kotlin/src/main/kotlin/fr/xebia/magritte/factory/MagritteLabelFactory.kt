package fr.xebia.magritte.factory

import fr.xebia.magritte.model.MagritteLabel

class MagritteLabelFactory {

    fun getLabels(): List<MagritteLabel> {
        val labels = mutableListOf<MagritteLabel>()
        val inputStream = this::class.java.classLoader.getResource("labels.txt").openStream()
        inputStream.bufferedReader().lines().forEach {
            labels.add(MagritteLabel(it))
        }
        inputStream.close()
        return labels
    }
}