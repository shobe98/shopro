package ro.andrei.shoppinglist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.activity_main.*
import ro.andrei.shoppinglist.adapter.ProductAdapter
import ro.andrei.shoppinglist.data.AppDatabase
import ro.andrei.shoppinglist.data.Product
import ro.andrei.shoppinglist.data.ProductType
import ro.andrei.shoppinglist.touch.ProductRecyclerTouchCallback

class MainActivity : AppCompatActivity(), ProductDialog.ProductHandler {

    companion object {
        const val KEY_PRODUCT = "KEY_PRODUCT"
        const val TAG_ADD_PROD_DIALOG = "TAG_ADD_PROD_DIALOG"
        const val TAG_EDIT_PROD_DIALOG = "TAG_EDIT_PROD_DIALOG"
    }


    override fun productCreated(item: Product) {
        saveItem(item)
    }

    override fun productUpdated(item: Product) {
        Thread {
            AppDatabase.getInstance(this@MainActivity).productDao().updateProduct(item)

            runOnUiThread {
                productAdapter.updateProductOnPosition(item, editIndex)
            }
        }.start()
    }


    private fun saveItem(product: Product) {
        Thread {
            product.id = AppDatabase.getInstance(this@MainActivity).productDao().addProduct(product)
            AppDatabase.getInstance(this@MainActivity).productDao().updatePriority(
                product.id!!, (product.id!!.toDouble() * 5000.0))

            runOnUiThread {
                productAdapter.addProduct(product)
            }
        }.start()
    }

    lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initRecyclerView()

        fab.setOnClickListener {
            showAddProductDialog()
        }

        fabDeleteAll.setOnClickListener {
            productAdapter.deleteAllProducts()
        }


    }

    private fun showAddProductDialog() {
        ProductDialog().show(supportFragmentManager, TAG_ADD_PROD_DIALOG)
    }


    private fun initRecyclerView() {
        Thread {
            var PRODUCTs: List<Product> =
                AppDatabase.getInstance(this@MainActivity).productDao().getAllProduct()
            runOnUiThread {

                productAdapter = ProductAdapter(this, PRODUCTs)
                recyclerShoppingList.adapter = productAdapter


                var itemDecorator = DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )

                recyclerShoppingList.addItemDecoration(itemDecorator)



                val callback = ProductRecyclerTouchCallback(productAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerShoppingList)

            }
        }.start()
    }

    private var editIndex: Int = -1

    fun showEditProductDialog(product: Product, index: Int) {
        editIndex = index

        val editDialog = ProductDialog()
        val bundle = Bundle()
        bundle.putSerializable(KEY_PRODUCT, product)

        editDialog.arguments = bundle

        editDialog.show(supportFragmentManager, TAG_EDIT_PROD_DIALOG)
    }
}
