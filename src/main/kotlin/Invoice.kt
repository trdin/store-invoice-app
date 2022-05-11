import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.ResultSet
import java.text.SimpleDateFormat
import java.util.*
import dao.mysql.MySqlCompany
import dao.mysql.MySqlInvoice
import dao.mysql.MySqlItem
import dao.mysql.MySqlItems

/**
 * counts the number of invoices issued
 */
var numberOfInvoices = 1;


/**
 * *class* Invoice contains information about the purchase and items
 *
 * @constructor creates an instance of invoice
 *
 * @param cashierId Id of the cashier recording the purchase
 * @param branch Id of the store branch where the purchase is taking place
 * @param register Id of the register where the purchase is taking place
 * @param transNum Id of the transaction
 * @param issuer class Company it gives information about the company which issued the invoice
 * @param newCard setting card, default value is null if the purchase is by cash
 * @param newCustomer setting customer, default value is null if the customer is not a company
 *
 * @property uuid the id of the item
 * @property createdTime time when item instance was created
 * @property lastModified time when item was last modified
 * @property priceToPay price the customer will pay
 * @property invoiceNumber number that gives information about the invoice
 * @property totalPrice price of the produce without the discounts
 * @property discountPurchase discount on the purchase in %
 * @property customer information about the customer if it is a company
 * @property card credit card number
 * @property cashierId Id of the cashier recording the purchase
 * @property branch Id of the store branch where the purchase is taking place
 * @property register Id of the register where the purchase is taking place
 * @property transNum Number that gives information about the purchase
 * @property issuer class Company it gives information about the company which issued the invoice
 */
class Invoice(
    val cashierId: String,
    val branch: String,
    val register: Int,
    val transNum: Int,
    var issuer: Company?,
    newCard: String? = "",
    newCustomer: Company? = null
) {

    val invoiceNumber: String = SimpleDateFormat("yy").format(Date()) + numberOfInvoices.toString().padStart(10, '0')

    var priceToPay = 0.toBigDecimal()
        set(value) {
            field = value
            modify()
        }
    private var totalPrice = 0.toBigDecimal()
    var discountPurchase = 0.toBigDecimal()
        set(value){
            field = value
            modify()
            updatePrice()
        }
    var uuid = UUID.randomUUID().toString().substring(0, 16)
    var itemList = Items()
        set(value) {
            field = value
            modify()
        }
    var customer = newCustomer
        set(value) {
            field = value
            modify()
        }
    var card = newCard
        set(value) {
            field = value
            modify()
        }

    var createdTime = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date())
    var lastModified = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date())
        private set

    init {
        numberOfInvoices++
    }

    /**
     * adds item to the items list
     */
    fun add(item: Item) {
        //itemList[item.barcode] = item
        //var condition = itemList.put(item.barcode, item)
        //println(condition)

        if (itemList.put(item.barcode, item) != null) {
            modify()
            var itemsSql = MySqlItems()
            if (item::class.simpleName == "InternalItem") {
                itemsSql.insertInternalItems(item as InternalItem, uuid)
            } else {
                itemsSql.insertItems(item, uuid)
            }
        }
        updatePrice()
    }

    /**
     * removes item from the items list using barcode
     */
    fun remove(barcode: String) {
        var item = itemList.remove(barcode);
        if (item != null) {
            modify()
            var itemsSql = MySqlItems()
            if (item::class.simpleName == "InternalItem") {
                itemsSql.deleteInternalItem(item.uuid, item.quantity, uuid)
            } else {
                itemsSql.deleteItem(item.uuid, item.quantity, uuid)
            }
        } else {
            throw IllegalStateException("Remove: this item does not exist")
        }
        updatePrice()
    }

    /**
     * removes quantity of items from items list
     */
    fun remove(barcode: String, quan: BigDecimal) {
        var item = itemList[barcode];
        if (item != null) {
            var data = Item(barcode, item.name, item.price, quan, item.tax)
            if (!itemList.remove(barcode, data)) {
                throw IllegalStateException("Remove: Too much quantity removed")
            }else{
                var itemsSql = MySqlItems()
                if (item::class.simpleName == "InternalItem") {
                    itemsSql.deleteInternalItem(item.uuid, quan, uuid)
                } else {
                    itemsSql.deleteItem(item.uuid, quan, uuid)
                }
            }
        } else {
            throw IllegalStateException("Remove: this item does not exist")
        }
        updatePrice()
    }

    /**
     * calculates total price that customer will pay
     * @return price
     */
    fun calculatePriceToPay() {
        var price = itemList.calculatePrice()
        priceToPay = round(price - (discountPurchase.divide(BigDecimal(100))) * price)
    }

    /**
     * rounds the number [numb] to 2 decimal
     * @return rounded number
     */
    private fun round(numb: BigDecimal): BigDecimal = numb.setScale(2, RoundingMode.HALF_EVEN)


    /**
     * calculates tax payed for each tax level
     * @return linkedHashMap with tax names and amount paid in taxes
     */
    fun taxLevels(): LinkedHashMap<Tax, BigDecimal?> {//calculates  each tax level and return a map with tax values
        var taxLevels: LinkedHashMap<Tax, BigDecimal?> = linkedMapOf(
            Tax.A to 0.toBigDecimal(),
            Tax.B to 0.toBigDecimal(),
            Tax.C to 0.toBigDecimal(),
            Tax.D to 0.toBigDecimal()
        )
        for ((k, v) in itemList) {
            taxLevels[v.tax] = taxLevels[v.tax]?.plus(v.calculateTax())
        }
        for ((k, v) in taxLevels) {
            if (v != null) {
                taxLevels[k] = round(v - (discountPurchase.divide(BigDecimal(100))) * v)
            }
        }

        return taxLevels
    }

    /**
     * writes out the amount paid in tax by tax level
     * @return string with tax amounts
     */
    private fun taxLevelsToString(): String {
        val taxLevels = taxLevels()
        var string = ""
        var sumTax = 0.toBigDecimal()
        for ((k, v) in taxLevels) {
            if (v != round(BigDecimal(0))) {
                if (v != null) {
                    sumTax += v
                }
                string += "Tax level ${k.name} ${k.value}%: $v€ \n"
            }
        }
        string += "Tax together: ${round(sumTax)}€"
        return string
    }

    /**
     * prints out the invoice
     */
    fun printInvoice() {
        calculatePriceToPay();
        println(issuer.toString())
        println("==============")
        println("branch: $branch");
        println("cash register: $register")
        println("==============")

        if (customer != null) {
            println(customer.toString())
            if (customer!!.taxpayer) {
                println("Taxpayer")
            } else {
                println("not a taxpayer")
            }
            println("==============")
        }


        println(itemList.toString())
        if (discountPurchase != 0.toBigDecimal()) {
            println("Price: ${itemList.calculatePrice()}€")
            println("-discount: $discountPurchase%")
        }
        println("Total Price: $priceToPay€")
        println("==============")

        if (card == "") {
            println("Način plačila: gotovina")
        } else {
            println("Način plačila: Kartica ${hideCard()}")
        }
        println("==============")

        println(taxLevelsToString());
        println("==============")

        println("cashier: $cashierId")
        println("datum in čas: $createdTime");
        println("Transaction Number: $transNum")
        println("Invoice Number: $invoiceNumber")
    }

    /**
     * updates [lastModified] when called
     */
    private fun modify() {
        lastModified = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date())
    }

    /**
     * hides customer card number when printing the invoice
     * @return hidden card number
     */
    private fun hideCard(): String {
        var string = ""
        for (i in 0..card!!.length - 5) {
            string += "X"
        }
        string += card?.subSequence(card!!.length - 4, card!!.length)

        return string.toString()
    }

    constructor(rs: ResultSet) : this(
        rs.getString("cashierId"),
        rs.getString("branch"),
        rs.getInt("register"),
        rs.getInt("transNumb"),
        null
    ) {
        this.uuid = rs.getString("UUID")
        //TODO company constructor or call the company sql
        val companySql = MySqlCompany();
        this.issuer = companySql.getById(rs.getString("Issuer_UUID"))
        if (rs.getString("Customer_UUID") != "") {
            this.customer = companySql.getById(rs.getString("Customer_UUID"))
        }
        if (rs.getString("card") != "") {
            this.card = rs.getString("card")
        }
        val itemsSql = MySqlItems();
        this.itemList = itemsSql.getItems(this.uuid)!!
        itemList.putAll(itemsSql.getInternalItems(this.uuid)!!)
    }

    fun updatePrice(){
        var sql = MySqlInvoice();
        calculatePriceToPay()
        sql.update(this)

    }
}

//stevilka kartice