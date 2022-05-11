import dao.mysql.*
import java.util.*


fun main(args: Array<String>) {
   /*val sqlItem = MySqlItem();
    val sqlIntItem = MySqlInternalItem();
    val item1 = Item(getBarcode(), "Skodelica", 1.60.toBigDecimal(), 1.0.toBigDecimal(), Tax.C)
    var item2 = Item(getBarcode(), "Hot wheels", 2222.60.toBigDecimal(), 15.0.toBigDecimal(), Tax.B)
    var intItem1 = InternalItem(getCheck("201000102200"))
    var intItem2 = InternalItem(getCheck("203000304300"))
    var intItem3 = InternalItem(getCheck("202000302200"))

    println(sqlItem.insert(item1))
    println(sqlItem.insert(item2))
    println(sqlIntItem.insert(intItem1))
    println(sqlIntItem.insert(intItem2))
    println(sqlIntItem.insert(intItem3))
    var listItems = sqlItem.getAll();
    for(i in listItems) {
        println(i.toString())
    }
    var listInternal = sqlIntItem.getAll()
    for(i in listInternal) {
        println(i.toString())
    }*/
    /*var sqlCompany = MySqlCompany();
    var comp1 = Company(
        "Kik D.O.O.",
        getNumber(8),
        getNumber(10),
        true,
        "Koroska ulica 19a",
        "2000",
        "Maribor"
    )
    var comp2 = Company(
        "Burek D.O.O.",
        getNumber(8),
        getNumber(10),
        true,
        "BOSNA 19a",
        "2100",
        "Bosna"
    )
    println(sqlCompany.insert(comp1))
    println(sqlCompany.insert(comp2))

    println(sqlCompany.getById(comp1.uuid).toString())*/
   println("invoice test")
    val sqlItem = MySqlItem();
    var listItems = sqlItem.getAll();
    val sqlIntItem = MySqlInternalItem();
    var listInternal = sqlIntItem.getAll()

    var sqlCompany = MySqlCompany();
    var listCompany = sqlCompany.getAll()
    var invoice = Invoice(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        4,
        2100,
        listCompany[0],
        "",
        listCompany[1]
    )
    var sqlInvoice = MySqlInvoice();

    println(sqlInvoice.insert(invoice))
    listItems[0].quantity = 10.toBigDecimal()
    listItems[0].discount = 18.toBigDecimal()
    invoice.add(listItems[0])
    listItems[1].quantity = 3.toBigDecimal()
    invoice.add(listItems[1])
    listInternal[0].quantity = 4.toBigDecimal()
    invoice.add(listInternal[0])
    listInternal[1].quantity = 1.2.toBigDecimal()
    invoice.add(listInternal[1])
    invoice.remove(listItems[1].barcode, 3.toBigDecimal())
    listInternal[0].quantity = 4.toBigDecimal()
    println(listInternal)
    invoice.add(listInternal[0])
    invoice.printInvoice()
    println("invoice form database\n")
    var sql = MySqlItems()
    sqlInvoice.getById(invoice.uuid)?.printInvoice()



}



