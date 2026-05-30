package com.njpg.loomcore.ui.screen.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.njpg.loomcore.model.Product
import com.njpg.loomcore.ui.components.ProductDialog
import com.njpg.loomcore.viewmodel.ProductsViewModel

@Composable
fun ProductsTab(vm: ProductsViewModel) {
    val products by vm.products.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Product?>(null) }

    if (showDialog) {
        ProductDialog(initial = editTarget, nextId = vm.nextId(), onConfirm = { unit ->
            if (editTarget == null) vm.add(unit) else vm.update(unit)
            showDialog = false
            editTarget = null
        }, onDismiss = { showDialog = false; editTarget = null })
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { editTarget = null; showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить изделие")
            }
        }) { padding ->
        if (products.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Нет изделий. Нажмите + чтобы добавить.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(products, key = { it.id }) { unit ->
                    ProductCard(
                        product = unit,
                        onEdit = { editTarget = it; showDialog = true },
                        onDelete = { vm.delete(it.id) },
                        onPickPhoto = { vm.importPhoto(unit.id, it.toPath()) })
                }
            }
        }
    }
}

