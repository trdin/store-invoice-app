package dao

import InternalItem
import Item
import java.math.BigDecimal

interface InternalItemDao: DaoCrud<InternalItem> {
    fun getByBarcode(barcode: String): InternalItem?
    fun getByPriceRange( bottom: BigDecimal, top : BigDecimal): List<Item>
}