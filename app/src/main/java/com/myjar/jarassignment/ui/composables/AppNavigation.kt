package com.myjar.jarassignment.ui.composables

import android.util.Log
import android.util.Log.i
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.ui.vm.JarViewModel
import com.myjar.jarassignment.utils.orEmpty
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: JarViewModel,
) {
    val navController = rememberNavController()
    val navigate = remember { mutableStateOf<String>("") }

    NavHost(modifier = modifier, navController = navController, startDestination = "item_list") {
        composable("item_list") {
            ItemListScreen(
                viewModel = viewModel,
                onNavigateToDetail = { selectedItem -> navigate.value = selectedItem },
                navigate = navigate,
                navController = navController
            )
        }
        composable("item_detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            ItemDetailScreen(itemId = itemId){
                navigate.value = ""  // remove the stored item's id
                    navController.navigate("item_list"){
                        popUpTo("item_list"){
                            inclusive = true
                        }
                    }
            }
        }
    }
}

@Composable
fun ItemListScreen(
    viewModel: JarViewModel,
    onNavigateToDetail: (String) -> Unit,
    navigate: MutableState<String>,
    navController: NavHostController
) {
    val items = viewModel.listStringData.collectAsStateWithLifecycle()


    if (navigate.value.isNotBlank()) {
        val currRoute = navController.currentDestination?.route.orEmpty()
        if (!currRoute.contains("item_detail")) {
            navController.navigate("item_detail/${navigate.value}")
        }
    }
    var initialSearchedText by remember {  mutableStateOf("")  }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            value = initialSearchedText,
            onValueChange = {
                initialSearchedText = it
                viewModel.searchProducts(initialSearchedText)
            },
            placeholder = {
                Text("Search Products")
            }
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            items(items.value.searchedProductList, key = {it.id}) { item ->
                Log.d("OneItems", item.toString())
                ItemCard(
                    item = item,
                    onClick = { onNavigateToDetail(item.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ItemCard(item: ComputerItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {



        Text(text = item.name, fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 22.sp)
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = item.data?.price.orEmpty().toString(), fontWeight = FontWeight.Normal, color = Color.Black, fontSize = 16.sp)
                Text(text =  item.data?.capacityGB.orEmpty().toString(), fontWeight = FontWeight.Normal, color = Color.Black, fontSize = 16.sp)
                Text(text =  item.data?.screenSize.orEmpty().toString(), fontWeight = FontWeight.Normal, color = Color.Black, fontSize = 16.sp)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text =  item.data?.generation.orEmpty(), fontWeight = FontWeight.Normal, color = Color.Black, fontSize = 16.sp)
                Text(text =  item.data?.strapColour.orEmpty(), fontWeight = FontWeight.Normal, color = Color.Black, fontSize = 16.sp)
                Text(text =  item.data?.caseSize.orEmpty(), fontWeight = FontWeight.Normal, color = Color.Black, fontSize = 16.sp)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text =  item.data?.cpuModel.orEmpty(), fontWeight = FontWeight.Normal, color = Color.Black, fontSize = 16.sp)
                Text(text =  item.data?.hardDiskSize.orEmpty(), fontWeight = FontWeight.Normal, color = Color.Black, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ItemDetailScreen(
    itemId: String?,
    goToItemListScreen : () -> Unit
) {
    // Fetch the item details based on the itemId
    // Here, you can fetch it from the ViewModel or repository

    BackHandler {
        goToItemListScreen()
    }
    Text(
        text = "Item Details for ID: $itemId",
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}
