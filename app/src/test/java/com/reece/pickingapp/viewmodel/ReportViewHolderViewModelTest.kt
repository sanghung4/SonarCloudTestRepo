package com.reece.pickingapp.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.reece.pickingapp.R
import com.reece.pickingapp.testingUtils.MockActivityService
import com.reece.pickingapp.type.SerialLineInput
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.emptyString
import com.reece.pickingapp.utils.extensions.showMessage
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.*

class ReportViewHolderViewModelTest : DescribeSpec({
    describe("OrderViewHolderViewModel") {
        lateinit var uut: ReportProductViewHolderViewModel
        lateinit var mockActivityService: ActivityService
        lateinit var mockProductModel: ProductModel
        lateinit var mockLifecycleOwner: LifecycleOwner
        lateinit var mockActivity: Activity
        lateinit var mockContext: Context
        lateinit var mockNavController: NavController
        lateinit var mockDefaultLocationSerialLineInput:  MutableList<SerialLineInput>
        val actionGlobalAuthNavigationValue = R.id.action_global_auth_navigation

        beforeEach {

            mockDefaultLocationSerialLineInput = mutableListOf()
            mockActivity = mockk()

            mockNavController = mockk{
                every { navigate(actionGlobalAuthNavigationValue) } returns kotlin.Unit
            }

            mockActivityService = mockk {
                every { activity } returns mockActivity
                every { navController } returns mockNavController
                every { getString(any()) } returns "someString"
                every { showMessage(any(), any()) } returns Unit
            }
            mockContext = mockk()

            mockProductModel = mockk()
            mockLifecycleOwner = mockk()

            uut = ReportProductViewHolderViewModel(
                product = mockProductModel,
                lifecycleOwner = mockLifecycleOwner
            )
        }

        describe("when .setProductUI()") {
            beforeEach {
                every { mockProductModel.location } returns "LOC1"
                every { mockProductModel.quantity } returns 1
                every { mockProductModel.qtyPicked } returns "1"
                every { mockProductModel.isBackOrder } returns false
                every { mockProductModel.description } returns "DESC1"
                every { mockProductModel.productId } returns "2023"
                every { mockProductModel.defaultLocationSerialLineInput } returns mockDefaultLocationSerialLineInput
                uut.setProductUI()
            }

            it("sets all ui values") {
                uut.txtShipQty.value shouldNotBe 0
                uut.txtPickedQty.value shouldNotBe null
                uut.txtPickedQty.value shouldNotBe emptyString
                uut.txtDescription.value shouldNotBe null
                uut.txtDescription.value shouldNotBe emptyString
                uut.txtDescPNNumber.value shouldNotBe null
                uut.txtDescPNNumber.value shouldNotBe emptyString
                uut.txtDescLocation.value shouldNotBe null
                uut.txtDescLocation.value shouldNotBe emptyString
            }

        }
    }
})