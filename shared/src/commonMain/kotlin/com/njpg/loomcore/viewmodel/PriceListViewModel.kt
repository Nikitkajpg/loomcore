package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import com.njpg.loomcore.data.AppJson
import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.PriceGroup
import com.njpg.loomcore.model.PriceRow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.builtins.ListSerializer
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

class PriceListViewModel : ViewModel() {

    private val _groups = MutableStateFlow<List<PriceGroup>>(emptyList())
    val groups = _groups.asStateFlow()

    init {
        val file = Paths.priceListFile
        if (file.exists()) {
            _groups.value = try {
                AppJson.decodeFromString(ListSerializer(PriceGroup.serializer()), file.readText())
            } catch (_: Exception) {
                emptyList()
            }
        }
    }

    private fun save() {
        Paths.priceListFile.writeText(AppJson.encodeToString(ListSerializer(PriceGroup.serializer()), _groups.value))
    }

    private fun nextGroupId() = (_groups.value.maxOfOrNull { it.id } ?: 0) + 1
    private fun nextRowId(group: PriceGroup) = (group.rows.maxOfOrNull { it.id } ?: 0) + 1

    fun addGroup() {
        _groups.update { it + PriceGroup(id = nextGroupId(), name = "Новая группа") }
        save()
    }

    fun updateGroupName(groupId: Int, name: String) {
        _groups.update { list -> list.map { if (it.id == groupId) it.copy(name = name) else it } }
        save()
    }

    fun deleteGroup(groupId: Int) {
        _groups.update { it.filter { g -> g.id != groupId } }
        save()
    }

    fun addRow(groupId: Int) {
        _groups.update { list ->
            list.map { g ->
                if (g.id == groupId) g.copy(
                    rows = g.rows + PriceRow(
                        id = nextRowId(g), service = "Услуга", price = "0.00"
                    )
                )
                else g
            }
        }
        save()
    }

    fun updateRow(groupId: Int, row: PriceRow) {
        _groups.update { list ->
            list.map { g ->
                if (g.id == groupId) g.copy(rows = g.rows.map { if (it.id == row.id) row else it })
                else g
            }
        }
        save()
    }

    fun deleteRow(groupId: Int, rowId: Int) {
        _groups.update { list ->
            list.map { g ->
                if (g.id == groupId) g.copy(rows = g.rows.filter { it.id != rowId })
                else g
            }
        }
        save()
    }
}