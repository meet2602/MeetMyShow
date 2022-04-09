package com.materialsouk.meetmyshow

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.materialsouk.meetmyshow.databinding.ActivitySeatingBinding
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class SeatingActivity : AppCompatActivity(), PaymentResultListener {
    lateinit var row_view_1: View
    lateinit var row_view_3: View
    lateinit var row_view_2: View
    lateinit var row_view_4: View
    lateinit var row_view_5: View
    var textIdList = arrayOf(
        R.id.column_text_1,
        R.id.column_text_2,
        R.id.column_text_3,
        R.id.column_text_4,
        R.id.column_text_5,
        R.id.column_text_6,
        R.id.column_text_7,
        R.id.column_text_8,
    )
    var textViewCount = 8
    private var col_text_view_array_1: Array<TextView?>? = null
    private var col_text_view_array_2: Array<TextView?>? = null
    private var col_text_view_array_3: Array<TextView?>? = null
    private var col_text_view_array_4: Array<TextView?>? = null
    private var col_text_view_array_5: Array<TextView?>? = null
    private lateinit var binding: ActivitySeatingBinding
    var quantityCount = 0
    var price = 100
    var rbString = "10:00 am to 12:00 am"
    private lateinit var dateString: String
    private lateinit var loadingDialog: Dialog

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_seating)
        val moviename = intent.getStringExtra("movie_name")
        val cinema_name = intent.getStringExtra("cinema_name")
        price = intent.getIntExtra("price", 100)
        binding.txtPrice.text = "Rs.$price"
        row_view_1 = findViewById(R.id.row_layout_A)
        row_view_2 = findViewById(R.id.row_layout_B)
        row_view_3 = findViewById(R.id.row_layout_C)
        row_view_4 = findViewById(R.id.row_layout_D)
        row_view_4 = findViewById(R.id.row_layout_D)
        row_view_5 = findViewById(R.id.row_layout_E)

        col_text_view_array_1 = arrayOfNulls(textViewCount)
        col_text_view_array_2 = arrayOfNulls(textViewCount)
        col_text_view_array_3 = arrayOfNulls(textViewCount)
        col_text_view_array_4 = arrayOfNulls(textViewCount)
        col_text_view_array_5 = arrayOfNulls(textViewCount)
        for (i in 0 until textViewCount) {
            col_text_view_array_1!![i] = row_view_1.findViewById<View>(textIdList[i]) as TextView
            col_text_view_array_1!![i]!!.setOnClickListener { v ->
                col_text_view_array_1!![i]!!.isSelected = !col_text_view_array_1!![i]!!.isSelected
                quantityCount = if (col_text_view_array_1!![i]!!.isSelected) {
                    quantityCount + 1
                } else {
                    if (quantityCount == 0) {
                        0
                    } else {
                        quantityCount - 1
                    }
                }
                setBackgroundTextView(col_text_view_array_1, i)
                quantityCheck()
            }
            col_text_view_array_2!![i] = row_view_2.findViewById<View>(textIdList[i]) as TextView
            col_text_view_array_2!![i]!!.setOnClickListener { v ->
                col_text_view_array_2!![i]!!.isSelected =
                    !col_text_view_array_2!![i]!!.isSelected
                quantityCount = if (col_text_view_array_2!![i]!!.isSelected) {
                    quantityCount + 1
                } else {
                    if (quantityCount == 0) {
                        0
                    } else {
                        quantityCount - 1
                    }
                }
                setBackgroundTextView(col_text_view_array_2, i)
                quantityCheck()
            }

            col_text_view_array_3!![i] = row_view_3.findViewById<View>(textIdList[i]) as TextView
            col_text_view_array_3!![i]!!.setOnClickListener { v ->
                col_text_view_array_3!![i]!!.isSelected =
                    !col_text_view_array_3!![i]!!.isSelected
                quantityCount = if (col_text_view_array_3!![i]!!.isSelected) {
                    quantityCount + 1
                } else {
                    if (quantityCount == 0) {
                        0
                    } else {
                        quantityCount - 1
                    }
                }
                setBackgroundTextView(col_text_view_array_3, i)
                quantityCheck()
            }

            col_text_view_array_4!![i] = row_view_4.findViewById<View>(textIdList[i]) as TextView
            col_text_view_array_4!![i]!!.setOnClickListener { v ->
                col_text_view_array_4!![i]!!.isSelected =
                    !col_text_view_array_4!![i]!!.isSelected
                quantityCount = if (col_text_view_array_4!![i]!!.isSelected) {
                    quantityCount + 1
                } else {
                    if (quantityCount == 0) {
                        0
                    } else {
                        quantityCount - 1
                    }
                }
                setBackgroundTextView(col_text_view_array_4, i)
                quantityCheck()
            }

            col_text_view_array_5!![i] = row_view_5.findViewById<View>(textIdList[i]) as TextView
            col_text_view_array_5!![i]!!.setOnClickListener { v ->
                col_text_view_array_5!![i]!!.isSelected =
                    !col_text_view_array_5!![i]!!.isSelected
                quantityCount = if (col_text_view_array_5!![i]!!.isSelected) {
                    quantityCount + 1
                } else {
                    if (quantityCount == 0) {
                        0
                    } else {
                        quantityCount - 1
                    }
                }
                setBackgroundTextView(col_text_view_array_5, i)
                quantityCheck()
            }

        }

        val pickDateBtn = findViewById<Button>(R.id.pickDateBtn)


        val calendar = Calendar.getInstance()
        loadingDialog = Dialog(this)
        loadingDialog.setContentView(R.layout.loading_layout)
        loadingDialog.window!!.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        loadingDialog.setCancelable(false)
        val day = calendar.get(Calendar.DAY_OF_MONTH);
        val year = calendar.get(Calendar.YEAR);
        val month = calendar.get(Calendar.MONTH);
        dateString = day.toString() + "/" + (month + 1) + "/" + year
        pickDateBtn.text = dateString
        var datePicker: DatePickerDialog
        pickDateBtn.setOnClickListener {
            datePicker = DatePickerDialog(
                this,
                { view, year, month, dayOfMonth -> // adding the selected date in the edittext
                    pickDateBtn.setText(dayOfMonth.toString() + "/" + (month + 1) + "/" + year)
                    dateString = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                    for (i in 0 until textViewCount) {
                        col_text_view_array_1!![i]!!.isEnabled = true
                        col_text_view_array_2!![i]!!.isEnabled = true
                        col_text_view_array_3!![i]!!.isEnabled = true
                        col_text_view_array_4!![i]!!.isEnabled = true
                        col_text_view_array_5!![i]!!.isEnabled = true

                        col_text_view_array_1!![i]!!.background =
                            ResourcesCompat.getDrawable(resources, R.drawable.button_outline, null)
                        col_text_view_array_2!![i]!!.background =
                            ResourcesCompat.getDrawable(resources, R.drawable.button_outline, null)
                        col_text_view_array_3!![i]!!.background =
                            ResourcesCompat.getDrawable(resources, R.drawable.button_outline, null)
                        col_text_view_array_4!![i]!!.background =
                            ResourcesCompat.getDrawable(resources, R.drawable.button_outline, null)
                        col_text_view_array_5!![i]!!.background =
                            ResourcesCompat.getDrawable(resources, R.drawable.button_outline, null)
                    }
                    quantityCount = 0
                    binding.txtQuality.text = "0"
                    binding.txtTotalPrice.text = "Rs.0"
                    check(moviename!!, loadingDialog, dateString, rbString, cinema_name!!)
                }, year, month, day
            )

            // set maximum date to be selected as today
            datePicker.datePicker.minDate = calendar.timeInMillis
            val string_date = "15-April-2022"

            val f = SimpleDateFormat("dd-MMM-yyyy")
            try {
                val d: Date = f.parse(string_date)
                val milliseconds = d.time
                datePicker.datePicker.maxDate = milliseconds
                datePicker.show()
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        binding.buyBtn.setOnClickListener {
            if (pickDateBtn.text.toString() != "Pick date") {
                if (quantityCount > 0) {

                    val checkout = Checkout()
                    checkout.setKeyID(this.resources.getString(R.string.api_key))
                    val options = JSONObject()

                    try {
                        val totalPrice = ((price * quantityCount) * 100)
                        options.put("name", "MeetMyShow")
                        options.put("description", "Test Payment")
                        options.put("theme.color", "#0093DD")
                        options.put("currency", "INR")
                        options.put("Amount", totalPrice)
                        options.put("prefill.contact", "+911234567890")
                        options.put("prefill.email", "random@gmail.com")
                        checkout.open(this, options)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(this, "Please Select atleast 1 seat", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please Select Date", Toast.LENGTH_LONG).show()
            }
        }
        radioGroup.setOnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
            val rb = findViewById<View>(checkedId) as RadioButton
            for (i in 0 until textViewCount) {
                col_text_view_array_1!![i]!!.isEnabled = true
                col_text_view_array_2!![i]!!.isEnabled = true
                col_text_view_array_3!![i]!!.isEnabled = true
                col_text_view_array_4!![i]!!.isEnabled = true
                col_text_view_array_5!![i]!!.isEnabled = true

                col_text_view_array_1!![i]!!.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.button_outline, null)
                col_text_view_array_2!![i]!!.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.button_outline, null)
                col_text_view_array_3!![i]!!.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.button_outline, null)
                col_text_view_array_4!![i]!!.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.button_outline, null)
                col_text_view_array_5!![i]!!.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.button_outline, null)

            }
            rbString = rb.text.toString()
            check(moviename!!, loadingDialog, dateString, rb.text.toString(), cinema_name!!)

        }
        check(moviename!!, loadingDialog, dateString, "10:00 am to 12:00 am", cinema_name!!)

    }

    private fun quantityCheck() {
        binding.txtQuality.text = quantityCount.toString()
        if (quantityCount > 0) {
            binding.txtTotalPrice.text = "Rs." + (price * quantityCount).toString()
        } else {
            Toast.makeText(this, "Please Select atleast 1 seat", Toast.LENGTH_LONG).show()
        }
    }

    private fun check(
        moviename: String,
        loadingDialog: Dialog,
        dateString: String,
        radioStr: String,
        cinema_name: String
    ) {
        loadingDialog.show()
        FirebaseFirestore.getInstance().collection("Tickets")
            .document(moviename)
            .collection("Users")
            .get().addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    val list = queryDocumentSnapshots.documents
                    for (documentList in list) {
                        if (dateString == documentList.get("date").toString() &&
                            radioStr == documentList.get("time").toString() &&
                            cinema_name == documentList.get("cinema_name").toString()
                        ) {
                            val resultSeating = documentList.get("seating_no").toString()
                            val lstValues: List<String> = resultSeating.split(",").map { it.trim() }
                            Log.d("lst", lstValues.toString())
                            lstValues.forEach {
                                when {
                                    "A" == it[0].toString() -> {
                                        val result: Int = it[1].toString().toInt()
                                        col_text_view_array_1!![result]!!.isEnabled = false
                                        col_text_view_array_1!![result]!!.background =
                                            ResourcesCompat.getDrawable(
                                                resources,
                                                R.drawable.button_default_set_dialog_outline,
                                                null
                                            )
                                    }
                                    "B" == it[0].toString() -> {
                                        val result: Int = it[1].toString().toInt()
                                        col_text_view_array_2!![result]!!.isEnabled = false
                                        col_text_view_array_2!![result]!!.background =
                                            ResourcesCompat.getDrawable(
                                                resources,
                                                R.drawable.button_default_set_dialog_outline,
                                                null
                                            )
                                    }
                                    "C" == it[0].toString() -> {
                                        val result: Int = it[1].toString().toInt()
                                        col_text_view_array_3!![result]!!.isEnabled = false
                                        col_text_view_array_3!![result]!!.background =
                                            ResourcesCompat.getDrawable(
                                                resources,
                                                R.drawable.button_default_set_dialog_outline,
                                                null
                                            )
                                    }
                                    "D" == it[0].toString() -> {
                                        val result: Int = it[1].toString().toInt()
                                        col_text_view_array_4!![result]!!.isEnabled = false
                                        col_text_view_array_4!![result]!!.background =
                                            ResourcesCompat.getDrawable(
                                                resources,
                                                R.drawable.button_default_set_dialog_outline,
                                                null
                                            )
                                    }
                                    "E" == it[0].toString() -> {
                                        val result: Int = it[1].toString().toInt()
                                        col_text_view_array_5!![result]!!.isEnabled = false
                                        col_text_view_array_5!![result]!!.background =
                                            ResourcesCompat.getDrawable(
                                                resources,
                                                R.drawable.button_default_set_dialog_outline,
                                                null
                                            )
                                    }
                                }
                            }
                            quantityCount = 0
                            binding.txtQuality.text = "0"
                            binding.txtTotalPrice.text = "Rs.0"
                        }
                    }
                    loadingDialog.dismiss()
                } else {
                    loadingDialog.dismiss()
                    Toast.makeText(this, "No Data Found", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun textResult(
        rowName: String,
        col_text_view_array: Array<TextView?>
    ): java.lang.StringBuilder {
        val row_result = StringBuilder()
        for (i in 0 until textViewCount) {
            if (col_text_view_array[i]!!.isSelected) {
                row_result.append("$rowName$i,")
            }
        }
        return row_result
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show()
        val selectedId = binding.radioGroup.checkedRadioButtonId

        val radioButton = findViewById<View>(selectedId) as RadioButton

        val SeatingStr = StringBuilder()
        SeatingStr.append(textResult("A", col_text_view_array_1!!))
        SeatingStr.append(textResult("B", col_text_view_array_2!!))
        SeatingStr.append(textResult("C", col_text_view_array_3!!))
        SeatingStr.append(textResult("D", col_text_view_array_4!!))
        SeatingStr.append(textResult("E", col_text_view_array_5!!))

        SeatingStr.deleteCharAt(SeatingStr.length - 1)
        Toast.makeText(
            this,
            SeatingStr.toString(), Toast.LENGTH_LONG
        ).show()
        loadingDialog.show()
        val moviename = intent.getStringExtra("movie_name")
        val cinema_name = intent.getStringExtra("cinema_name")
        val registerHM: HashMap<String, Any> = HashMap()
        val id = UUID.randomUUID().toString()
        registerHM["movie_name"] = moviename!!
        registerHM["movie_id"] = intent.getStringExtra("movieId")!!
        registerHM["banner_image_url"] = intent.getStringExtra("banner_image_url")!!
        registerHM["user_id"] = FirebaseAuth.getInstance().currentUser!!.uid
        registerHM["id"] = id
        registerHM["seating_no"] = SeatingStr.toString()
        registerHM["date"] = binding.pickDateBtn.text
        registerHM["time"] = radioButton.text
        val totalPrice = price * quantityCount
        registerHM["total_price"] = totalPrice
        registerHM["price"] = price
        registerHM["quantity"] = quantityCount
        registerHM["cinema_name"] = cinema_name!!
        registerHM["cinema_location"] = intent.getStringExtra("cinema_location")!!
        FirebaseFirestore.getInstance().collection("Tickets").document(moviename)
            .collection("Users").document(id).set(registerHM)
            .addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    FirebaseFirestore.getInstance().collection("User_Tickets")
                        .document(FirebaseAuth.getInstance().currentUser!!.uid)
                        .collection("Users").document(id).set(registerHM)
                        .addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Ticket Booked Successfully",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                loadingDialog.dismiss()
                                val intent = Intent(this, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            } else {
                                loadingDialog.dismiss()
                                Toast.makeText(
                                    this,
                                    Objects.requireNonNull(task1.exception).toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                } else {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        this,
                        Objects.requireNonNull(task1.exception).toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Not Successful", Toast.LENGTH_SHORT).show()
    }

    private fun setBackgroundTextView(textViewArray: Array<TextView?>?, i: Int) {
        if (textViewArray!![i]!!.isSelected) {
            textViewArray[i]!!.background = ResourcesCompat.getDrawable(
                resources,
                R.drawable.button_light_set_dialog_outline,
                null
            )

        } else {
            textViewArray[i]!!.background =
                ResourcesCompat.getDrawable(resources, R.drawable.button_outline, null)

        }
    }

}