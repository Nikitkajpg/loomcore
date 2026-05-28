package com.njpg.loomcore.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.njpg.loomcore.ui.screen.tabs.ClientsTab
import com.njpg.loomcore.ui.screen.tabs.SuppliersTab
import com.njpg.loomcore.ui.screen.tabs.UnitsTab
import com.njpg.loomcore.viewmodel.MainViewModel


private val TAB_ITEMS = listOf(
    TabItem("База", Icons.Default.Store),
    TabItem("Поставщики", Icons.Default.ShoppingCart),
    TabItem("Покупатели", Icons.Default.Person),
)

@Composable
fun MainScreen() {
    val vm: MainViewModel = viewModel { MainViewModel() }
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        Logo()

        Row(modifier = Modifier.fillMaxSize()) {

            NavigationRail(
                modifier = Modifier.fillMaxHeight(),
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                Spacer(Modifier.height(8.dp))
                TAB_ITEMS.forEachIndexed { index, item ->
                    NavigationRailItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        alwaysShowLabel = true
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxHeight().width(1.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTab) {
                    0 -> UnitsTab(vm)
                    1 -> SuppliersTab(vm)
                    2 -> ClientsTab(vm)
                }
            }
        }
    }
}