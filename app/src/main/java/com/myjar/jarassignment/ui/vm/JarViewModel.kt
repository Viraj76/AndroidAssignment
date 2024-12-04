package com.myjar.jarassignment.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JarViewModel : ViewModel() {

    private val _listStringData = MutableStateFlow(ProductState())
    val listStringData: MutableStateFlow<ProductState>
        get() = _listStringData

    private val repository: JarRepository = JarRepositoryImpl(createRetrofit())

    fun fetchData() {
        viewModelScope.launch {
             repository.fetchResults().collect{computerItemList->
                 Log.d("Itemssss", computerItemList.toString())
                 _listStringData.value = listStringData.value.copy(fetchedProductList = computerItemList)
                 searchProducts()
             }
        }
    }

    fun searchProducts(initialFetchedProducts :  String = ""){
        Log.d("Searching", initialFetchedProducts)

        val searchedProductsList = listStringData.value.fetchedProductList.filter {
            // we can add all other properties too. just name for now.
            it.name.lowercase().contains(initialFetchedProducts.lowercase())
        }

        _listStringData.value = listStringData.value.copy(searchedProductList = searchedProductsList)
    }
}




data class ProductState(
    val searchingText : String = "",
    val fetchedProductList : List<ComputerItem> = emptyList(),
    val searchedProductList : List<ComputerItem> = emptyList(),
)