package ro.andrei.shoppinglist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.product_row.view.*
import ro.andrei.shoppinglist.MainActivity
import ro.andrei.shoppinglist.R
import ro.andrei.shoppinglist.data.AppDatabase
import ro.andrei.shoppinglist.data.Product
import ro.andrei.shoppinglist.data.ProductType
import ro.andrei.shoppinglist.touch.ProductTouchHelperCallback
import java.util.*

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>, ProductTouchHelperCallback {
    var productList = mutableListOf<Product>()

    val context: Context

    constructor(context: Context, products: List<Product>) {
        this.context = context
        this.productList.addAll(products)

    }

    override fun onDismissed(position: Int) {

        deleteProduct(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(productList, fromPosition, toPosition)

        if (productList.size != 1) {
            if (toPosition == productList.size - 1) {
                Thread {
                    AppDatabase.getInstance(context).productDao().updatePriority(
                        productList[toPosition].id!!,
                        productList[toPosition - 1].priority + 5000.0
                    )
                }.start()
            }
            else if(toPosition == 0) {
                Thread {
                    AppDatabase.getInstance(context).productDao().updatePriority(
                        productList[toPosition].id!!,
                        productList[1].priority - 5000.0
                    )
                }.start()
            }
            else {
                Thread {
                    AppDatabase.getInstance(context).productDao().updatePriority(
                        productList[toPosition].id!!,
                        (productList[toPosition - 1].priority + productList[toPosition + 1].priority) / 2.0
                    )
                }.start()

            }
        }

        notifyItemMoved(fromPosition, toPosition)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productRow = LayoutInflater.from(context).inflate(
            R.layout.product_row, parent, false
        )
        return ViewHolder(productRow)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var product = productList.get(holder.adapterPosition)

        holder.tvTitle.text = product.name
        holder.tvDescription.text = product.description
        holder.tvPrice.text = product.estPrice.toString()
        holder.cbChecked.isChecked = product.status

        holder.ivIcon.setImageResource(
            when (product.category) {
                ProductType.FRUIT -> R.drawable.watermelon
                ProductType.VEGGIE -> R.drawable.carrot
                ProductType.CLEANING -> R.drawable.cleaning
                ProductType.ELECTRONIC -> R.drawable.electronics
                else -> R.drawable.plus
            }
        )

        holder.btnDelete.setOnClickListener {
            deleteProduct(holder.adapterPosition)
        }

        holder.cbChecked.setOnCheckedChangeListener { _, isChecked ->
            updateChecked(
                holder.adapterPosition,
                isChecked
            )
        }

        holder.btnEdit.setOnClickListener {
            (context as MainActivity).showEditProductDialog(
                product, holder.adapterPosition
            )
        }
    }


    fun deleteProduct(index: Int) {
        Thread {
            AppDatabase.getInstance(context).productDao().deleteProduct(productList[index])
            (context as MainActivity).runOnUiThread {
                productList.removeAt(index)
                notifyItemRemoved(index)

            }

        }.start()

    }

    fun updateChecked(index: Int, isChecked: Boolean) {
        Thread {
            AppDatabase.getInstance(context).productDao()
                .updateChecked(productList[index].id!!, isChecked)
        }.start()
    }

    fun addProduct(product: Product) {
        productList.add(product)
        notifyItemInserted(productList.lastIndex)

    }

    fun deleteAllProducts() {
        Thread {
            AppDatabase.getInstance(context).productDao().deleteAllProducts()

            (context as MainActivity).runOnUiThread {
                productList.clear()
                notifyDataSetChanged()
            }

        }.start()
    }

    fun updateProductOnPosition(product: Product, index: Int) {
        productList.set(index, product)
        notifyItemChanged(index)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.tvTitle
        val tvDescription = itemView.tvDescription
        val tvPrice = itemView.tvPrice
        val ivIcon = itemView.ivRowIcon
        val cbChecked = itemView.cbProduct
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
    }


}