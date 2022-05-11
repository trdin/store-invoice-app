import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

/**
 * *class* Items map of instances of Item
 *
 * @constructor creates an instance of Items
 *
 * @property uuid the id of the Items
 * @property createdTime time when Items instance was created
 * @property lastModified time when Items was last modified
 *
 */

class Items : LinkedHashMap<String, Item>() {

    private val uuid = UUID.randomUUID().toString()
    var createdTime = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date())
    var lastModified = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date())
        private set

    /**
     * adds item to the items map
     * @return the added item or null if it was unsuccessful
     */
    override fun put(key: String, value: Item): Item? {
        modify()
        if (value::class.simpleName == "InternalItem") {
            for ((k, v) in this) {
                if (k.substring(0, 7) == key.substring(0, 7)) {
                    v.addQuantity(value.quantity)
                    return v
                }
            }
            super.put(key, value)
            return get(key)
        } else {
            if (!super.containsKey(key)) {
                super.put(key, value)
                return get(key)
            } else {
                this.getValue(key).addQuantity(value.quantity)
                return get(key)
            }
        }
    }

    /**
     * removes item from Items map using barcode
     * @return the removed  item or null if it was unsuccessful
     */
    override fun remove(key: String): Item? {
        modify()

        for ((k, v) in this) {
            if (v::class.simpleName != "InternalItem") continue

            val quan = key.substring(7, 12).toBigDecimal().divide(1000.toBigDecimal())

            if (k.substring(0, 7) == key.substring(0, 7)) {
                if (quan.compareTo(v.quantity) == 0) {
                    return super.remove(key)
                } else if (quan < v.quantity) {
                    v.removeQuantity(quan)
                    return v
                }
            }
        }
        if (super.containsKey(key)) {
            return super.remove(key)

        }
        return null
    }

    /**
     * subtracts the quantity form an item or removes it
     * @return true if successful else it returns false
     */
    override fun remove(key: String, value: Item): Boolean {
        var item = get(key)
        if (value::class.simpleName == "InternalItem") {
            for ((k, v) in this) {
                if (k.substring(0, 7) == key.substring(0, 7)) {
                    item = v
                    break
                }
            }
        }

        if (item != null) {
            if (item.quantity.compareTo(value.quantity) == 0) {
                if (remove(key) == null) {
                    return false
                }
                return true
            } else if (item.quantity > value.quantity) {
                item.removeQuantity(value.quantity)
                return true
            }
        }
        return false
    }

    /**
     * edits item quantity found by barcode in map
     */
    fun editItemQuan(barcode: String, quan: BigDecimal) {
        val item = get(barcode) ?: throw IllegalStateException("Edit Quantity: this item does not exist")
        item.quantity = quan
    }

    /**
     * edits item discount found by barcode in map
     */
    fun editItemDis(barcode: String, discount: BigDecimal) {
        var item: Item? = get(barcode) ?: throw IllegalStateException("Edit Discount: this item does not exist")
        get(barcode)?.discount = discount
    }

    /**
     * edits item price found by barcode in map
     */
    fun editItemPrice(barcode: String, price: BigDecimal) {
        var item: Item? = get(barcode) ?: throw IllegalStateException("Edit Price: this item does not exist")
        get(barcode)?.price = price
    }

    /**
     * calculates total price of all the items in map
     * @return price of items
     */
    fun calculatePrice(): BigDecimal {
        var price = 0.0.toBigDecimal()
        for ((k, v) in this) {
            price += v.finalPrice()
        }
        return price
    }

    /**
     * calculates total tax of all the items in map
     * @return tax of items
     */
    fun calculateTax(): BigDecimal {
        var tax = 0.0.toBigDecimal()
        for ((k, v) in this) {
            tax += v.calculateTax()
        }
        return tax
    }

    /**
     * updates [lastModified] when called
     */
    private fun modify() {
        lastModified = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date())
    }

    /**
     * overrides toString method and writes all the items
     * @return Items in string
     */
    override fun toString(): String {
        var string = ""
        for ((k, v) in this) {
            string += v.toString() + "\n"
        }
        return string
    }
}