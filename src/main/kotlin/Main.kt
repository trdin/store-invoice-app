import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

fun getNumber(lenght: Int): String {
    val allowedChars = ('0'..'9')
    return (1..lenght)
        .map { allowedChars.random() }
        .joinToString("")
}

fun getInvoiceNum(): String {
    return SimpleDateFormat("yy").format(Date()) + getNumber(10)
}

fun getBarcode(): String {
    var barcode = getNumber(12);
    return getCheck(barcode)
}

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

fun main(args: Array<String>) {

    var invoice = Invoice(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        4,
        2100,
        Company(
            "Kik D.O.O.",
            getNumber(8),
            getNumber(10),
            true,
            "Koroska ulica 19a",
            "2000",
            "Maribor"
        )
    )

    try {
        var itemBarcode1 = getBarcode();
        var itemBarcode2 = getBarcode();
        var itemBarcode3 = getBarcode();
        invoice.add(Item(itemBarcode1, "Skodelica", 1.60.toBigDecimal(), 1.0.toBigDecimal(), Tax.C));
        invoice.add(Item(itemBarcode2, "Krožnik", 4.60.toBigDecimal(), 1.0.toBigDecimal(), Tax.C));
        invoice.add(Item(itemBarcode2, "Krožnik", 4.60.toBigDecimal(), 1.0.toBigDecimal(), Tax.C));
        invoice.add(Item(itemBarcode3, "Hot wheels", 2.40.toBigDecimal(), 15.0.toBigDecimal(), Tax.B));
        invoice.remove(itemBarcode2, 0.5.toBigDecimal())
        invoice.remove(itemBarcode3)
        invoice.itemList.editItemQuan(itemBarcode1, BigDecimal(5))
        invoice.itemList.editItemPrice(itemBarcode2, BigDecimal(5.20))
        invoice.itemList.editItemDis(itemBarcode2, BigDecimal(3))
        invoice.add(InternalItem(getCheck("201000102200")))
        invoice.remove(getCheck("201000100200"))

        invoice.add(InternalItem(getCheck("202000302200")))
        invoice.remove(getCheck("202000302200"), 0.5.toBigDecimal() )

        invoice.add(InternalItem(getCheck("203000304300")))
        invoice.remove(getCheck("203000303100"))

        invoice.add(InternalItem(getCheck("203000204300")))
        invoice.itemList.editItemQuan(getCheck("203000204300"), BigDecimal(2.3))

        invoice.add(InternalItem(getCheck("201000204300")))
        invoice.remove(getCheck("201000204300"))


    } catch (error: IllegalArgumentException) {
        println("$error")
    } catch (error: IllegalStateException) {
        println("$error")
    }



    println("\n")
    println("=============")

    println("Invoice 2")
    println("\n")

    invoice.printInvoice()
    var item = InternalItem(getCheck("201000100200"))
    println(getCheck("201000100200").substring(0, 7))
    println(getCheck("201000100200").substring(7, 12))


}



