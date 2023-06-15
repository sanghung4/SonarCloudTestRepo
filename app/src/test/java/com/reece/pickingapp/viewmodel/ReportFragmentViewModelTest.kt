package com.reece.pickingapp.viewmodel

import android.app.Activity
import android.content.SharedPreferences
import com.apollographql.apollo.api.Response
import com.reece.pickingapp.ShippingDetailsQuery
import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.repository.PickingRepositoryImpl
import com.reece.pickingapp.repository.UserPreferencesImp
import com.reece.pickingapp.testingUtils.MockActivityService
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.StringConverter
import com.reece.pickingapp.wrappers.ReportAdapterWrapper
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class ReportFragmentViewModelTest : DescribeSpec({
    describe("ProductsFragmentViewModel") {
        lateinit var uut: ReportFragmentViewModel
        lateinit var mockRepository: PickingRepositoryImpl
        lateinit var mockUserPreferences: UserPreferencesImp
        lateinit var mockActivityService: ActivityService
        lateinit var mockStringConverter: StringConverter
        lateinit var mockProductsAdapterWrapper: ReportAdapterWrapper
        lateinit var mockLocationDataEntryViewModel: DataEntryViewModel
        lateinit var mockBoxesDataEntryViewModel: DataEntryViewModel
        lateinit var mockSkidsDataEntryViewModel: DataEntryViewModel
        lateinit var mockBundlesDataEntryViewModel: DataEntryViewModel
        lateinit var mockPiecesDataEntryViewModel: DataEntryViewModel
        lateinit var mainThreadSurrogate: ExecutorCoroutineDispatcher
        lateinit var mockNavigateAfterStagingCallback: () -> Unit
        var navigateAfterStagingCallbackWasCalled = false
        lateinit var mockResponse: Response<ShippingDetailsQuery.Data>
        lateinit var mockData: ShippingDetailsQuery.Data
        lateinit var mockErrorData: ArrayList<ErrorLogDTO>
        lateinit var mockSharedPreferences: SharedPreferences
        lateinit var mockActivity: Activity


        beforeEach {
            mainThreadSurrogate = newSingleThreadContext("UI thread")
            Dispatchers.setMain(mainThreadSurrogate)

            mockRepository = mockk()
            mockUserPreferences = mockk {
                every { getErrorLogs() } returns ArrayList<ErrorLogDTO>()
            }
            mockErrorData = mockUserPreferences.getErrorLogs()


            mockActivityService = MockActivityService().create()
            mockStringConverter = mockk {
                every { convertToHtmlSpan(any()) } returns mockk()
            }
            mockProductsAdapterWrapper = mockk()

            mockLocationDataEntryViewModel = mockk {
                every {
                    getInputString()
                } returns "some value"
                every {
                    setInputState(any())
                } returns Unit
                every {
                    setHintString(any())
                } returns Unit
            }
            mockBoxesDataEntryViewModel = mockk {
                every {
                    getInputString()
                } returns "some value"
                every {
                    setInputState(any())
                } returns Unit
                every {
                    setHintString(any())
                } returns Unit
                every {
                    setDataEntryFieldType(any())
                } returns Unit
            }
            mockSkidsDataEntryViewModel = mockk {
                every {
                    getInputString()
                } returns "some value"
                every {
                    setInputState(any())
                } returns Unit
                every {
                    setHintString(any())
                } returns Unit
                every {
                    setDataEntryFieldType(any())
                } returns Unit
            }
            mockBundlesDataEntryViewModel = mockk {
                every {
                    getInputString()
                } returns "some value"
                every {
                    setInputState(any())
                } returns Unit
                every {
                    setHintString(any())
                } returns Unit
                every {
                    setDataEntryFieldType(any())
                } returns Unit
            }
            mockPiecesDataEntryViewModel = mockk {
                every {
                    getInputString()
                } returns "some value"
                every {
                    setInputState(any())
                } returns Unit
                every {
                    setHintString(any())
                } returns Unit
                every {
                    setDataEntryFieldType(any())
                } returns Unit
            }

            uut = ReportFragmentViewModel(

                mockUserPreferences,
                mockProductsAdapterWrapper,
                mockActivityService

            )

            mockSharedPreferences = mockk {
                every { getString(any(), any()) } returns "someString"
                every { edit().putString(any(), any())?.apply() } returns Unit
            }
            mockActivity = mockk {
                every { getSharedPreferences(any(), any()) } returns mockSharedPreferences
            }
            mockActivityService = mockk {
                every { activity } returns mockActivity
            }

            mockUserPreferences = UserPreferencesImp(mockActivityService)
        }

        afterEach {
            Dispatchers.resetMain()
            mainThreadSurrogate.close()
        }
    }
})