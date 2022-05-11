import java.math.BigDecimal
import java.sql.ResultSet
import kotlin.math.ceil

/**
 * *class* InternalItem that contains information about the produce
 *
 * @constructor creates an instance of InternalItem
 *
 * @param barcode the barcode of this item
 * @param newDiscount setting the discount of this item
 *
 * @property barcode the barcode of this item
 * @property tax the tax of this item
 * @property quantity the quantity of this item
 * @property country the country from where this item has originated (from barcode)
 * @property name the name of this item
 * @property price the price of this item
 * @property discount the discount of this item
 * @property uuid the id of the item
 * @property createdTime time when item instance was created
 * @property lastModified time when item was last modified
 * @property barcodeDepartment map containing department name, name of the produce, price and tax level
 *
 */
class InternalItem(
    barcode: String,
    newDiscount: BigDecimal = 0.toBigDecimal()
) : Item(barcode, "not set", 0.toBigDecimal(), 0.toBigDecimal(), Tax.B, newDiscount) {
    var internalId: String = ""
        private set(value) {
            field = value
            modify()
        }

    var deparment: String = ""
        private set(value) {
            field = value
            modify()
        }
    var deparmentId: String = ""
        private set(value) {
            field = value
            modify()
        }

    override var quantity: BigDecimal = 0.toBigDecimal()
        set(value) {
            field = round(value)
            val weight = (field * 1000.toBigDecimal()).toInt().toString().padStart(5, '0');
            barcode = getCheck(barcode.substring(0, 7) + weight)
            modify()
        }
/*
    /**
     * adds [quan] to Item quantity
     */
    override fun addQuantity(quan: BigDecimal) {
        modify()
        quantity += quan;
        val weight = (quantity.toInt() * 1000).toString().padStart(5, '0');
        barcode = getCheck(barcode.substring(0, 7) + weight)
    }

    /**
     * removes [quan] from Item quantity
     */
    override fun removeQuantity(quan: BigDecimal) {
        modify()
        quantity -= quan;
        val weight = (quantity.toInt() * 1000).toString().padStart(5, '0');
        barcode = getCheck(barcode.substring(0, 7) + weight)
    }*/


    private val barcodeDepartment: HashMap<String, Array<Any>> = hashMapOf(
        "2010001" to arrayOf("Zelenjava", "Zelje", 2.4.toBigDecimal(), Tax.B),
        "2010002" to arrayOf("Zelenjava", "Repa", 3.4.toBigDecimal(), Tax.B),
        "2010003" to arrayOf("Zelenjava", "Krompir", 3.4.toBigDecimal(), Tax.B),
        "2010004" to arrayOf("Zelenjava", "Blitva", 5.61.toBigDecimal(), Tax.B),
        "2020001" to arrayOf("Sadje", "Banane", 1.5.toBigDecimal(), Tax.B),
        "2020002" to arrayOf("Sadje", "Hruške", 2.5.toBigDecimal(), Tax.B),
        "2020003" to arrayOf("Sadje", "Jagode", 3.6.toBigDecimal(), Tax.B),
        "2020004" to arrayOf("Sadje", "Mango", 3.6.toBigDecimal(), Tax.B),
        "2030001" to arrayOf("Meso", "Piščančja prsa", 5.1.toBigDecimal(), Tax.B),
        "2030002" to arrayOf("Meso", "Svinjska Ribica", 6.7.toBigDecimal(), Tax.B),
        "2030003" to arrayOf("Meso", "Svinjska Krača", 6.3.toBigDecimal(), Tax.B),
        "2030003" to arrayOf("Meso", "Svinjski Vrat", 7.7.toBigDecimal(), Tax.B)
    )

    init {
        var item = barcodeDepartment[barcode.substring(0, 7)];
        if (item != null) {
            deparment = item[0] as String
            name = item[1] as String
            price = item[2] as BigDecimal
            tax = item[3] as Tax
            deparmentId = barcode.substring(0, 3)
            internalId = barcode.substring(3, 7);
            var weight = barcode.substring(7, 12).toBigDecimal()
            quantity = weight.divide(1000.toBigDecimal())
        } else {
            throw java.lang.IllegalArgumentException("No Item with that Barcode")
        }
    }

    /**
     * overrides toString method and writes out parameters of InternalItem
     * @return InternalItem in string
     */
    override fun toString(): String {
        var string = "$name Price: $price€/KG X $quantity KG  : ${calculatePrice()}€ code $barcode"
        if (discount != 0.toBigDecimal()) {
            string += "\n                         - $discount%  = ${finalPrice()}€"
        }
        return string
    }

    /**
     * receives the barcode and appends it the check digit to make it correct
     * @return correct barcode
     */
    fun getCheck(barcode: String): String {
        var i = 0
        var sum = 0.0
        while (i < barcode.length - 1) {
            sum += barcode[i].digitToInt()
            if (i + 1 != barcode.length) {
                sum += barcode[i + 1].digitToInt() * 3
            }
            i += 2
        }
        val ten = ceil(sum / 10.0) * 10
        return barcode + (ten - sum).toInt()
    }

    constructor(rs: ResultSet) : this(rs.getString("barcode")) {
        name = rs.getString("name")
        price = rs.getString("price").toBigDecimal()
        quantity = 0.toBigDecimal()
        tax = Tax.valueOf(rs.getString("tax"))
        lastModified = rs.getString("modifiedTime")
        createdTime = rs.getString("createdTime")
        uuid = rs.getString("UUID")
        //deparment = rs.getString("dep_name")
        deparmentId = rs.getString("Department_id")
    }

}


