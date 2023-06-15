package com.reece.pickingapp.repository

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.models.SplitQtyDTO
import com.reece.pickingapp.testingUtils.delayDuration
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.extensions.setErrorLog
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.lang.reflect.Type
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class UserPreferencesTest : DescribeSpec({
    describe("UserPreferences") {
        lateinit var uut: UserPreferencesImp
        lateinit var mockActivityService: ActivityService
        lateinit var mockActivity: Activity
        lateinit var mockSharedPreferences: SharedPreferences

        beforeEach {
            mockSharedPreferences = mockk {
                every { getString(any(), any()) } returns "someString"
                every { getString("split_qty", any()) } returns "{}"
                every { getString("error_log", any()) } returns "[]"
                every { edit().putString(any(), any())?.apply() } returns Unit
            }
            mockActivity = mockk {
                every { getSharedPreferences(any(), any()) } returns mockSharedPreferences
            }
            mockActivityService = mockk {
                every { activity } returns mockActivity
            }

            uut = UserPreferencesImp(mockActivityService)
        }

        describe("when .getUsername()") {
            var result: String? = null

            beforeEach {
                result = uut.getUsername()
            }

            it("calls to SharedPreferences .getString() for 'username' ") {
                verify { mockSharedPreferences.getString("username", null) }
            }
            it("returns a String") {
                result shouldNotBe null
            }
        }

        describe("when .setUsername()") {

            beforeEach {
                uut.setUsername("someUsername")
            }
            it("calls to SharedPreferences .putString() for 'username' passing in 'someUsername'") {
                verify { mockSharedPreferences.edit().putString("username", "someUsername") }
            }
        }
describe("when .setEmailId()") {

            beforeEach {
                uut.setEmailId("someEmailId")
            }
            it("calls to SharedPreferences .putString() for 'email_id' passing in 'someEmailId'") {
                verify { mockSharedPreferences.edit().putString("email_id", "someEmailId") }
            }
        }

        describe("when .getEmailId()") {
            var result: String? = null

            beforeEach {
                result = uut.getEmailId()
            }
            it("calls .getEmailId()") {
                verify { mockSharedPreferences.getString("email_id", null) }
            }
            it("returns a String") {
                result shouldNotBe null
            }
        }

        describe("when .getUserPreference()") {
            var result: UserPreferences? = null

            beforeEach {
                every {
                    mockActivityService.userPreferences
                }returns mockk()
                result = uut.getUserPreference()
            }
            it("returns a String") {
                result shouldNotBe null
            }
            it("Calls getUserPreference"){
                verify { mockActivityService.userPreferences }
            }
        }

        describe("when .getBranch()") {
            var result: String? = null

            beforeEach {
                result = uut.getBranch()
            }
            it("calls .getBranch()") {
                verify { mockSharedPreferences.getString("branch_id", null) }
            }
            it("returns a String") {
                result shouldNotBe null
            }
        }

        describe("when .getUsername()") {
            var result: String? = null

            beforeEach {
                result = uut.getUsername()
            }
            it("calls .getUsername()") {
                verify { mockSharedPreferences.getString("username", null) }
            }
            it("returns a String") {
                result shouldNotBe null
            }
        }

        describe("when .setBranch()") {

            beforeEach {
                uut.setBranch("someBranchId")
            }
            it("calls to SharedPreferences .putString() 'branch_id' passing in 'someBranchId'") {
                verify { mockSharedPreferences.edit().putString("branch_id", "someBranchId") }
            }
        }

        describe("when .setErrorLog()") {
            var errorLog= ArrayList<ErrorLogDTO>()
            var json=""
            beforeEach {
                uut.setErrorLog(errorLog)
                val gson = Gson()

                 json = gson.toJson(errorLog)
            }
            it("calls to SharedPreferences .putString() 'error_log' passing in json") {
                verify { mockSharedPreferences.edit().putString("error_log", json) }
            }
        }

        describe("when .getErrorLogs()") {
            var result: ArrayList<ErrorLogDTO>? = null

            beforeEach {
                result = uut.getErrorLogs()
            }

            it("calls .getString()") {
                verify { mockSharedPreferences.getString("error_log", "") }
            }

            it("returns a Array") {
                result shouldNotBe null
            }
        }

        describe("when .saveSplitQty()") {
            var splitQty= SplitQtyDTO()
            var json=""
            beforeEach {
                uut.saveSplitQty(splitQty)
                val gson = Gson()
                json = gson.toJson(splitQty)
            }
            it("calls to SharedPreferences .putString() 'split_qty' passing in json") {
                verify { mockSharedPreferences.edit().putString("split_qty", json) }
            }
        }

        describe("when .getSplitQty()") {
            var result: SplitQtyDTO? = null

            beforeEach {
                result = uut.getSplitQty()
            }

            it("calls .getSplitQty()") {
                verify { mockSharedPreferences.getString("split_qty", "") }
            }

            it("returns a String") {
                result shouldNotBe null
            }
        }

        describe("when deleteSplitQty()"){
            beforeEach {
                every { mockActivity.getSharedPreferences(any(), any()).edit()!!.remove(any()).apply() } returns Unit
                uut.deleteSplitQty()
            }
            it("Calls Shared Preferences "){
                verify { mockActivity.getSharedPreferences(any(), any()) }
            }
        }
        
    }
})