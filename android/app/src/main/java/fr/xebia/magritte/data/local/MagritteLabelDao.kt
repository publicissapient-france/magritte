package fr.xebia.magritte.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface MagritteLabelDao {
    @Query("select * from labels WHERE category = :category")
    fun getAllLabels(category: String): Flowable<List<MagritteLabel>>

    @Insert
    fun insertAll(labels: List<MagritteLabel>)
}