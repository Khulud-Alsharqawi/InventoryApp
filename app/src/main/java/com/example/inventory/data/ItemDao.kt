package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//2)Create the item DAO
@Dao
interface ItemDao {


//##The argument (OnConflict) tells the Room what to do in case of a conflict insertion
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)
// Flow -> to update data on the DB
    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * from item ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>

}