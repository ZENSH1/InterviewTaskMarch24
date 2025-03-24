package com.xs.carappinterviewtask.utils.objects

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.xs.carappinterviewtask.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToLong

object ExtensionUtils {
    var windowInsets: Insets? = null

    fun String.completeHtml(): String {
        return """
             <html>
            <head>
                <meta charset="UTF-8">
                <title>Document</title>
            </head>
            <body>
               <p> $this </p>
            </body>
            </html>
        """
    }

    fun Context.getSdpValue(sdpRes: Int): Int {
        return resources.getDimensionPixelSize(sdpRes)
    }

    /**
     * Applies special styling to a portion of text within a TextView.
     * This function makes the specified `specialText` bold and changes its color,
     * allowing it to stand out from the rest of the `fullText`. Additionally, it can
     * set a click listener for the special text.
     *
     * @param fullText The complete text to be displayed in the TextView.
     * @param specialText The text to be styled as special (bold and clickable).
     * @param onClick Optional lambda function to be executed when the special text is clicked.
     *                If provided, it will be triggered when the special text is clicked.
     */
    fun TextView.specialTextStyleOnClick(
        fullText: String, specialText: String,
        onClick: (() -> Unit)? = null) {
        val spannableString = SpannableString(fullText)
        val startIndex = fullText.indexOf(specialText)
        val endIndex = startIndex + specialText.length

        // Make "specialText" bold
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Index not found Error
        if (startIndex == -1) {
            throw Exception("Special Text not found")
        }
        spannableString.setSpan(
            ForegroundColorSpan(context.getColor(R.color.app_color)),
            startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set Spanned text to TextView
        text = spannableString
    }

    fun Context.isPackageInstalled(packageName: String?): Boolean {
        return safeReturn(false) {
            if (packageName != null) {
                packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                true
            } else {
                false
            }
        }
    }

    fun Long.getTime(isForPref: Boolean = false): String {
        return try {
            val dateFormat = if (isForPref) SimpleDateFormat("HH mm a", Locale.getDefault())
            else SimpleDateFormat("HH : mm a", Locale.getDefault())
            // Convert the Long timestamp to a Date object
            val date = Date(this) // Multiply by 1000 to convert seconds to milliseconds
            // Format the Date object as a string
            dateFormat.format(date)
        } catch (e: Exception) {
            "00:00"
        }
    }

    fun Long.toDateString(): String {
        // Define the format of the date string
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            // Convert the Long timestamp to a Date object
            val date = Date(this) // Multiply by 1000 to convert seconds to milliseconds
            // Format the Date object as a string
            dateFormat.format(date)
        } catch (e: Exception) {
            "00:00"
        }
    }

    fun Long.toDateString(format: String): String {
        // Define the format of the date string
        return try {
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            // Convert the Long timestamp to a Date object
            val date = Date(this) // Multiply by 1000 to convert seconds to milliseconds
            // Format the Date object as a string
            dateFormat.format(date)
        } catch (e: Exception) {
            "00:00"
        }
    }

    fun Long.videoNameString(): String {
        // Define the format of the date string
        return try {
            val format = "yyyy-MM-dd-HH-mm-ss"
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            // Convert the Long timestamp to a Date object
            val date = Date(this) // Multiply by 1000 to convert seconds to milliseconds
            // Format the Date object as a string
            dateFormat.format(date)
        } catch (e: Exception) {
            toString()
        }
    }

    /**Trims extra spaces around characters in a string.
     * @return trimmed String
     * */
    fun String.trimExtraSpaces(): String {
        return split("\\s+".toRegex()).joinToString(" ").trim()
    }

    fun Long.getSizeAsString(): String {
        var sizeType = "KB"
        var sizeValue = (this / 1024f).roundToLong()
        if (sizeValue > 1024) {
            sizeValue = (sizeValue / 1024f).roundToLong()
            sizeType = "MB"
            if (sizeValue > 1024) {
                sizeType = "GB"
                sizeValue = (sizeValue / 1024f).roundToLong()
            }
        }
        return "$sizeValue $sizeType"
    }

    fun Pair<Long, String>.string(): String {
        return "$first $second"
    }

    fun Long.formatByteSize(): String {
        val size = this
        if (size <= 0) return ""
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow((digitGroups.toDouble()))) + " " + units[digitGroups]
    }

    /** Extension Function
     * - Convert File to MultipartBody.Part
     * @return MultipartBody.Part
     * @receiver File
     * @author Zain Sherazi - 622
     * */
    fun File.toMultipartBodyPart(name: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name, this.name, this.asRequestBody("image/*".toMediaTypeOrNull())
        )
    }

    fun Double.formatToTwoDecimalPoints(): String {
        val df = DecimalFormat(DECIMAL_POINT_FORMAT)
        return df.format(this)
    }

    fun Double.formatKMToMetersDecimalPoints(): String {
        val meters = this * 1000
        val df = DecimalFormat(DECIMAL_POINT_FORMAT)
        return df.format(meters)
    }

    private const val DECIMAL_POINT_FORMAT = "#.#"
    private const val TWO_DECIMAL_POINT_FORMAT = "#.##"

    /**Check if the string is null or empty, if it is, perform the action
     * @param perform : Action to be performed if the string is null or empty
     * @return String
     * @author: Zain Sherazi
     * */
    inline fun String?.ifNullOrEmpty(perform: () -> String): String {
        return safeReturn("") {
            if (isNullOrEmpty()) {
                perform.invoke()
            } else {
                this
            }
        }
    }

    fun String.isEmptyOrBlank(): Boolean {
        return isNullOrEmpty() || isNullOrBlank()
    }

    /**
     * Converts a size in bytes (represented as a Double) to a human-readable string format.
     * The function determines the appropriate size unit (KB, MB, GB) based on the value.
     *
     * @return A string representing the size in a human-readable format,
     *         such as "1.23 MB" or "0.45 GB". If an exception occurs,
     *         it returns "0 Bytes".
     */
    fun Double.getSizeAsString(): String {
        return try {
            var sizeType = "KB"
            var sizeValue = (this / 1024f)
            if (sizeValue > 1000f) {
                sizeValue = (sizeValue / 1024f)
                sizeType = "MB"
                if (sizeValue > 1000f) {
                    sizeType = "GB"
                    sizeValue = (sizeValue / 1024f)
                }
            }
            "${sizeValue.formatToTwoDecimalPoints()}$sizeType"
        } catch (e: Exception) {
            "0 Bytes"
        }
    }

    inline fun String.contains(value: String, action: (Boolean) -> String): String {
        return action.invoke((contains(value)))
    }

    fun String.toPart(name: String): MultipartBody.Part {
        // Create a RequestBody instance from the string
        val requestBody = this.toRequestBody("text/plain".toMediaTypeOrNull())
        // Create a MultipartBody.Part instance
        return MultipartBody.Part.createFormData(name, null, requestBody)
    }

    private var lastClicked = 0L
    fun View.setDebounceListener(delay: Long = 500L, onClick: (View) -> Unit) {
        this.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClicked > delay) {
                onClick(it)
                lastClicked = currentTime
            }
        }
    }

    /**
     * Converts an integer to a shortened string representation.
     * The function formats the integer into a more readable form:
     * - If the integer is less than 1000, it returns the integer as a string.
     * - If the integer is less than 1,000,000, it formats the number in kilobytes (k).
     * - If the integer is 1,000,000 or more, it formats the number in megabytes (M).
     *
     * @return A string representing the shortened version of the integer,
     *         such as "500", "1.5k", or "2.3M".
     */
    fun Int.toShortenedString(): String {
        return when {
            this < 1000 -> this.toString()
            this < 1_000_000 -> String.format("%.1fk", this / 1000.0)
            else -> String.format("%.1fM", this / 1_000_000.0)
        }
    }

    fun Long.toShortenedString(): String {
        return when {
            this < 1000 -> this.toString()
            this < 1_000_000 -> String.format("%.1fk", this / 1000.0)
            else -> String.format("%.1fM", this / 1_000_000.0)
        }
    }

    /*fun Activity.handleWindowInsetsListener(
        v: View, windowInsets: WindowInsetsCompat): WindowInsetsCompat {
        systemBarInsetsCompat = windowInsets.getWindowInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(0, 0, 0, systemBarInsetsCompat?.bottom ?: 0)
        return windowInsets
    }*/

    /**Records exception to the error log.
     * @param TAG : Tag for the log, if not provided, uses Default value 'FATAL'
     * @return Nothing
     */
    fun Throwable.recordException(TAG: String = "FATAL") {
        Log.e(TAG, stackTraceToString())
        try {
            //    FirebaseCrashlytics.getInstance().log(stackTraceToString())
        } catch (e: Exception) {

        }
    }
    /**Safely runs a block inside try catch to avoid any crashes.
     * @param block : Block to be executed
     * @return Nothing
     * @author : Zain Sherazi
     */
    inline fun <T> T.safeRun(block: T.() -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            //ignore but log it
            e.recordException()
        }
    }

    fun Any.log() {
        toString().log()
    }

    /**Safely returns a value from a block inside try catch to avoid any crashes.
     * @param block : Block to be executed
     * @param default : Default value to be returned if an exception occurs
     * @return R : Return type of the block
     */
    inline fun <T, R> T.safeReturn(default: R, block: T.() -> R): R {
        return try {
            block()
        } catch (e: Exception) {
            e.recordException()
            default
        }
    }

    inline fun <T, R> T.safeReturn(block: T.() -> R?): R? {
        return try {
            block()
        } catch (e: Exception) {
            e.recordException()
            null
        }
    }

    fun MutableList<Int>.getAverage(): Int {
        var sum = 0
        for (i in this) {
            sum += i
        }
        return sum / if (this.size > 0) size else 1
    }

    /**Show Toast:
     * does what it says it does.
     * @param s : Toast Message to be displayed
     * @return Nothing
     */
    fun Context.showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.showToast(s: String) {
        context?.showToast(s)
    }

    /**Log Extension function:
     * Directly logs the string value on Log.e
     * @param TAG : tag for the log, if none is provided uses a default value
     */
    fun String.log(TAG: String = "PrideApp") {
        Log.e(TAG, this)
    }

    private fun formatDuration(duration: Long): String {
        val minutes = (duration / 1000) / 60
        val seconds = (duration / 1000) % 60
        return String.format(DURATION_FORMAT, minutes, seconds)
    }

    private const val DURATION_FORMAT = "%02d:%02d"

    fun Long.formatDurationSeconds(): String {
        val minutes = (this) / 60
        val seconds = (this) % 60
        return String.format(DURATION_FORMAT, minutes, seconds)
    }

    fun Long.formatDurationMillis(): String {
        if (this < 0) {
            return String.format(DURATION_FORMAT, 0, 0)
        }
        val duration = this / 1000L
        return duration.formatDurationSeconds()
    }

    /**Perform an action*/
    inline fun Fragment.collectOnCreated(crossinline collect: suspend (CoroutineScope) -> Unit) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                collect.invoke(this)
            }
        }
    }

    //HideKeyBoard Extensions
    fun Activity.hideKeyboard() {
        safeRun {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    fun Fragment.hideKeyboard() {
        safeRun { activity?.hideKeyboard() }
    }

    //ShowKeyboard Extensions
    fun Activity.showKeyboard(view: View?) {
        safeRun {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, 0)
        }
    }

    fun Fragment.showKeyboard() {
        safeRun { activity?.showKeyboard(view) }
    }

    /*
    * Converts the Api Created_time format into a better one
    * */
    fun String.toDateTime(): String {
        // Parse the date-time string into an Instant
        val instant = Instant.parse(this)
        // Format the Instant to a date string (yyyy-MM-dd)
        val formatter =
            DateTimeFormatter.ofPattern("MMMM d, yyyy\nh:mm a").withZone(ZoneId.systemDefault())

        return formatter.format(instant)
    }

    fun ImageView.load(value: String?) {
        Glide.with(context).load(value).error(R.drawable.placeholder).placeholder(R.drawable.placeholder)
            .into(this)
    }

    fun String.toHumanTime(): String {
        val instant = Instant.parse(this)
        val now = Instant.now()

        val duration = Duration.between(instant, now)
        return when {
            duration.toDays() > 0 -> "${duration.toDays()} days ago"
            duration.toHours() > 0 -> "${duration.toHours()} hours ago"
            duration.toMinutes() > 0 -> "${duration.toMinutes()} minutes ago"
            else -> "Just now"
        }
    }



    fun String.toDateSimpleTime(): String {
        // Parse the date-time string into an Instant
        val instant = Instant.parse(this)

        // Format the Instant to a date string (yyyy-MM-dd)
        val formatter =
            DateTimeFormatter.ofPattern("MMMM d, yyyy - h:mm a").withZone(ZoneId.systemDefault())

        return formatter.format(instant)
    }

    fun String.toDate(): String {
        // Parse the date-time string into an Instant
        val instant = Instant.parse(this)

        // Format the Instant to a date string (yyyy-MM-dd)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("UTC"))

        return formatter.format(instant)
    }


    fun <T> AppCompatActivity.collectLatestLifecycleFlow(
        flow: Flow<T>, collect: suspend (T) -> Unit,
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                flow.collectLatest(collect)
            }
        }
    }

    /**
     * Handles the window insets for a given view in the Activity.
     * This function retrieves the system bar insets (such as status and navigation bars)
     * and can be used to adjust the padding of the view accordingly.
     *
     * @param view The view for which the insets are being handled.
     * @param insets The WindowInsetsCompat object containing the current insets.
     * @return The modified WindowInsetsCompat object, which can be used for further processing.
     */
    fun Activity.handleInsets(view: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        windowInsets = systemBars
        // Optionally, you can set padding for the view based on the insets
        // view.setPadding(0, 0, 0, 0)
        return insets
    }


    fun View.visible() {
        visibility = VISIBLE
    }

    fun View.visible(value: Boolean) {
        if (value) visible() else gone()
    }

    fun View.gone() {
        visibility = GONE
    }

    fun View.invisible() {
        visibility = INVISIBLE
    }

    fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                flow.collectLatest(collect)
            }
        }
    }

    suspend fun delay(millis: Long) {
        withContext(Dispatchers.IO) {
            kotlinx.coroutines.delay(millis)
        }
    }

    fun EditText.validateOnChange(checkAndValidate: (text: String) -> Unit) {
        addTextChangedListener {
            doOnTextChanged { text, _, _, _ ->
                text?.let {
                    if (it.isNotEmpty()) checkAndValidate.invoke(it.toString())
                }
            }
        }
    }

    fun String.toSafeInt(): Int {
        return safeReturn(0) {
            toInt()
        }
    }

}