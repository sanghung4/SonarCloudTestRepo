package com.reece.pickingapp.viewmodel

import android.view.KeyEvent
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.api.Response
import com.google.android.material.textfield.TextInputLayout
import com.reece.pickingapp.PickImageQuery
import com.reece.pickingapp.repository.PickingRepositoryImpl
import com.reece.pickingapp.testingUtils.MockActivityService
import com.reece.pickingapp.testingUtils.MockDataEntryViewModel
import com.reece.pickingapp.testingUtils.MockLifecycleOwner
import com.reece.pickingapp.testingUtils.delayDuration
import com.reece.pickingapp.type.SerialLineInput
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.DataEntryFieldType
import com.reece.pickingapp.view.state.CardState
import com.reece.pickingapp.view.state.InputFocusState
import com.reece.pickingapp.view.state.InputState
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewHolderViewModelTest : DescribeSpec({
    describe("ProductViewHolderViewModel") {
        lateinit var uut: ProductViewHolderViewModel
        lateinit var mockActivityService: ActivityService
        lateinit var mockProduct: ProductModel
        lateinit var mockRepository: PickingRepositoryImpl
        lateinit var mockLifecycleOwner: LifecycleOwner
        lateinit var mockOnItemClicked: ((userPick: ProductModel, failureCallback: () -> Unit) -> Unit)
        lateinit var mockonBarcodeScanClick: ((userPick: ProductModel, successCallback: (barcode: String) -> Unit, failureCallback: () -> Unit) -> Unit)
        lateinit var mockOnItemClickedIsSerial: ((userPick: ProductModel, serialNumbers: List<SerialLineInput>, failureCallback: () -> Unit) -> Unit)
        lateinit var mockOnSplitQuantity: ((userPick: ProductModel, serialNumbers: List<SerialLineInput>, quantityPicked: Int, failureCallback: () -> Unit) -> Unit)
        lateinit var mockScrollTo: ((position: Int, offset: Int) -> Unit)
        lateinit var showQtyDialog: (userPick: ProductModel,Int) -> Unit

        lateinit var mockProductIdDataEntryViewModel: DataEntryViewModel
        lateinit var mockSplitQtyDataEntryViewModel: DataEntryViewModel
        var onBarcodeScanClickWasCalled = false

        var onItemClickedWasCalled = false
        var onItemClickedIsSerialWasCalled = false
        lateinit var passedProduct: ProductModel
        lateinit var passedFailureCallback: () -> Unit
        lateinit var passedSerialNumbers: List<SerialLineInput>
        lateinit var mockData: PickImageQuery.Data
        lateinit var mockResponse: Response<PickImageQuery.Data>
        lateinit var mainThreadSurrogate: ExecutorCoroutineDispatcher
        var mockScrollToWasCalled = false
        var passedPosition = 0
        var passedOffset = 0
        var passedPickedQnty = 0

        lateinit var mockOnMoveCursor: (adapterPosition: Int) -> Unit
        var adapterPosition: Int = 0

        beforeEach {
            mainThreadSurrogate = newSingleThreadContext("UI thread")
            Dispatchers.setMain(mainThreadSurrogate)

            mockActivityService = MockActivityService().create()
            mockRepository = mockk()
            mockProduct = mockk()
            mockLifecycleOwner = MockLifecycleOwner().create()

            mockOnItemClicked = { userPick, failureCallback ->
                onItemClickedWasCalled = true
                passedProduct = userPick
                passedFailureCallback = failureCallback
            }

            mockOnItemClickedIsSerial = { userPick, serialNumbers, failureCallback ->
                onItemClickedIsSerialWasCalled = true
                passedProduct = userPick
                passedFailureCallback = failureCallback
                passedSerialNumbers = serialNumbers
            }

            mockonBarcodeScanClick = { userPick, successCallback, failureCallback ->
                onBarcodeScanClickWasCalled = true
                passedProduct = userPick
                passedFailureCallback = failureCallback

                mockOnMoveCursor = { position ->
                    adapterPosition = position
                }

                mockOnSplitQuantity = { userPick, serialNumbers, pickedQnty, failureCallback ->
                    onItemClickedIsSerialWasCalled = true
                    passedProduct = userPick
                    passedFailureCallback = failureCallback
                    passedSerialNumbers = serialNumbers
                    passedPickedQnty = pickedQnty
                }

                mockScrollTo = { position, offset ->
                    mockScrollToWasCalled = true
                    passedPosition = position
                    passedOffset = offset
                }

                uut = ProductViewHolderViewModel(
                    mockActivityService,
                    mockProduct,
                    mockLifecycleOwner,
                    mockOnItemClicked,
                    mockOnItemClickedIsSerial,
                    mockOnSplitQuantity,
                    mockScrollTo,
                    mockOnMoveCursor,
                    mockonBarcodeScanClick,
                    mockRepository,
                    showQtyDialog
                )

                mockData = mockk()
                mockResponse = mockk {
                    every { errors } returns null
                    every { data } returns mockData
                }
                coEvery { mockRepository.queryPickingProductImage(any()) } returns mockResponse

                every { mockProduct.quantity } returns 5
                every { mockProduct.isSerial } returns false
                every { mockProduct.productId } returns "someProductId"
            }

            afterEach {
                Dispatchers.resetMain()
                mainThreadSurrogate.close()
            }

            describe("when .setUp()") {
                beforeEach {
                    mockProductIdDataEntryViewModel = MockDataEntryViewModel().create()
                    every { mockProductIdDataEntryViewModel.setDataEntryFieldType(any()) } returns Unit
                    mockSplitQtyDataEntryViewModel = MockDataEntryViewModel().create()
                    every { mockSplitQtyDataEntryViewModel.setHintString(any()) } returns Unit
                    every { mockSplitQtyDataEntryViewModel.setEndIconMode(any()) } returns Unit
                    every { mockSplitQtyDataEntryViewModel.setDataEntryFieldType(any()) } returns Unit
                    uut.productIdDataEntryViewModel =
                        MutableLiveData(mockProductIdDataEntryViewModel)
                }
                describe("when .setUpProductIdEntryViewModel()") {
                    beforeEach {
                        every { mockProduct.isSerial } returns false
                        every { mockProduct.quantity } returns 10
                        every { mockProduct.productId } returns "someProductId"
                        every { mockProductIdDataEntryViewModel.getInputString() } returns "someInputString"

                        uut.setUp()
                    }
                    it("calls .productIdDataEntryViewModel.setHintString()") {
                        verify { mockProductIdDataEntryViewModel.setHintString("Product ID") }
                    }
                    it("calls .productIdDataEntryViewModel.setInputCanBeCleared(false)") {
                        verify { mockProductIdDataEntryViewModel.setInputCanBeCleared(false) }
                    }
                    it("calls .productIdDataEntryViewModel.setDataEntryFieldTyp()e") {
                        verify {
                            mockProductIdDataEntryViewModel.setDataEntryFieldType(
                                DataEntryFieldType.SCANNABLE_PRODUCT
                            )
                        }
                    }
                }

                describe("when setProductIdState()") {
                    beforeEach {
                        every { mockProduct.isSerial } returns false
                        every { mockProduct.quantity } returns 10
                        every { mockProduct.productId } returns "someProductId"
                        every { mockProductIdDataEntryViewModel.getInputString() } returns "someInputString"
                        uut.setUp()
                    }
                    it("calls .productIdDataEntryViewModel.setInputState(InputState.Default())") {
                        verify(exactly = 1) {
                            mockProductIdDataEntryViewModel.setInputState(withArg {
                                it::class shouldBe InputState.Default::class
                            })
                        }
                    }
                }

                describe("when .adapterPosition is 0") {
                    beforeEach {
                        every { mockProduct.isSerial } returns false
                        every { mockProduct.quantity } returns 10
                        every { mockProduct.productId } returns "someProductId"
                        every { mockProductIdDataEntryViewModel.getInputString() } returns "someInputString"
                        uut.setUp()
                    }
                    describe("when .setProductIdFocusState()") {
                        verify(exactly = 1) {
                            mockProductIdDataEntryViewModel.setInputFocusState(withArg {
                                it shouldBe InputFocusState.Focussed
                            })
                        }
                    }
                }

                describe("when product does not have serial numbers") {
                    beforeEach {
                        every { mockProduct.isSerial } returns false
                        every { mockProduct.quantity } returns 10
                        every { mockProduct.productId } returns "someProductId"
                        every { mockProductIdDataEntryViewModel.getInputString() } returns "someInputString"
                        uut.setUp()
                    }
                    describe("when .setCurrentQuantity()") {
                        it("sets .quantityEditString to the product's quantity") {
                            uut.quantityEditString.value shouldBe "10"
                        }
                    }
                }

                describe("when product has serial numbers") {
                    beforeEach {
                        every { mockProduct.isSerial } returns true
                        every { mockProduct.quantity } returns 10
                        every { mockProduct.productId } returns "someProductId"
                        every { mockProductIdDataEntryViewModel.getInputString() } returns "someInputString"
                        uut.setUp()
                    }

                    it(".quantityEditString is 0") {
                        uut.quantityEditString.value shouldBe "0"
                    }
                }

                describe("when .validateProductId()") {
                    describe("when the productId is valid") {
                        beforeEach {
                            every { mockProduct.isSerial } returns false
                            every { mockProduct.quantity } returns 10
                            every { mockProduct.productId } returns "someProductId"
                            every { mockProductIdDataEntryViewModel.getInputString() } returns "someProductId"
                            uut.productIdDataEntryViewModel =
                                MutableLiveData(mockProductIdDataEntryViewModel)
                            uut.setUp()
                        }

                        describe("when setCardValidationState()") {
                            it("sets .cardValidationState to CardState.Ready()") {
                                uut.cardValidationState.value!!::class shouldBe CardState.Ready()::class
                            }
                        }
                    }
                }

                describe("when .queryPickProductImage()") {
                    describe("when the response does NOT have errors") {
                        beforeEach {
                            every { mockProduct.isSerial } returns false
                            every { mockProduct.quantity } returns 10
                            every { mockProductIdDataEntryViewModel.getInputString() } returns "someProductId"
                            every { mockProduct.productId } returns "someProductId"
                            every { mockData.productImageUrl } returns "someImageUrl"
                            mockResponse = mockk {
                                every { errors } returns null
                                every { data } returns mockData
                            }
                            coEvery { mockRepository.queryPickingProductImage(any()) } returns mockResponse
                            uut.setUp()
                        }
                        it("sets productImageUrl to 'someImageUrl'") {
                            eventually(delayDuration) {
                                uut.productImageUrl.value shouldBe "someImageUrl"
                            }
                        }
                    }
                }

                describe("when .setUpSplitQuantity()") {
                    beforeEach {
                        every { mockProduct.isSerial } returns false
                        every { mockProduct.quantity } returns 10
                        every { mockProduct.productId } returns "someProductId"

                        every { mockProductIdDataEntryViewModel.getInputString() } returns "someInputString"

                        uut.setUp()
                    }
                    it("calls .splitQtyDataEntryViewModel.setHintString()") {
                        verify { mockSplitQtyDataEntryViewModel.setHintString("QTY") }
                    }
                    it("calls .splitQtyDataEntryViewModel.setEndIconMode()") {
                        verify { mockSplitQtyDataEntryViewModel.setEndIconMode(TextInputLayout.END_ICON_NONE) }
                    }
                    it("calls .splitQtyDataEntryViewModel.setDataEntryFieldType()") {
                        verify {
                            mockSplitQtyDataEntryViewModel.setDataEntryFieldType(
                                DataEntryFieldType.NUMBER_ENTRY
                            )
                        }
                    }
                }
            }

            describe("when .validateProductId()") {
                beforeEach {
                    every { mockProduct.productId } returns "someProductId"
                }
                describe("when the productId is valid") {
                    beforeEach {
                        every { mockProduct.productId } returns "someProductId"
                        mockProductIdDataEntryViewModel = MockDataEntryViewModel().create()
                        every { mockProductIdDataEntryViewModel.setDataEntryFieldType(any()) } returns Unit
                        every { mockProductIdDataEntryViewModel.getInputString() } returns "someProductId"
                        uut.productIdDataEntryViewModel =
                            MutableLiveData(mockProductIdDataEntryViewModel)
                    }

                    describe("when setProductIdState()") {
                        beforeEach {
                            every { mockProduct.isSerial } returns false
                            uut.validateProductId()
                        }

                        it("calls .productIdDataEntryViewModel.setInputState(InputState.Success())") {
                            verify(exactly = 1) {
                                mockProductIdDataEntryViewModel.setInputState(withArg {
                                    it::class shouldBe InputState.Success::class
                                    it.message shouldBe "Valid Product ID!"
                                })
                            }
                        }
                    }

                    describe("when the product has serial numbers") {
                        val mockProductSerialNumberViewModels =
                            MutableLiveData<MutableList<DataEntryViewModel>>()
                        lateinit var mockSerial1: DataEntryViewModel
                        lateinit var mockSerial2: DataEntryViewModel

                        beforeEach {
                            every { mockProduct.isSerial } returns true
                            mockSerial1 = MockDataEntryViewModel().create()
                            every { mockSerial1.getInputString() } returns "serialAdded"
                            every { mockSerial1.entryIsBlank() } returns false
                            every { mockSerial1.getTextInputId() } returns 1234

                            mockSerial2 = MockDataEntryViewModel().create()
                            every { mockSerial2.getInputString() } returns null
                            every { mockSerial2.entryIsBlank() } returns true
                            every { mockSerial2.getTextInputId() } returns 5678

                            mockProductSerialNumberViewModels.value =
                                mutableListOf(mockSerial1, mockSerial2)
                            uut.productSerialNumberViewModels = mockProductSerialNumberViewModels
                            uut.validateProductId()
                        }

                        describe("when all serial numbers have been added") {
                            beforeEach {
                                mockProductSerialNumberViewModels.value = mutableListOf(mockSerial1)
                                uut.productSerialNumberViewModels =
                                    mockProductSerialNumberViewModels
                                uut.validateProductId()
                            }

                            it("calls .activityService.hideKeyboard()") {
                                verify { mockActivityService.hideKeyboard() }
                            }

                            describe("when setCardValidationState()") {
                                it("sets .cardValidationState to CardState.Ready()") {
                                    uut.cardValidationState.value!!::class shouldBe CardState.Ready()::class
                                }
                            }
                        }

                        describe("when all serial numbers have NOT been added") {
                            beforeEach {
                                val mockSerial: DataEntryViewModel =
                                    MockDataEntryViewModel().create()
                                every { mockSerial.entryIsBlank() } returns true
                                every { mockSerial.getTextInputId() } returns 1234
                                mockProductSerialNumberViewModels.value =
                                    mutableListOf(mockSerial, mockSerial)
                                mockProductIdDataEntryViewModel = MockDataEntryViewModel().create()
                                every { mockProductIdDataEntryViewModel.setDataEntryFieldType(any()) } returns Unit
                                every { mockProductIdDataEntryViewModel.getInputString() } returns "someProductId"
                                uut.productIdDataEntryViewModel =
                                    MutableLiveData(mockProductIdDataEntryViewModel)
                                uut.validateProductId()
                            }
                            describe("when setCardValidationState()") {
                                it("sets .cardValidationState to CardState.Default()") {
                                    uut.cardValidationState.value!!::class shouldBe CardState.Default()::class
                                }
                            }
                        }

                        it("calls the first serialNumberViewModel's .setInputState(InputState.Default())") {
                            verify(exactly = 1) {
                                mockSerial1.setInputState(withArg { state ->
                                    state::class shouldBe InputState.Default::class
                                })
                            }
                        }

                        describe("when .focusFirstSerial()") {
                            it("calls the empty serialNumberViewModel's .setInputFocusState(InputFocusState.Focussed) ") {
                                eventually(duration = delayDuration) {
                                    verify {
                                        mockSerial1.setInputFocusState(withArg { state ->
                                            state::class shouldBe InputFocusState.Focussed::class
                                        })
                                    }
                                }
                            }
                        }

                        describe("when .setCurrentQuantity()") {
                            it("sets .quantityEditString to totalAddedSerialNumbers() value") {
                                uut.quantityEditString.value shouldBe "1"
                            }
                        }
                    }

                    describe("when the product does NOT have serial numbers") {
                        beforeEach {
                            every { mockProduct.isSerial } returns false
                            uut.validateProductId()
                        }

                        it("calls .activityService.hideKeyboard()") {
                            verify { mockActivityService.hideKeyboard() }
                        }

                        describe("when .setCardValidationState()") {
                            it("sets .cardValidationState to CardState.Ready()") {
                                uut.cardValidationState.value!!::class shouldBe CardState.Ready()::class
                            }
                        }
                    }
                }

                describe("when the productId is NOT valid") {
                    beforeEach {
                        mockProductIdDataEntryViewModel = MockDataEntryViewModel().create()
                        every { mockProductIdDataEntryViewModel.setDataEntryFieldType(any()) } returns Unit
                        every { mockProductIdDataEntryViewModel.getInputString() } returns "someInvalidId"
                        uut.productIdDataEntryViewModel =
                            MutableLiveData(mockProductIdDataEntryViewModel)
                        uut.validateProductId()
                    }

                    describe("when .resetProdIdEntryString()") {
                        it("calls .productIdDataEntryViewModel.resetEntryString()") {
                            verify(exactly = 1) {
                                mockProductIdDataEntryViewModel.resetEntryString()
                            }
                        }
                    }

                    describe("when setProductIdState()") {
                        it("calls .productIdDataEntryViewModel.setInputState(InputState.Error())") {
                            verify(exactly = 1) {
                                mockProductIdDataEntryViewModel.setInputState(withArg { state ->
                                    state::class shouldBe InputState.Error::class
                                    state.message shouldBe "Product ID someInvalidId did not match. Please scan again or enter manually."
                                })
                            }
                        }
                    }

                    describe("when .setProductIdFocusState()") {
                        it("calls .productIdDataEntryViewModel.setInputFocusState() setting to Focussed") {
                            verify(exactly = 1) {
                                mockProductIdDataEntryViewModel.setInputFocusState(withArg {
                                    it shouldBe InputFocusState.Focussed
                                })
                            }
                        }
                    }
                }
            }
//        TODO: Later uncomment below code
//        describe("when .validateAllSerialNumbers()") {
//            val mockProductSerialNumberViewModels =
//                MutableLiveData<MutableList<DataEntryViewModel>>()
//            lateinit var mockSerial1: DataEntryViewModel
//            lateinit var mockSerial2: DataEntryViewModel
//
//            beforeEach {
//                every { mockProduct.productId } returns "someProductId"
//            }
//            describe("when serialNumber .canBeValidated() is true") {
//                describe("when the serial number is a duplicate") {
//
//                    beforeEach {
//                        mockSerial1 = MockDataEntryViewModel().create()
//                        every { mockSerial1.getInputString() } returns "someDuplicateValue"
//                        every { mockSerial1.canBeValidated() } returns true
//                        every { mockSerial1.entryIsBlank() } returns false
//                        mockSerial2 = MockDataEntryViewModel().create()
//                        every { mockSerial2.getInputString() } returns "someDuplicateValue"
//                        every { mockSerial2.canBeValidated() } returns true
//                        every { mockSerial2.entryIsBlank() } returns false
//
//                        mockProductSerialNumberViewModels.value =
//                            mutableListOf(mockSerial1, mockSerial2)
//                        uut.productSerialNumberViewModels = mockProductSerialNumberViewModels
//                        uut.validateAllSerialNumbers()
//                    }
//
//                    it("calls the serialNumberViewModel's .setInputState(InputState.Error())") {
//                        verify(exactly = 1) {
//                            mockSerial1.setInputState(withArg { state ->
//                                state::class shouldBe InputState.Error::class
//                                state.message shouldBe "You’ve already scanned this serial #. Please scan a different item."
//                            })
//                        }
//                    }
//                    it("calls the serialNumberViewModel's .resetEntryString()") {
//                        verify(exactly = 1) {
//                            mockSerial2.resetEntryString()
//                        }
//                    }
//                }
//
//                describe("when the serial number is a blank") {
//
//                    beforeEach {
//                        mockSerial1 = MockDataEntryViewModel().create()
//                        every { mockSerial1.canBeValidated() } returns true
//                        every { mockSerial1.entryIsBlank() } returns true
//
//                        mockProductSerialNumberViewModels.value = mutableListOf(mockSerial1)
//                        uut.productSerialNumberViewModels = mockProductSerialNumberViewModels
//                        uut.validateAllSerialNumbers()
//                    }
//
//                    it("calls the serialNumberViewModel's .setInputState(InputState.Error())") {
//                        verify(exactly = 1) {
//                            mockSerial1.setInputState(withArg { state ->
//                                state::class shouldBe InputState.Error::class
//                                state.message shouldBe "Serial number required"
//                            })
//                        }
//                    }
//                    it("calls the serialNumberViewModel's .resetEntryString()") {
//                        verify(exactly = 1) {
//                            mockSerial1.resetEntryString()
//                        }
//                    }
//                }
//
//                describe("when the serial number is not a duplicate or blank") {
//                    beforeEach {
//                        mockSerial1 = MockDataEntryViewModel().create()
//                        every { mockSerial1.getInputString() } returns "someValue"
//                        every { mockSerial1.canBeValidated() } returns true
//                        every { mockSerial1.entryIsBlank() } returns false
//                        mockSerial2 = MockDataEntryViewModel().create()
//                        every { mockSerial2.getInputString() } returns "someOtherValue"
//                        every { mockSerial2.canBeValidated() } returns true
//                        every { mockSerial2.entryIsBlank() } returns false
//
//                        mockProductSerialNumberViewModels.value =
//                            mutableListOf(mockSerial1, mockSerial2)
//                        uut.productSerialNumberViewModels = mockProductSerialNumberViewModels
//                        uut.validateAllSerialNumbers()
//                    }
//
//                    it("calls the serialNumberViewModel's .setInputState(InputState.Success())") {
//                        verify(exactly = 1) {
//                            mockSerial1.setInputState(withArg { state ->
//                                state::class shouldBe InputState.Success::class
//                                state.message shouldBe "Valid Serial #"
//                            })
//                        }
//                    }
//                }
//            }
//
//            describe("when setCurrentQuantity()") {
//                beforeEach {
//                    mockSerial1 = MockDataEntryViewModel().create()
//                    every { mockSerial1.getInputString() } returns "someValue"
//                    every { mockSerial1.canBeValidated() } returns true
//                    every { mockSerial1.entryIsBlank() } returns false
//                    mockSerial2 = MockDataEntryViewModel().create()
//                    every { mockSerial2.getInputString() } returns null
//                    every { mockSerial2.canBeValidated() } returns false
//                    every { mockSerial2.entryIsBlank() } returns true
//
//                    mockProductSerialNumberViewModels.value =
//                        mutableListOf(mockSerial1, mockSerial2)
//                    uut.productSerialNumberViewModels = mockProductSerialNumberViewModels
//                    uut.validateAllSerialNumbers()
//                }
//
//                it("sets .quantityEditString to totalAddedSerialNumbers() value") {
//                    uut.quantityEditString.value shouldBe "1"
//                }
//            }
//
//            describe("when focusNextSerialEntry(true)") {
//                beforeEach {
//                    mockSerial1 = MockDataEntryViewModel().create()
//                    every { mockSerial1.getInputString() } returns "someValue"
//                    every { mockSerial1.canBeValidated() } returns true
//                    every { mockSerial1.entryIsBlank() } returns false
//                    mockSerial2 = MockDataEntryViewModel().create()
//                    every { mockSerial2.getInputString() } returns null
//                    every { mockSerial2.canBeValidated() } returns true
//                    every { mockSerial2.entryIsBlank() } returns true
//
//                    mockProductSerialNumberViewModels.value =
//                        mutableListOf(mockSerial1, mockSerial2)
//                    uut.productSerialNumberViewModels = mockProductSerialNumberViewModels
//                    uut.validateAllSerialNumbers()
//                }
//
//                it("calls the current serialNumberViewModel's .setInputFocusState(InputFocusState.Focussed) ") {
//                    verify(exactly = 1) {
//                        mockSerial2.setInputFocusState(withArg {
//                            it::class shouldBe InputFocusState.Focussed::class
//                        })
//                    }
//                }
//            }
//
//            describe("when productId is valid AND all serial numbers have been added") {
//                beforeEach {
//                    mockSerial1 = MockDataEntryViewModel().create()
//                    every { mockSerial1.getInputString() } returns "someValue"
//                    every { mockSerial1.canBeValidated() } returns true
//                    every { mockSerial1.entryIsBlank() } returns false
//
//                    every { mockProduct.productId } returns "someProductId"
//                    mockProductIdDataEntryViewModel = MockDataEntryViewModel().create()
//                    every { mockProductIdDataEntryViewModel.getInputString() } returns "someProductId"
//                    uut.productIdDataEntryViewModel =
//                        MutableLiveData(mockProductIdDataEntryViewModel)
//
//                    mockProductSerialNumberViewModels.value = mutableListOf(mockSerial1)
//                    uut.productSerialNumberViewModels = mockProductSerialNumberViewModels
//                    uut.validateAllSerialNumbers()
//                }
//
//                it("calls .activityService.hideKeyboard()") {
//                    verify { mockActivityService.hideKeyboard() }
//                }
//
//                describe("when setCardValidationState()") {
//                    it("sets .cardValidationState to CardState.Ready()") {
//                        uut.cardValidationState.value!!::class shouldBe CardState.Ready()::class
//                    }
//                }
//            }
//
//            describe("when productId is not valid or all serial numbers have not been added") {
//                beforeEach {
//                    mockSerial1 = MockDataEntryViewModel().create()
//                    every { mockSerial1.getInputString() } returns "someValue"
//                    every { mockSerial1.canBeValidated() } returns true
//                    every { mockSerial1.entryIsBlank() } returns true
//
//                    every { mockProduct.productId } returns "someProductId"
//                    mockProductIdDataEntryViewModel = MockDataEntryViewModel().create()
//                    every { mockProductIdDataEntryViewModel.getInputString() } returns "someInvalidProductId"
//                    uut.productIdDataEntryViewModel =
//                        MutableLiveData(mockProductIdDataEntryViewModel)
//
//                    mockProductSerialNumberViewModels.value = mutableListOf(mockSerial1)
//                    uut.productSerialNumberViewModels = mockProductSerialNumberViewModels
//                    uut.validateAllSerialNumbers()
//                }
//
//                describe("when setCardValidationState()") {
//                    it("sets .cardValidationState to CardState.Default()") {
//                        uut.cardValidationState.value!!::class shouldBe CardState.Default()::class
//                    }
//                }
//            }
//        }

            describe("when .onEditorAction()") {
                describe("when the enter button is tapped OR IME_ACTION_NEXT") {
//                TODO: Later uncomment below code
//                describe("when the passed TextView is a serial number input") {
//                    val mockProductSerialNumberViewModels =
//                        MutableLiveData<MutableList<DataEntryViewModel>>()
//                    lateinit var mockSerial: DataEntryViewModel
//                    lateinit var mockTextView: TextView
//                    lateinit var mockKeyEvent: KeyEvent
//
//                    beforeEach {
//                        every { mockProduct.productId } returns "someProductId"
//                        mockTextView = mockk {
//                            every { id } returns 1234
//                            every { setNextFocusDownId(any()) } returns Unit
//                        }
//                        mockKeyEvent = mockk {
//                            every { keyCode } returns KeyEvent.KEYCODE_ENTER
//                            every { action } returns KeyEvent.ACTION_UP
//                        }
//                        mockSerial = MockDataEntryViewModel().create()
//                        every { mockSerial.getTextInputId() } returns mockTextView.hashCode()
//                        every { mockSerial.canBeValidated() } returns true
//                        every { mockSerial.entryIsBlank() } returns true
//
//                        mockProductSerialNumberViewModels.value = mutableListOf(mockSerial)
//                        uut.productSerialNumberViewModels = mockProductSerialNumberViewModels
//
//                        uut.onEditorAction(mockTextView, 0, mockKeyEvent)
//                    }
//
//                    it("calls .nextFocusDownId for the passed TextView") {
//                        verify { mockTextView.setNextFocusDownId(1234) }
//                    }
//
//                    describe("when .validateAllSerialNumbers()") {
//                        describe("when serialNumber .canBeValidated() is true") {
//                            describe("when the serial number is a blank") {
//                                it("calls the serialNumberViewModel's .setInputState(InputState.Error())") {
//                                    verify(exactly = 1) {
//                                        mockSerial.setInputState(withArg { state ->
//                                            state::class shouldBe InputState.Error::class
//                                            state.message shouldBe "Serial number required"
//                                        })
//                                    }
//                                }
//                                it("calls the serialNumberViewModel's .resetEntryString()") {
//                                    verify(exactly = 1) {
//                                        mockSerial.resetEntryString()
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }

                    describe("when the passed TextView is NOT serial number input") {
                        lateinit var mockTextView: TextView
                        lateinit var mockKeyEvent: KeyEvent

                        beforeEach {
                            every { mockProduct.productId } returns "someProductId"
                            mockTextView = mockk {
                                every { id } returns 1234
                                every { setNextFocusDownId(any()) } returns Unit
                            }
                            mockKeyEvent = mockk {
                                every { keyCode } returns KeyEvent.KEYCODE_ENTER
                                every { action } returns KeyEvent.ACTION_UP
                            }

                            mockProductIdDataEntryViewModel = MockDataEntryViewModel().create()
                            every { mockProductIdDataEntryViewModel.getInputString() } returns "someInvalidId"
                            uut.productIdDataEntryViewModel =
                                MutableLiveData(mockProductIdDataEntryViewModel)

                            uut.onEditorAction(mockTextView, 0, mockKeyEvent)
                        }

                        describe("when .validateProductId()") {
                            describe("when the productId is NOT valid") {

                                describe("when setProductIdState()") {
                                    it("calls .productIdDataEntryViewModel.setInputState(InputState.Error())") {
                                        verify(exactly = 1) {
                                            mockProductIdDataEntryViewModel.setInputState(withArg { state ->
                                                state::class shouldBe InputState.Error::class
                                                state.message shouldBe "Product ID someInvalidId did not match. Please scan again or enter manually."
                                            })
                                        }
                                    }
                                }

                                describe("when .resetProdIdEntryString()") {
                                    it("calls .productIdDataEntryViewModel.resetEntryString()") {
                                        verify(exactly = 1) {
                                            mockProductIdDataEntryViewModel.resetEntryString()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            describe("when .onTextChanged()") {
                describe("when the cardstate is NOT in error, it is a serial number entry, the serial entry is not in error state, and the text count is 0") {
                    val mockProductSerialNumberViewModels =
                        MutableLiveData<MutableList<DataEntryViewModel>>()
                    lateinit var mockSerial: DataEntryViewModel

                    beforeEach {
                        every { mockProduct.productId } returns "someProductId"
                        mockSerial = MockDataEntryViewModel().create()
                        every { mockSerial.getTextInputId() } returns 12345
                        every { mockSerial.canBeValidated() } returns true
                        every { mockSerial.entryIsBlank() } returns true
                        every { mockSerial.getInputStateValue() } returns InputState.Default()

                        mockProductSerialNumberViewModels.value = mutableListOf(mockSerial)
                        uut.productSerialNumberViewModels = mockProductSerialNumberViewModels

                        uut.cardValidationState.value = CardState.Default()
                        uut.onTextChanged(12345, mockk(), 0, 0, 0)
                    }
//                TODO: Later uncomment below code
//                describe("when .validateAllSerialNumbers()") {
//                    describe("when serialNumber .canBeValidated() is true") {
//                        describe("when the serial number is a blank") {
//                            it("calls the serialNumberViewModel's .setInputState(InputState.Error())") {
//                                verify(exactly = 1) {
//                                    mockSerial.setInputState(withArg { state ->
//                                        state::class shouldBe InputState.Error::class
//                                        state.message shouldBe "Serial number required"
//                                    })
//                                }
//                            }
//                            it("calls the serialNumberViewModel's .resetEntryString()") {
//                                verify(exactly = 1) {
//                                    mockSerial.resetEntryString()
//                                }
//                            }
//                        }
//                    }
//                }
                }
            }

            describe("when .submitButtonTapped()") {
                beforeEach {
                    every { mockProduct.isSerial } returns false
                    uut.submitButtonTapped()
                }

                it("calls .activityService.hideKeyboard()") {
                    verify { mockActivityService.hideKeyboard() }
                }

                describe("when .setCardValidationState()") {
                    it("sets .cardValidationState to CardState.Loading()") {
                        uut.cardValidationState.value!!::class shouldBe CardState.Loading()::class
                    }
                }

                describe("when product has serial numbers") {
                    val mockProductSerialNumberViewModels =
                        MutableLiveData<MutableList<DataEntryViewModel>>()
                    lateinit var mockSerial: DataEntryViewModel

                    beforeEach {
                        every { mockProduct.isSerial } returns true

                        mockSerial = MockDataEntryViewModel().create()
                        every { mockSerial.getInputString() } returns "someSerialNumber"
                        every { mockSerial.entryIsBlank() } returns false

                        mockProductSerialNumberViewModels.value = mutableListOf(mockSerial)
                        uut.productSerialNumberViewModels = mockProductSerialNumberViewModels

                        uut.submitButtonTapped()
                    }

                    it("calls .onItemClickedIsSerial()") {
                        onItemClickedIsSerialWasCalled shouldBe true
                    }
                    describe("that method's args") {
                        describe("the userPick") {
                            it("is the product") {
                                passedProduct shouldBeSameInstanceAs mockProduct
                            }
                        }
                        describe("the serialNumbers") {
                            describe("when .getAllSerialNumbers()") {
                                it("contains a SerialLineInput") {
                                    passedSerialNumbers.count() shouldBe 1
                                }
                                describe("that SerialLineInput") {
                                    it("has a line value of 1") {
                                        passedSerialNumbers.first().line shouldBe 1
                                    }
                                    it("has a serial value of 'someSerialNumber'") {
                                        passedSerialNumbers.first().serial shouldBe "someSerialNumber"
                                    }
                                }
                            }
                        }
                        describe("when .failureCallback()") {
                            beforeEach {
                                every { mockSerial.entryIsBlank() } returns true
                                passedFailureCallback()
                            }
                            describe("when setCardValidationState()") {
                                it("sets .cardValidationState to CardState.Error()") {
                                    uut.cardValidationState.value!!::class shouldBe CardState.Error::class
                                }
                            }
                            describe("when .removeSerialNumbers()") {
                                it("calls the serialNumber's .productIdDataEntryViewModel.resetEntryString()") {
                                    verify(exactly = 1) {
                                        mockSerial.resetEntryString()
                                    }
                                }
                                it("calls serialNumber's .setInputState(InputState.Error())") {
                                    verify(exactly = 1) {
                                        mockSerial.setInputState(withArg {
                                            it::class shouldBe InputState.Error::class
                                            it.message shouldBe "Serial Numbers Reset"
                                        })
                                    }
                                }
                            }

                            describe("when .setCurrentQuantity()") {
                                it("sets .quantityEditString to the product's quantity") {
                                    uut.quantityEditString.value shouldBe "0"
                                }
                            }

                            describe("when focusNextSerialEntry()") {
                                it("calls the empty serialNumberViewModel's .setInputFocusState(InputFocusState.Focussed) ") {
                                    verify(exactly = 1) {
                                        mockSerial.setInputFocusState(withArg { state ->
                                            state::class shouldBe InputFocusState.Focussed::class
                                        })
                                    }
                                }
                            }
                        }
                    }
                }

                describe("when product does not have serial numbers") {
                    beforeEach {
                        every { mockProduct.isSerial } returns false
                        uut.submitButtonTapped()
                    }

                    it("calls .onItemClicked()") {
                        onItemClickedWasCalled shouldBe true
                    }
                    describe("that method") {
                        describe("the userPick") {
                            it("is the product") {
                                passedProduct shouldBeSameInstanceAs mockProduct
                            }
                        }
                        describe("when .failureCallback()") {
                            beforeEach {
                                passedFailureCallback()
                            }
                            describe("when setCardValidationState()") {
                                it("sets .cardValidationState to CardState.Error()") {
                                    uut.cardValidationState.value!!::class shouldBe CardState.Error::class
                                }
                            }
                        }
                    }
                }
            }

            describe("when .setSerialHolderPos()") {
                beforeEach {
                    uut.setSerialHolderPos(yPos = 100)
                }

                it("sets .serialNumbersHolderYPos to passed value of 100") {
                    uut.serialNumbersHolderYPos shouldBe 100
                }
            }

        }
    }
})
