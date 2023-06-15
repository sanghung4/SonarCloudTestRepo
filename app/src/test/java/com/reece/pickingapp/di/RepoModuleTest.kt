package com.reece.pickingapp.di

import com.reece.pickingapp.networking.ConnectivityService
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.StringConverter
import com.reece.pickingapp.view.ui.Utils
import com.reece.pickingapp.wrappers.MaterialAlertDialogBuilderWrapper
import com.reece.pickingapp.wrappers.OIDCConfigWrapper
import com.reece.pickingapp.wrappers.OktaWebAuthWrapper
import com.reece.pickingapp.wrappers.ProductsAdapterWrapper
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.mockk

class RepoModuleTest : DescribeSpec({
    describe("RepoModule") {
        lateinit var uut: RepoModule

        beforeEach {
            uut = RepoModule()
        }

        describe("when .provideWebService()") {

            var result: PickingApi? = null

            beforeEach {
                result = uut.provideWebService(mockk(), mockk(), mockk())
            }

            it("returns a ConnectivityService") {
                result shouldNotBe null
            }
        }

        describe("when .provideConnectivityService()") {

            lateinit var mockActivityService: ActivityService
            var result: ConnectivityService? = null

            beforeEach {
                mockActivityService = mockk()
                result = uut.provideConnectivityService(mockActivityService)
            }

            it("returns a ConnectivityService") {
                result shouldNotBe null
            }
        }

        describe("when .provideUtils()") {
            var result: Utils? = null

            beforeEach {
                result = uut.provideUtils()
            }

            it("returns a Utils") {
                result shouldNotBe null
            }
        }

        describe("when .provideActivityService()") {
            var result: ActivityService? = null

            beforeEach {
                result = uut.provideActivityService()
            }

            it("returns a ActivityService") {
                result shouldNotBe null
            }
        }

        describe("when .provideOIDCConfigWrapper()") {
            var result: OIDCConfigWrapper? = null

            beforeEach {
                result = uut.provideOIDCConfigWrapper()
            }

            it("returns a OIDCConfigWrapper") {
                result shouldNotBe null
            }
        }

        describe("when .provideOktaWebAuthWrapper()") {
            var result: OktaWebAuthWrapper? = null

            beforeEach {
                result = uut.provideOktaWebAuthWrapper()
            }

            it("returns a OktaWebAuthWrapper") {
                result shouldNotBe null
            }
        }

        describe("when .provideStringConverter()") {
            var result: StringConverter? = null

            beforeEach {
                result = uut.provideStringConverter()
            }

            it("returns a StringConverter") {
                result shouldNotBe null
            }
        }

        describe("when .providesProductsAdapterWrapper()") {
            var result: ProductsAdapterWrapper? = null

            beforeEach {
                result = uut.providesProductsAdapterWrapper()
            }

            it("returns a ProductsAdapterWrapper") {
                result shouldNotBe null
            }
        }

        describe("when .providesMaterialAlertDialogBuilderWrapper()") {
            var result: MaterialAlertDialogBuilderWrapper? = null

            beforeEach {
                result = uut.providesMaterialAlertDialogBuilderWrapper()
            }

            it("returns a MaterialAlertDialogBuilderWrapper") {
                result shouldNotBe null
            }
        }
    }
})