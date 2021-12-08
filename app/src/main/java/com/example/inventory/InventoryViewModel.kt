package com.example.inventory

import androidx.lifecycle.*
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData

//5)Create Inventory ViewModel
class InventoryViewModel(private val itemDao: ItemDao): ViewModel() {

    //12.1) Use ListAdapter
    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()


    // 5.3) insert the item to the DB
    private fun insertItem(item: Item) {
        viewModelScope.launch{
            itemDao.insert(item)

        }
    }
    //5.2) transfer the input  to item "table" format
    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }
    //5.1) add the item that comes from the user
    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }
    //verify if the text in the TextFields are not empty.
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

//16)Retrieve item details
    fun retrieveItem(id: Int): LiveData<Item>{
        return itemDao.getItem(id).asLiveData()
    }

    //21) Implement the new method, updateItem()

    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    /*22)method sellItem() that takes an instance of the Item entity
    class and returns nothing
     */
    fun sellItem(item: Item) {

 /* 23) Inside the if block, create a new immutable property called newItem.
 Call copy() function on the item instance passing in the updated
 quantityInStock, that is decreasing the stock by 1.
 *** SO, the COPY work is take an column of the selected table "of the chosen elemnt on the table"
 like when we choose from the Items table column of quantityInStock */

        if (item.quantityInStock > 0) {
        val newItem = item.copy(quantityInStock = item.quantityInStock - 1)

          updateItem(newItem)
        }
    }

    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }
//25) Delete item entity
    fun deletItem(item:Item){
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    //26) Update the entity using Room
    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) : Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    fun updateItem(itemId: Int,itemName: String,itemPrice: String,itemCount: String) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }

}


/*6)Create Inventory View Model Factory
the Factory use  to manege the Dao for the ViewModel So, viewModel can handle it
 */
class InventoryViewModelFactory(private val itemDao: ItemDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}


