package com.njpg.loomcore.ui.screen.tabs.suppliers

import androidx.compose.runtime.Composable
import com.njpg.loomcore.model.Supplier
import com.njpg.loomcore.ui.screen.tabs.PartnerCard

@Composable
fun SupplierCard(
    supplier: Supplier, onEdit: () -> Unit, onDelete: () -> Unit
) {
    PartnerCard(
        name = supplier.name, lines = listOf(supplier.url, supplier.notes), onEdit = onEdit, onDelete = onDelete
    )
}