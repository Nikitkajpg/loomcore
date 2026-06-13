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

/**
 * ViewModel прейскуранта ремонтных услуг.
 *
 * Не использует [JsonRepository] (тот рассчитан на плоские списки),
 * поэтому сериализация реализована напрямую через [AppJson].
 */
class PriceListViewModel : ViewModel() {

    private val _groups = MutableStateFlow<List<PriceGroup>>(emptyList())

    /** Реактивный список групп прейскуранта. */
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

    /** Сохраняет текущий список групп на диск. Вызывается после каждой мутации. */
    private fun save() {
        Paths.priceListFile.writeText(AppJson.encodeToString(ListSerializer(PriceGroup.serializer()), _groups.value))
    }

    private fun nextGroupId() = (_groups.value.maxOfOrNull { it.id } ?: 0) + 1
    private fun nextRowId(group: PriceGroup) = (group.rows.maxOfOrNull { it.id } ?: 0) + 1

    /** Добавляет новую группу. */
    fun addGroup() {
        _groups.update { it + PriceGroup(id = nextGroupId(), name = "Новая группа") }
        save()
    }

    /** Переименовывает группу с указанным [groupId]. */
    fun updateGroupName(groupId: Int, name: String) {
        _groups.update { list -> list.map { if (it.id == groupId) it.copy(name = name) else it } }
        save()
    }

    /** Удаляет группу вместе со всеми её строками. */
    fun deleteGroup(groupId: Int) {
        _groups.update { it.filter { g -> g.id != groupId } }
        save()
    }

    /** Добавляет новую строку-операцию в группу [groupId]. */
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

    /** Обновляет строку [row] в группе [groupId] (ищет по [PriceRow.id]). */
    fun updateRow(groupId: Int, row: PriceRow) {
        _groups.update { list ->
            list.map { g ->
                if (g.id == groupId) g.copy(rows = g.rows.map { if (it.id == row.id) row else it })
                else g
            }
        }
        save()
    }

    /** Удаляет строку с [rowId] из группы [groupId]. */
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