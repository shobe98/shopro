package ro.andrei.shoppinglist.data

import androidx.room.*

@Dao
interface ProductDao {
    @Query("SELECT * FROM product ORDER BY priority")
    fun getAllProduct(): List<Product>

    @Insert
    fun addProduct(product: Product): Long

    @Delete
    fun deleteProduct(product: Product)

    @Update
    fun updateProduct(product: Product)

    @Query("UPDATE product SET status = :status WHERE id = :id")
    fun updateChecked(id: Long, status: Boolean)

    @Query("DELETE FROM product")
    fun deleteAllProducts()

    @Query("UPDATE product SET priority = :priority WHERE id = :id")
    fun updatePriority(id: Long, priority: Double)
}