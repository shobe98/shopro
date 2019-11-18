package ro.andrei.shoppinglist.data

import androidx.room.*
import java.io.Serializable


enum class ProductType { FRUIT, VEGGIE, ELECTRONIC, CLEANING, OTHER }

@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "est_price") var estPrice: Int,
    @ColumnInfo(name = "status") var status: Boolean,
    @ColumnInfo(name = "category") var category: ProductType = ProductType.OTHER,
    @ColumnInfo(name = "priority") var priority: Double = 0.0
) : Serializable
