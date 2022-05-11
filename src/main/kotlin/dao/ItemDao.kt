package dao

import Item
import java.math.BigDecimal

interface ItemDao : DaoCrud<Item> {
    fun getByBarcode(barcode: String): Item?
    fun getByPriceRange(bottom: BigDecimal, top : BigDecimal): List<Item>
}