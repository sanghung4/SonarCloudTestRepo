package com.reece.pickingapp.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.reece.pickingapp.models.EclipseCredentialModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import java.util.concurrent.CountDownLatch
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class PickingDatabaseDaoTest {
    private lateinit var database: PickingAppDatabase
    private lateinit var dao: PickingDatabaseDao

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PickingAppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.eclipseLoginDAO()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertEclipseCredential() = runBlocking {
        val eclipseCredential = EclipseCredentialModel(uid = UUID.randomUUID(), username = "someString",
        email = "someString@reece.com",
        isRemember = true,
        eclipsePass = "someString",
        entryDate = "someString")

        dao.insertCredential(eclipseCredential)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            dao.getCredentials().collect {
                assertEquals(1,it.size)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }
    @Test
    fun deleteEclipseCredential_returnsTrue() = runBlocking {
        val credential = EclipseCredentialModel(uid = UUID.randomUUID(), username = "someString",
            email = "someString@reece.com",
            isRemember = true,
            eclipsePass = "someString",
            entryDate = "someString")
        val secondCredential = EclipseCredentialModel(uid = UUID.randomUUID(), username = "someString",
            email = "someSecondEmail@reece.com",
            isRemember = true,
            eclipsePass = "someString",
            entryDate = "someString")

        dao.insertCredential(credential)
        dao.insertCredential(secondCredential)

        dao.deleteCredential(secondCredential)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            dao.getCredentials().collect {
                assertThat(it,not(hasItem(secondCredential)))
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()

    }
    @Test
    fun updateEclipseCredential() = runBlocking {
        val eclipseCredential = EclipseCredentialModel(uid = UUID.randomUUID(), username = "someString",
            email = "someString@reece.com",
            isRemember = true,
            eclipsePass = "someString",
            entryDate = "someString")

        dao.insertCredential(eclipseCredential)

        eclipseCredential.email = "otherSomeString@reece.com"

        dao.updateCredential(eclipseCredential)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            dao.getCredentials().collect {
                assertEquals(it[0].email,eclipseCredential.email)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }
}