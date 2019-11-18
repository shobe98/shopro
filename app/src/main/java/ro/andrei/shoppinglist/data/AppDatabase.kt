package ro.andrei.shoppinglist.data

import android.content.Context
import androidx.room.*

@Database(entities = arrayOf(Product::class), version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase::class.java, "todo.db")
                    .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}


class Converters {
    @TypeConverter
    fun productTypeToInt(value: ProductType) = value.ordinal

    @TypeConverter
    fun intToProductType(idx: Int) = ProductType.values()[idx]

}