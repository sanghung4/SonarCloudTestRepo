package com.reece.pickingapp.repository

import com.reece.pickingapp.data.PickingDatabaseDao
import com.reece.pickingapp.models.EclipseCredentialModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class EclipseLoginRepository @Inject constructor(private val pickingDao: PickingDatabaseDao) {
    suspend fun addCredential(credential: EclipseCredentialModel) =
        pickingDao.insertCredential(credential = credential)

    suspend fun updateCredential(credential: EclipseCredentialModel) =
        pickingDao.updateCredential(credential)

    suspend fun deleteAllCredentials(credential: EclipseCredentialModel) =
        pickingDao.deleteCredential(credential = credential)

    suspend fun deleteCredential(credential: EclipseCredentialModel) = pickingDao.deleteCredential(credential)
    suspend fun getCredentialByEmail(email: String): EclipseCredentialModel? =
        pickingDao.getCredentialByEmail(email)

    suspend fun getCountCredentials(): Int =
        pickingDao.getCountCredentials().size

    fun getCredentials(): Flow<List<EclipseCredentialModel>> =
        pickingDao.getCredentials().flowOn(Dispatchers.IO).conflate()
}