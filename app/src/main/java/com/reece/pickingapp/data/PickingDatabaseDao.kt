package com.reece.pickingapp.data

import androidx.compose.runtime.MutableState
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.reece.pickingapp.models.EclipseCredentialModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PickingDatabaseDao {

    @Query("SELECT * from eclipse_credentials_tbl")
    fun getCredentials(): Flow<List<EclipseCredentialModel>>
    @Query("SELECT * from eclipse_credentials_tbl")
    fun getCountCredentials(): List<EclipseCredentialModel>
    @Query("SELECT * from eclipse_credentials_tbl where ec_email=:email")
    suspend fun getCredentialByEmail(email: String): EclipseCredentialModel

    @Query("SELECT * from eclipse_credentials_tbl where uid=:id")
    suspend fun getCredentialById(id: String): EclipseCredentialModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCredential(credential: EclipseCredentialModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCredential(credential: EclipseCredentialModel)

    @Delete
    suspend fun deleteCredential(credential: EclipseCredentialModel)
}
