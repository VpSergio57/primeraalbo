package com.example.primeraalbo.common.database
import androidx.room.*
import com.example.primeraalbo.common.dataModel.CategoryPercentList
import com.example.primeraalbo.common.entities.TransactionEntity

@Dao
interface TransactionDao {

    @Query("UPDATE TransactionEntity SET creation_date = substr(creation_date, 7, 4) || '-' || substr(creation_date, 1,2) || '-' || substr(creation_date, 4,2)")
    fun UpdateDatesFormart()

    @Query("SELECT * FROM TransactionEntity")
    fun getAll(): MutableList<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(objects: MutableList<TransactionEntity>)

    @Query("SELECT COUNT(*) AS NUMBER FROM TransactionEntity WHERE status LIKE 'pending'  AND creation_date LIKE :montYear")
    fun getTotalPendingTrans(montYear: String) : String

    @Query("SELECT COUNT(*) AS NUMBER FROM TransactionEntity WHERE status LIKE 'rejected'  AND creation_date LIKE :montYear")
    fun getTotalRejectedTrans(montYear: String): String

    @Query("SELECT IFNULL(SUM(amount), 0) AS totalAmount FROM TransactionEntity WHERE status LIKE 'done' AND  operation LIKE 'in' AND creation_date LIKE :montYear")
    fun getTotalIngresos(montYear: String): String

    @Query("SELECT IFNULL(SUM(amount), 0) AS totalAmount FROM TransactionEntity WHERE status LIKE 'done' AND " +
            " operation LIKE 'out' AND creation_date LIKE :montYear")
    fun getTotalGastos(montYear: String): String

    @Query("SELECT t1.category, \n" +
            "ROUND((100*(SUM(t1.amount))/ (\n" +
            "SELECT SUM(t2.amount)  FROM TransactionEntity t2\n" +
            "WHERE operation LIKE 'out' and t2.creation_date LIKE :montYear\n" +
            ") ),2) as porcentaje  FROM TransactionEntity t1\n" +
            "WHERE operation LIKE 'out' and t1.creation_date LIKE :montYear\n" +
            "GROUP BY t1.category\n" +
            "ORDER BY porcentaje DESC;")
    fun getGastosCategoryPercentList(montYear: String): MutableList<CategoryPercentList>


}


