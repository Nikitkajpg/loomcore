package com.njpg.loomcore.model

import kotlinx.serialization.Serializable

/**
 * Заказ — центральная сущность приложения.
 *
 * Поддерживает два режима работы через поле [type]:
 * - [OrderType.PRODUCT] — пошив изделия.
 * - [OrderType.REPAIR] — ремонт.
 *
 * ## Расчёт цен
 * Для заказа типа PRODUCT:
 * - себестоимость ([cachedCost]) = стоимость материалов + часы × ставка в час
 * - итоговая цена ([finalPrice]) = себестоимость × (1 + наценка%)
 *
 * Для заказа типа REPAIR:
 * - [repairTotalPrice] = сумма [RepairOperation.totalPrice] по всем операциям
 *
 * Все ценовые поля сохраняются как снапшот на момент создания заказа,
 * чтобы последующее изменение прейскуранта или цен материалов
 * не меняло уже сохранённые заказы.
 *
 * @property id                Уникальный идентификатор заказа.
 * @property type              Тип заказа: пошив или ремонт.
 * @property clientId          Привязанный покупатель (nullable).
 * @property status            Текущий статус заказа.
 * @property startDate         Дата начала в формате ДД.ММ.ГГГГ.
 * @property endDate           Дата окончания (nullable).
 * @property productId         Шаблон изделия из каталога (только PRODUCT).
 * @property materialsUsed     Список расходов материалов (только PRODUCT).
 * @property workTimeHours     Затраченное время в часах (только PRODUCT).
 * @property photoPaths        Имена фотографий, хранящихся в [ImageStorage] (только PRODUCT).
 * @property cachedCost        Сохранённая себестоимость (только PRODUCT).
 * @property finalPrice        Сохранённая итоговая цена с наценкой (только PRODUCT).
 * @property repairOperations  Список выполненных операций ремонта (только REPAIR).
 * @property repairTotalPrice  Итоговая сумма ремонта (только REPAIR).
 * @property notes             Произвольные заметки к заказу.
 */
@Serializable
data class Order(
    val id: Int,
    val type: OrderType = OrderType.PRODUCT,
    val clientId: Int? = null,
    val status: OrderStatus = OrderStatus.IN_PROGRESS,
    val startDate: String = "",
    val endDate: String? = null,
    val productId: Int? = null,
    val materialsUsed: List<MaterialUsage> = emptyList(),
    val workTimeHours: Double = 0.0,
    val photoPaths: List<String> = emptyList(),
    val cachedCost: Double = 0.0,
    val finalPrice: Double = 0.0,
    val repairOperations: List<RepairOperation> = emptyList(),
    val repairTotalPrice: Double = 0.0,
    val notes: String = ""
)
