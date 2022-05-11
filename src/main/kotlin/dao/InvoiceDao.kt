package dao

import Invoice

interface InvoiceDao : DaoCrud<Invoice> {
    fun getByInvoiceNumber(invoiceNumber: String): Invoice?
    fun getByCashierId( cashierId : String): List<Invoice>
}