package com.reece.pickingapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reece.pickingapp.models.EclipseCredentialModel

@Database(
    entities = [EclipseCredentialModel::class],
    version = 1,
    exportSchema = false
)
abstract class PickingAppDatabase : RoomDatabase() {
    abstract fun eclipseLoginDAO(): PickingDatabaseDao
}