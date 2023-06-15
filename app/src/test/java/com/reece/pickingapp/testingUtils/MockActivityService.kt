package com.reece.pickingapp.testingUtils

import com.reece.pickingapp.R
import com.reece.pickingapp.utils.ActivityService
import io.mockk.every
import io.mockk.mockk

class MockActivityService {
    fun create(): ActivityService {
        return mockk {
            every { showMessage(any(), any()) } returns Unit
            every { hideKeyboard() } returns Unit
            every { getString(R.string.product_id_label_string) } returns "Product ID"
            every { getString(R.string.valid_product_id) } returns "Valid Product ID!"
            every {
                getString(
                    R.string.invalid_product_id,
                    any()
                )
            } returns "Product ID someInvalidId did not match. Please scan again or enter manually."
            every { getString(R.string.duplicate_serial_message) } returns "You’ve already scanned this serial #. Please scan a different item."
            every { getString(R.string.serial_number_blank) } returns "Serial number required"
            every { getString(R.string.serial_num_valid) } returns "Valid Serial #"
            every { getString(R.string.product_id_label_string) } returns "Product ID"
            every { getString(R.string.serial_num_reset) } returns "Serial Numbers Reset"
            every { getString(R.string.error_fetch_product) } returns "Error fetching products"
            every { getString(R.string.unknown) } returns "Unknown error. Check the logs for trace"
            every { getString(R.string.order_stage_success) } returns "Order successfully staged!"
            every { getString(R.string.error_serial_unique) } returns "Error submitting Serial #. Please verify you are using unique values."
            every { getString(R.string.order_number_spannable, any()) } returns "someSpannable"
            every { getString(R.string.customer_name_spannable, any()) } returns "someSpannable"
            every { getString(R.string.authorization_expired) } returns "Authorization expired. Please log in again."
            every { getString(R.string.confirm_order_message) } returns "Are you ready to start this order?"
            every { getString(R.string.cancel) } returns "Cancel"
            every { getString(R.string.ready_to_start) } returns "READY TO START"
            every { getString(R.string.blank_field_error) } returns "Field cannot be blank."
            every { getString(R.string.staging_location_hint) } returns "Staging Location *"
            every { getString(R.string.prompt_hint) } returns "Email Address"
            every { getString(R.string.operation_cancelled) } returns "Operation cancelled"
            every { getString(R.string.eclipse_username_hint) } returns "Enter Username"
            every { getString(R.string.eclipse_password_hint) } returns "Enter Password"
            every { getString(R.string.boxes) } returns "Boxes"
            every { getString(R.string.skids) } returns "Skids"
            every { getString(R.string.bundles) } returns "Bundles"
            every { getString(R.string.pieces) } returns "Pieces"
            every { getString(R.string.branch_id) } returns "Branch ID"
            every { getString(R.string.product_list_error) } returns "Product loading Error. Pull to refresh"
            every {
                getString(
                    R.string.branch_missing_error,
                    any()
                )
            } returns "Branch Id is a minimum of 3 digits"
            every { getString(R.string.product_quantity_label_string) } returns "QTY"
            every { getString(R.string.split_qty_serial_validation_error) } returns "Quantity does not match the number of serial numbers scanned. Please try again."
            every { getString(R.string.split_qty_validation_error) } returns "Quantity must be less than required amount."


            every { getInteger(R.integer.mincron_branch_id_length_minimum) } returns 3
            every { getString(R.string.show_more) } returns "Show more"
            every { getString(R.string.show_less) } returns "Show less"
            every { getString(R.string.shipping_instruction) } returns "Shipping Instructions: "
            every { getString(R.string.app_version, any()) } returns "1.1.0"
        }
    }
}