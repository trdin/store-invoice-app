import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import util.BarcodeUtil.isBarcodeValid
import util.BarcodeUtil.getCompanyCountryFromBarcode
import java.sql.ResultSet


/**
 * *Enum class* Tax
 * Tax class it contains tax options.
 *
 * @param value BigDecimal which tax property is selected
 * @property A 0%
 * @property B 9.5%
 * @property C 22%
 * @property D 5%
 */

enum class Tax(val value: BigDecimal) {
    A(0.toBigDecimal()), B(9.5.toBigDecimal()), C(22.toBigDecimal()), D(5.toBigDecimal())
}

/**
 * *open class* Item that contains information about the produce
 *
 * @constructor creates an instance of Item
 *
 * @param barcode the barcode of this item
 * @param newName setting the name of this item
 * @param newPrice setting the price of this item
 * @param newQuantity setting the quantity of this item
 * @param tax the tax of this item
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
 *
 */
open class Item(
    var barcode: String,
    newName: String,
    newPrice: BigDecimal,
    newQuantity: BigDecimal,
    var tax: Tax,
    newDiscount: BigDecimal = 0.toBigDecimal()
) {
    open var quantity = newQuantity
        set(value) {
            field = round(value)
            modify()
        }

    var country: String = ""
        protected set
    var name = newName
        set(value) {
            field = value
            modify()
        }
    var price = round(newPrice)
        set(value) {
            field = round(value)
            modify()
        }
    var discount = newDiscount
        set(value) {
            field = round(value)
            modify()
        }

    init {
        if (isBarcodeValid(barcode)) {
            country = getCompanyCountryFromBarcode(barcode)
        } else {
            throw IllegalArgumentException("barcode is not valid")
        }
    }

    var uuid = UUID.randomUUID().toString().substring(0, 16)
    var createdTime = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date())
    var lastModified = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date())

    /**
     * calculates the price of the items
     * @return the price of the quantity of items
     */
    fun calculatePrice(): BigDecimal = round(price * quantity)

    /**
     * calculates the discount of the items
     * @return the discount amount in euros of items
     */
    fun calculateDiscount(): BigDecimal = (discount.divide(BigDecimal(100)) * calculatePrice())

    /**
     * calculates the finalprice of the items
     * @return the price of the quantity of items discount is subtracted
     */
    fun finalPrice(): BigDecimal = round(calculatePrice() - calculateDiscount())

    /**
     * calculates the tax amount paid int euros
     * @return the tax amount paid
     */
    fun calculateTax(): BigDecimal = (tax.value.divide(BigDecimal(100))) * finalPrice()

    /**
     * rounds the number [numb] to 2 decimal
     * @return rounded number
     */
    protected fun round(numb: BigDecimal): BigDecimal = numb.setScale(2, RoundingMode.HALF_EVEN)

    /**
     * adds [quan] to Item quantity
     */
    open fun addQuantity(quan: BigDecimal) {
        quantity += quan
        modify()
    }

    /**
     * removes [quan] from Item quantity
     */
    open fun removeQuantity(quan: BigDecimal) {
        quantity -= quan
        modify()
    }
    /**
     * updates [lastModified] when called
     */
    protected fun modify() {
        lastModified = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date())
    }

    /**
     * overrides toString method and writes out parameters of Item
     * @return Item in string
     */
    override fun toString(): String {
        var string = "$name Price: $price X $quantity  : ${calculatePrice()}€"
        if (discount != 0.toBigDecimal()) {
            string += "\n                         - $discount%  = ${finalPrice()}€"
        }
        return string
    }
    constructor() : this("","", 0.toBigDecimal(), 0.toBigDecimal(), Tax.A)
    fun mapDataToObject( rs: ResultSet ): Item{
        var tax : Tax = Tax.valueOf(rs.getString("tax"));
        var item = Item(
            rs.getString("barcode"),
            rs.getString("name"),
            rs.getBigDecimal("price"),
            0.toBigDecimal(),
            tax,
        )
        item.lastModified = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(rs.getString("modifiedTime"))
        item.createdTime = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(rs.getString("createdTime"))
        return item
    }

    constructor(rs: ResultSet): this(rs.getString("barcode"),
        rs.getString("name"),
        rs.getString("price").toBigDecimal(),
        0.toBigDecimal(),
        Tax.A){
        this.uuid = rs.getString("UUID")
        this.tax = Tax.valueOf(rs.getString("tax"))
        this.lastModified = rs.getString("modifiedTime")
        this.createdTime = rs.getString("createdTime")
    }
}

