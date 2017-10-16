package fr.xebia.magritte.data.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "labels")
data class MagritteLabel(@PrimaryKey val value: String, val category: String)