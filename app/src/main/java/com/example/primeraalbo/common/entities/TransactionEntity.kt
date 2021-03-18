package com.example.primeraalbo.common.entities
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.temporal.TemporalAmount
import java.util.*

@Entity(tableName = "TransactionEntity", indices = [Index(value = ["uuid"], unique = true)] )
data class TransactionEntity(
    @PrimaryKey var uuid:Long,
    var description: String,
    var category: String,
    var operation: String,
    var amount: Float,
    var status: String,
    var creation_date: String
){
}
