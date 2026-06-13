package com.njpg.loomcore.ui.screen.tabs.suppliers

import androidx.compose.runtime.Composable
import com.njpg.loomcore.model.Supplier
import com.njpg.loomcore.ui.screen.tabs.PartnerCard

/**
 * Карточка поставщика в списке раздела "Поставщики".
 *
 * Аналог [ClientCard] — делегирует отображение [PartnerCard].
 *
 * @param supplier  Отображаемый поставщик.
 * @param onEdit    Открывает [SupplierDialog] для редактирования.
 * @param onDelete  Открывает [ConfirmDeleteDialog] для удаления.
 */
@Composable
fun SupplierCard(
    supplier: Supplier, onEdit: () -> Unit, onDelete: () -> Unit
) {
    PartnerCard(
        name = supplier.name, lines = listOf(supplier.url, supplier.notes), onEdit = onEdit, onDelete = onDelete
    )
}