package com.reece.pickingapp.viewmodel

import android.content.Context.PRINT_SERVICE
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.reece.pickingapp.R
import com.reece.pickingapp.models.SplitQtyDTO
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.emptyString
import com.reece.pickingapp.view.adapter.ReportProductsAdapter
import com.reece.pickingapp.wrappers.ReportAdapterWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class ReportFragmentViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val reportAdapterWrapper: ReportAdapterWrapper = ReportAdapterWrapper(),
    private val activityService: ActivityService
) : ViewModel() {
    val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }

    private var productModel: ProductModel? = null
    private lateinit var viewLifecycleOwner: LifecycleOwner
    val reportProductAdapter = MutableLiveData<ReportProductsAdapter>()
    var currentDate = MutableLiveData(emptyString)
    var orderDate = MutableLiveData(emptyString)
    var orderNumber = MutableLiveData(emptyString)
    var customerInfo = MutableLiveData(emptyString)
    val product = MutableLiveData<ProductModel>()
    lateinit var navigateAfterStagingCallback: () -> Unit
    private lateinit var mWebView: WebView
    lateinit var productsSaved: SplitQtyDTO
    fun setUp(
        lifecycleOwner: LifecycleOwner,
        navigationForStagingCallback: () -> Unit
    ) {
        viewLifecycleOwner = lifecycleOwner
        getProductsSaved()
        navigateAfterStagingCallback = navigationForStagingCallback
    }

    fun shareButtonTapped() {
        shareText(getShareText())
    }

    fun printButtonTapped() {
        val html = generateLocalHTML()
        doWebViewPrint(html)
    }

    fun doneButtonTapped() {
        navigateAfterStagingCallback()
    }

    private fun getProductsSaved() {
        productsSaved = userPreferences.getSplitQty()
        if (productsSaved.productsList.isNotEmpty()) {
            productsSaved.let {
                productModel = productsSaved.productsList.first()
                //set UI
                val startPicked = productModel?.getProductDTO()?.startPickTime
                orderDate.value = startPicked
                currentDate.value = SimpleDateFormat(activityService.getString(R.string.format_current_date)).format(Date())
                orderNumber.value = productModel?.orderId
                customerInfo.value = productModel?.customerName
                //set adapter
                reportProductAdapter.value = productModel?.let { it1 ->
                    reportAdapterWrapper.createAdapter(
                        activityService
                    )
                }
                val list = mutableListOf<ReportProductViewHolderViewModel>()
                for (product in productsSaved.productsList) {
                    product?.let {
                        val viewHolderViewModel =
                            ReportProductViewHolderViewModel(it, viewLifecycleOwner)
                        list.add(viewHolderViewModel)
                    }
                }
                reportProductAdapter.value?.submitList(list)
                reportProductAdapter.value?.notifyDataSetChanged()
            }
        }
        //delete data
        userPreferences.deleteSplitQty()
    }

     fun generateLocalHTML(): String {
         Log.i(TAG,"generateLocalHTML()")

         //Init htmlDocument
         var htmlDocument = "<!DOCTYPE html>" +
                            "<html>"

         //Styles for all the document
         val headerAndStyles =
                 "<head>" +
                 "<style>" +
                 "@import url('https://fonts.googleapis.com/css2?family=Roboto&display=swap');" +
                 "p, h1, h2{" +
                 "font-family: 'Roboto', sans-serif;" +
                 "}" +
                 "p{" +
                 "  font-size: 12px;" +
                 "  font-family: 'Roboto';" +
                 "font-style: normal;" +
                 "font-weight: 400;" +
                 "line-height: 130%;" +
                 "}" +
                 "h1{" +
                 "font-size: 21px;" +
                 "font-style: normal;" +
                 "font-weight: 700;" +
                 "line-height: 18px;" +
                 "}" +
                 "h2{" +
                 "font-family: 'Roboto';" +
                 "font-style: normal;" +
                 "font-weight: 700;" +
                 "font-size: 12px;" +
                 "line-height: 17px;" +
                 "padding: 20px 0px 0px 0px;" +
                 "}" +
                 "table, td, th {  " +
                 "  border: 1px solid #000;" +
                 "  text-align: center;" +
                 "  font-family: 'Roboto', sans-serif;" +
                 "  vertical-align: top" +
                 "}" +
                 "th{" +
                 "font-family: 'Roboto';" +
                 "font-style: bold;" +
                 "font-size: 12px;" +
                 "line-height: 17px;" +
                 "font-weight: bold;" +
                 "}" +
                 "td{" +
                 "font-family: 'Roboto';" +
                 "font-style: normal;" +
                 "font-weight: 400;" +
                 "font-size: 12px;" +
                 "line-height: 17px;" +
                 "}" +
                 "table {" +
                 "  border-collapse: collapse;" +
                 "  width: 100%;" +
                 "}" +
                 "th, td {" +
                 "  padding: 8px 0px 8px 0px;" +
                 "}" +
                 ".table-left{" +
                 "  text-align: left;" +
                 "  padding: 8px 0px 8px 20px;" +
                 "}" +
                 "html{" +
                 "padding: 30px;" +
                 "}" +
                 "</style>" +
                 "</head>"

         //Init body default information (Headers)
         var body = "<body>" +
                 "<p>${SimpleDateFormat(activityService.getString(R.string.format_current_date)).format(Date())}</p>" +
                 "<h1>Report</h1>" +
                 "<p>These items need to be updated in ERP by a manager.</p>" +
                 "<table>" +
                 "  <tr>" +
                 "    <th>CUSTOMER</th>" +
                 "    <th>ORDER NUMBER</th>" +
                 "    <th>ORDER DATE</th>" +
                 "  </tr>"

         // add general product information to the body
         body += "  <tr>" +
                 "    <td class=\"table-left\">${productsSaved.productsList.first()?.customerName?:""}</td>" +
                 "    <td>${productsSaved.productsList.first()?.product?.orderId?:""}</td>" +
                 "    <td>${productsSaved.productsList.first()?.product?.startPickTime?:""}</td>" +
                 "  </tr>" +
                 "</table>"


         //Iterate within the products

         var productsList =""
         var productCounter = 1;
         productsSaved.productsList.forEach {

             var thisProductTable = "<table>" //init this product table
             //Set h2 Title
             thisProductTable += "<h2>Product ${productCounter}</h2>"

             //Set this product table headers row
             thisProductTable +=
                        "  <tr>" +
                        "    <th>SHIP QTY</th>" +
                        "    <th>PICKED QTY</th>" +
                        "    <th>DESCRIPTION</th>" +
                        "  </tr>"

             //Set product information row
             thisProductTable += "<tr>" +
                         "    <td>${it?.quantity?:"0"}</td>" +
                         "    <td>${it?.qtyPicked?:"0"}</td>" +
                         "    <td class=\"table-left\">" +
                         "      ${it?.description}<br>" +
                         "      PN: ${it?.productId?:"0"}<br>" +
                         "      Loc: ${it?.location?:""}<br>"

             //Iterate serial numbers in the default location (if any on qtyPicked)
             it?.defaultLocationSerialLineInput?.forEach {
                 thisProductTable += "Serial Number: ${it.serial?:""}<br>"
             }

             //Close general product Information TD and row
             thisProductTable +=
                            "    </td>" +
                            "  </tr>"

             //Set ALT. LOCATIONS header title row
             thisProductTable +=
                         "  <tr>" +
                         "    <td></td>" +
                         "    <td></td>" +
                         "    <th>ALT. LOCATIONS</th>" +
                         "  </tr>"

             //Iterate in all alternate locations with their serial numbers (if Any)
             var alternateLocationsRow = "  <tr>" +
                                            "    <td></td>" //this td is empty by default (Ship qty column)

             //init qty and alternate loc with serials TD
             var alternateLocationsQtyTD = "<td>" //init alternateLocationsQtyTD opening TD
             var alternateLocationsAndSerialsTD = "<td class=\"table-left\">" //init alternateLocationsAndSerialsTD opening TD
             var myPosition = 1
             //concat al locations
             it?.alternateLocationDTO?.forEach { locationDTO ->
                 //validate if the product is serial
                 alternateLocationsQtyTD += locationDTO.qty
                 alternateLocationsAndSerialsTD += (activityService.getString(
                     R.string.label_location,
                     myPosition.toString()
                 ))
                 alternateLocationsAndSerialsTD += " "
                 alternateLocationsAndSerialsTD += (locationDTO.location)
                 alternateLocationsAndSerialsTD += "<br>"
                 alternateLocationsQtyTD += "<br>"
                 if (!locationDTO.serialNumbers.isNullOrEmpty()) {
                     //get location
                     alternateLocationsQtyTD += "<br>"
                     alternateLocationsAndSerialsTD += "<br>"
                     alternateLocationsAndSerialsTD += (activityService.getString(R.string.label_serial_number))
                     locationDTO.serialNumbers.forEach { _serialNumber ->
                         alternateLocationsAndSerialsTD += "<br>"
                         alternateLocationsQtyTD += "<br>"
                         alternateLocationsAndSerialsTD += _serialNumber
                     }
                     alternateLocationsAndSerialsTD += "<br>"
                     alternateLocationsQtyTD += "<br>"
                     alternateLocationsAndSerialsTD += "<br>"
                     alternateLocationsQtyTD += "<br>"
                 }
                 myPosition++
             }

             //Verify Back Order flag
             if(it?.isBackOrder == true){
                 alternateLocationsAndSerialsTD +=  "      <b>BACK ORDER NEEDED</b>"
             }


             //Close alternateLocationsQtyTD
             //If there are not alternate locations, picked qty column stays empty
             alternateLocationsQtyTD += "    </td>"

             //Close alternateLocationsAndSerialsTD
             alternateLocationsAndSerialsTD += "    </td>"


             // Concat alternate locations row with qty TD and serials TD
             alternateLocationsRow = alternateLocationsRow + alternateLocationsQtyTD + alternateLocationsAndSerialsTD

             // Close alternate locations row
             alternateLocationsRow += "  </tr>"

             //Add alternateLocationsRow to this product
             thisProductTable+= alternateLocationsRow


            //Close this product table
             thisProductTable+= "</table>"
             //Add this product to the products list
             productsList += thisProductTable
            //Increment product counter index
             productCounter++

         }
         body += productsList

         //Close body
         body += "</body>"
         //Concat and close htmlDocument
         htmlDocument = htmlDocument + headerAndStyles + body
         htmlDocument += "</html>"

         return  htmlDocument
    }

    private fun doWebViewPrint( htmlDocument: String) {
        // Create a WebView object specifically for printing
        val webView = activityService.activity?.let { WebView(it) }
        webView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) =
                false

            override fun onPageFinished(view: WebView, url: String) {
                Log.i(TAG, "page finished loading $url")
                createWebPrintJob(view)
            }
        }
        // Generate an HTML document on the fly:
        webView?.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null)
        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        if (webView != null) {
            mWebView = webView
        }
    }
    private fun createWebPrintJob(webView: WebView) {
        (activityService.activity?.getSystemService(PRINT_SERVICE) as? PrintManager)?.let { printManager ->
            val fileName = "Report_" + productsSaved.productsList.first()?.product?.orderId
            val printAdapter = webView.createPrintDocumentAdapter(fileName)
            printManager.print(
                fileName,
                printAdapter,
                PrintAttributes.Builder().build()
            )
        }
    }

    private fun getShareText():String {
        val nextLine = "\n"

        // Init with general order information
        var shareText = "${SimpleDateFormat(activityService.getString(R.string.format_current_date)).format(Date())}" + nextLine+
                "Report" + nextLine+
                "These items need to be updated in ERP by a manager." + nextLine +
                "CUSTOMER: ${productsSaved.productsList.first()?.customerName?:""}" + nextLine +
                "ORDER NUMBER: ${productsSaved.productsList.first()?.product?.orderId?:""}" + nextLine+
                "ORDER DATE: ${productsSaved.productsList.first()?.product?.startPickTime?:""}" + nextLine + nextLine

        // Iterate on products list

        var productsList =""
        var productCounter = 1;
        productsSaved.productsList.forEach {

            var thisProductTable = "" //init this product table
            //Set h2 Title
            thisProductTable += "Product ${productCounter}" + nextLine

            //Set this product table headers row
            thisProductTable +=
                        "SHIP QTY: ${it?.quantity?:"0"} " + nextLine +
                        "PICKED QTY: ${it?.qtyPicked?:"0"}" + nextLine +
                        "DESCRIPTION: ${it?.description}" + nextLine +
                        "PN: ${it?.productId?:"0"}" +nextLine +
                        "Loc: ${it?.location?:""}" + nextLine

            //Iterate serial numbers in the default location (if any on qtyPicked)
            if (!it?.defaultLocationSerialLineInput.isNullOrEmpty()){
                thisProductTable += "Serial Number(s)"
            }
            it?.defaultLocationSerialLineInput?.forEach { thisSerial ->
                thisProductTable += thisSerial.serial + nextLine
            }
            //Close default location block
            thisProductTable += nextLine


            //Set ALT. LOCATIONS header title row
            var alternateLocationsRow = "ALT. LOCATIONS$nextLine"

            //Iterate in all alternate locations with their serial numbers (if Any)
            //init qty and alternate loc with serials TD
            var alternateLocationsAndSerialsTD = "" //init alternateLocationsAndSerialsTD opening TD
            var myPosition = 1
            //concat al locations
            it?.alternateLocationDTO?.forEach { locationDTO ->
                //validate if the product is serial
                alternateLocationsAndSerialsTD += "QTY: (${locationDTO.qty})   "
                alternateLocationsAndSerialsTD += (activityService.getString(
                    R.string.label_location,
                    myPosition.toString()
                ))
                alternateLocationsAndSerialsTD += (locationDTO.location)
                alternateLocationsAndSerialsTD += nextLine
                if (!locationDTO.serialNumbers.isNullOrEmpty()) {
                    //get location
                    alternateLocationsAndSerialsTD += (activityService.getString(R.string.label_serial_number))
                    locationDTO.serialNumbers.forEach { _serialNumber ->
                        alternateLocationsAndSerialsTD += nextLine
                        alternateLocationsAndSerialsTD += _serialNumber
                    }
                    alternateLocationsAndSerialsTD += nextLine
                    alternateLocationsAndSerialsTD += nextLine
                }
                myPosition++
            }

            //Verify Back Order flag
            if(it?.isBackOrder == true){
                alternateLocationsAndSerialsTD += "BACK ORDER NEEDED"
            }
            // Concat alternate locations row with qty TD and serials TD
            alternateLocationsRow += alternateLocationsAndSerialsTD
            //Add alternateLocationsRow to this product
            thisProductTable+= alternateLocationsRow
            //Add this product to the products list
            productsList += thisProductTable
            //Increment product counter index
            productCounter++

        }

        shareText+= productsList

        return shareText
    }

    private fun shareText(textToShare: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare)
        sendIntent.type = "text/plain"
        val shareIntent = Intent.createChooser(sendIntent, null)
        activityService.activity?.startActivity(shareIntent)
    }
}
