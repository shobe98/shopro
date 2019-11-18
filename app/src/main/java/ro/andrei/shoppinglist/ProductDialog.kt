package ro.andrei.shoppinglist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.new_product_dialog.view.*
import ro.andrei.shoppinglist.data.Product
import ro.andrei.shoppinglist.data.ProductType

class ProductDialog : DialogFragment() {

    interface ProductHandler {
        fun productCreated(item: Product)
        fun productUpdated(item: Product)
    }


    private var isEditMode = false

    private lateinit var productHandler: ProductHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ProductHandler) {
            productHandler = context
        } else {
            throw RuntimeException(
                "The activity does not implement the ProductHandlerInterface"
            )
        }
    }

    private lateinit var etProductTitle: EditText
    private lateinit var etProductDescription: EditText
    private lateinit var etProductPrice: EditText
    private lateinit var spnProductCategory: Spinner

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        if(arguments != null && arguments!!.containsKey(MainActivity.KEY_PRODUCT)) {
            isEditMode = true
        }

        if(isEditMode) {
            builder.setTitle("Edit product")
        }
        else {
            builder.setTitle("New product")
        }

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_product_dialog, null
        )

        etProductTitle = rootView.etTitle
        etProductDescription = rootView.etDescription
        etProductPrice = rootView.etPrice
        spnProductCategory = rootView.spnCategory
        builder.setView(rootView)

        if(isEditMode) {
            rootView.tvInnerTitle.text = "Edit"

            var product = (arguments?.getSerializable(MainActivity.KEY_PRODUCT) as Product)

            etProductTitle.setText(product.name)
            etProductPrice.setText(product.estPrice.toString())
            etProductDescription.setText(product.description)
            spnProductCategory.setSelection(product.category.ordinal)
        }

        builder.setPositiveButton("OK") { dialog, witch ->
            // empty
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if(etProductDescription.text.isEmpty()) {
                etProductDescription.setText("")
            }
            if(etProductPrice.text.isEmpty()) {
                etProductPrice.setText("0")
            }


            if (etProductTitle.text.isNotEmpty()) {
                if(isEditMode) {
                    handleProductEdit()
                }
                else {
                    handleProductCreate()
                }

                (dialog as AlertDialog).dismiss()
            } else {
                etProductTitle.error = "This field can not be empty"
            }
        }
    }

    private fun handleProductEdit() {
        var productToEdit = (arguments?.getSerializable(MainActivity.KEY_PRODUCT) as Product)

        productToEdit.description = etProductDescription.text.toString()
        productToEdit.name = etProductTitle.text.toString()
        productToEdit.category = when (spnProductCategory.selectedItemPosition) {
            0 -> ProductType.FRUIT
            1 -> ProductType.VEGGIE
            2 -> ProductType.ELECTRONIC
            3 -> ProductType.CLEANING
            else -> ProductType.OTHER
        }
        productToEdit.estPrice = etProductPrice.text.toString().toInt()

        productHandler.productUpdated(productToEdit)
    }

    private fun handleProductCreate() {
        productHandler.productCreated(
            Product(
                null,
                etProductTitle.text.toString(),
                etProductDescription.text.toString(),
                etProductPrice.text.toString().toInt(),
                false,

                when (spnProductCategory.selectedItemPosition) {
                    0 -> ProductType.FRUIT
                    1 -> ProductType.VEGGIE
                    2 -> ProductType.ELECTRONIC
                    3 -> ProductType.CLEANING
                    else -> ProductType.OTHER
                }
            )
        )
    }
}