package ro.andrei.shoppinglist.data

import android.content.Context
import androidx.room.*
import ro.andrei.shoppinglist.R

@Database(entities = arrayOf(Product::class), version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase::class.java, context.getString(R.string.database_name))
                    .fallbackToDestructiveMigration()
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