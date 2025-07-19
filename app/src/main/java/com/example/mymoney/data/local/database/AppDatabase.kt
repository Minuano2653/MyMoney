package com.example.mymoney.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.mymoney.data.local.datasource.AccountDao
import com.example.mymoney.data.local.datasource.CategoryDao
import com.example.mymoney.data.local.datasource.TransactionDao
import com.example.mymoney.data.local.entity.LocalAccount
import com.example.mymoney.data.local.entity.LocalCategory
import com.example.mymoney.data.local.entity.LocalTransaction
import java.math.BigDecimal

@Database(entities = [LocalCategory::class, LocalTransaction::class, LocalAccount::class], version = 1, exportSchema = false)
@TypeConverters(BigDecimalConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
}

class BigDecimalConverter {

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toPlainString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }
}