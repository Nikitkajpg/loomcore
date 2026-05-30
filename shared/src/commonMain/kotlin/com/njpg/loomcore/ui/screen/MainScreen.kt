package com.njpg.loomcore.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.njpg.loomcore.ui.screen.tabs.ClientsTab
import com.njpg.loomcore.ui.screen.tabs.ProductsTab
import com.njpg.loomcore.ui.screen.tabs.SuppliersTab
import com.njpg.loomcore.viewmodel.ClientsViewModel
import com.njpg.loomcore.viewmodel.ProductsViewModel
import com.njpg.loomcore.viewmodel.SuppliersViewModel

private val TAB_ITEMS = listOf(
    TabItem("База", Icons.Default.Store),
    TabItem("Поставщики", Icons.Default.ShoppingCart),
    TabItem("Покупатели", Icons.Default.Person),
)

@Composable
fun MainScreen() {
    val unitsVm: ProductsViewModel = viewModel { ProductsViewModel() }
    val suppliersVm: SuppliersViewModel = viewModel { SuppliersViewModel() }
    val clientsVm: ClientsViewModel = viewModel { ClientsViewModel() }

    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        Surface(
            color = MaterialTheme.colorScheme.surface, tonalElevation = 4.dp, modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LoomCore",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

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

            HorizontalDivider(modifier = Modifier.fillMaxHeight().width(1.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTab) {
                    0 -> ProductsTab(unitsVm)
                    1 -> SuppliersTab(suppliersVm)
                    2 -> ClientsTab(clientsVm)
                }
            }
        }
    }
}