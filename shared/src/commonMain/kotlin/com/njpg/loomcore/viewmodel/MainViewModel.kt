package com.njpg.loomcore.viewmodel

import androidx.lifecycle.ViewModel
import com.njpg.loomcore.data.ImageStorage
import com.njpg.loomcore.data.JsonRepository
import com.njpg.loomcore.data.Paths
import com.njpg.loomcore.model.Client
import com.njpg.loomcore.model.ProductUnit
import com.njpg.loomcore.model.Supplier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.nio.file.Path

class MainViewModel : ViewModel() {

    private val unitsRepo = JsonRepository(Paths.unitsFile, ProductUnit.serializer())
    private val suppliersRepo = JsonRepository(Paths.suppliersFile, Supplier.serializer())
    private val clientsRepo = JsonRepository(Paths.clientsFile, Client.serializer())

    private val _units = MutableStateFlow<List<ProductUnit>>(emptyList())
    val units = _units.asStateFlow()

    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())
    val suppliers = _suppliers.asStateFlow()

    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients = _clients.asStateFlow()

    init {
        loadAll()
    }

    fun loadAll() {
        _units.value = unitsRepo.loadAll()
        _suppliers.value = suppliersRepo.loadAll()
        _clients.value = clientsRepo.loadAll()
    }

    // ── Units ──────────────────────────────────────────────

    fun addUnit(productUnit: ProductUnit) {
        _units.update { it + productUnit }
        unitsRepo.saveAll(_units.value)
    }

    fun updateUnit(productUnit: ProductUnit) {
        _units.update { list -> list.map { if (it.id == productUnit.id) productUnit else it } }
        unitsRepo.saveAll(_units.value)
    }

    fun deleteUnit(id: Int) {
        _units.update { it.filter { u -> u.id != id } }
        unitsRepo.saveAll(_units.value)
    }

    fun importUnitPhoto(unitId: Int, source: Path) {
        val fileName = ImageStorage.importPhoto(source)
        val unit = _units.value.find { it.id == unitId } ?: return
        updateUnit(unit.copy(photoPath = fileName))
    }

    fun nextUnitId(): Int = (_units.value.maxOfOrNull { it.id } ?: 0) + 1

    // ── Suppliers ──────────────────────────────────────────

    fun addSupplier(s: Supplier) {
        _suppliers.update { it + s }
        suppliersRepo.saveAll(_suppliers.value)
    }

    fun updateSupplier(s: Supplier) {
        _suppliers.update { list -> list.map { if (it.id == s.id) s else it } }
        suppliersRepo.saveAll(_suppliers.value)
    }

    fun deleteSupplier(id: Int) {
        _suppliers.update { it.filter { s -> s.id != id } }
        suppliersRepo.saveAll(_suppliers.value)
    }

    fun nextSupplierId(): Int = (_suppliers.value.maxOfOrNull { it.id } ?: 0) + 1

    // ── Clients ────────────────────────────────────────────

    fun addClient(c: Client) {
        _clients.update { it + c }
        clientsRepo.saveAll(_clients.value)
    }

    fun updateClient(c: Client) {
        _clients.update { list -> list.map { if (it.id == c.id) c else it } }
        clientsRepo.saveAll(_clients.value)
    }

    fun deleteClient(id: Int) {
        _clients.update { it.filter { c -> c.id != id } }
        clientsRepo.saveAll(_clients.value)
    }

    fun nextClientId(): Int = (_clients.value.maxOfOrNull { it.id } ?: 0) + 1
}