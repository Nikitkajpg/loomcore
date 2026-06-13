package com.njpg.loomcore.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.njpg.loomcore.ui.screen.tabs.TabItem
import com.njpg.loomcore.ui.screen.tabs.clients.ClientsTab
import com.njpg.loomcore.ui.screen.tabs.materials.MaterialsTab
import com.njpg.loomcore.ui.screen.tabs.orders.OrdersTab
import com.njpg.loomcore.ui.screen.tabs.products.ProductsTab
import com.njpg.loomcore.ui.screen.tabs.profile.ProfileTab
import com.njpg.loomcore.ui.screen.tabs.suppliers.SuppliersTab
import com.njpg.loomcore.viewmodel.*

/**
 * Список вкладок боковой навигационной панели.
 * Порядок элементов соответствует индексам в [MainScreen].
 */
private val TAB_ITEMS = listOf(
    TabItem("Заказы", Icons.Default.Star),
    TabItem("Изделия", Icons.Default.Store),
    TabItem("Материалы", Icons.Default.Layers),
    TabItem("Поставщики", Icons.Default.ShoppingCart),
    TabItem("Покупатели", Icons.Default.Person),
    TabItem("Профиль", Icons.Default.Settings),
)

/**
 * Главный экран приложения с боковой навигацией и областью контента.
 *
 * ## Архитектура
 * Все ViewModel создаются здесь один раз и передаются вниз по дереву
 * Composable. Это гарантирует, что одни и те же данные (например, список
 * материалов) видны как в разделе "Материалы", так и в "Заказах".
 *
 * ## Навигация
 * Реализована через [NavigationRail] (вертикальная панель слева).
 * Состояние [selectedTab] — индекс активной вкладки.
 */
@Composable
fun MainScreen() {
    val productsVm: ProductsViewModel = viewModel { ProductsViewModel() }
    val materialsVm: MaterialsViewModel = viewModel { MaterialsViewModel() }
    val suppliersVm: SuppliersViewModel = viewModel { SuppliersViewModel() }
    val clientsVm: ClientsViewModel = viewModel { ClientsViewModel() }
    val profileVm: ProfileViewModel = viewModel { ProfileViewModel() }
    val ordersVm: OrdersViewModel = viewModel { OrdersViewModel() }

    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        AppBar()
        Row(modifier = Modifier.fillMaxSize()) {
            NavigationRail(
                modifier = Modifier.fillMaxHeight(), containerColor = MaterialTheme.colorScheme.surface
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
                    0 -> OrdersTab(ordersVm, clientsVm, materialsVm, productsVm, profileVm)
                    1 -> ProductsTab(productsVm, materialsVm)
                    2 -> MaterialsTab(materialsVm, suppliersVm, productsVm)
                    3 -> SuppliersTab(suppliersVm, materialsVm)
                    4 -> ClientsTab(clientsVm)
                    5 -> ProfileTab(profileVm)
                }
            }
        }
    }
}