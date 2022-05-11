package dao.mysql

import dao.InvoiceDao
import Invoice
import db.DatabaseUtil
import java.sql.SQLException
import java.util.*

class MySqlInvoice : InvoiceDao {
    override fun getById(id: String): Invoice? {
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(MySqlInvoice.SQL_GET_BY_ID)
                select.setString(1, id.toString())
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return try {
                    Invoice(rs)
                } catch (error: IllegalStateException) {
                    println("$error")
                    null
                }
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return null

    }

    override fun getAll(): List<Invoice> {
        val conn = DatabaseUtil.getConnection() ?: return emptyList()
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_ALL)
                val rs = select.executeQuery()
                var list = listOf<Invoice>()
                while (rs.next()) {
                    try {
                        //list.plus(Item(rs))
                        list = list + Invoice(rs)
                    } catch (error: IllegalStateException) {
                        println("$error")
                        return emptyList()
                    }
                }
                return list.ifEmpty {
                    emptyList()
                }
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return emptyList()
    }

    override fun insert(obj: Invoice): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val select = it.prepareStatement(MySqlInvoice.SQL_INSERT)
                select.setString(1, obj.uuid)
                select.setString(2, obj.priceToPay.toString())
                select.setString(3, obj.invoiceNumber)
                select.setString(4, obj.discountPurchase.toString())
                select.setString(5, obj.card)
                select.setString(6, obj.cashierId)
                select.setString(7, obj.branch)
                select.setString(8, obj.register.toString())
                select.setString(9, obj.transNum.toString())
                select.setString(10, obj.createdTime)
                select.setString(11, obj.lastModified)
                select.setString(12, obj.customer?.uuid ?: "")
                select.setString(13, obj.issuer?.uuid ?: "")

                val rs = select.executeUpdate()
                if (rs == 1) {
                    return true
                } else {
                    false
                }
                //if (rs.first()) return false

            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    override fun update(obj: Invoice): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val select = it.prepareStatement(SQL_UPDATE)
                select.setString(1, obj.priceToPay.toString())
                select.setString(2, obj.invoiceNumber)
                select.setString(3, obj.discountPurchase.toString())
                select.setString(4, obj.card)
                select.setString(5, obj.cashierId)
                select.setString(6, obj.branch)
                select.setString(7, obj.register.toString())
                select.setString(8, obj.transNum.toString())
                select.setString(9, obj.createdTime)
                select.setString(10, obj.lastModified)
                select.setString(11, obj.customer?.uuid ?: "")
                select.setString(12, obj.issuer?.uuid ?: "")
                select.setString(13, obj.uuid)
                val rs = select.executeUpdate()
                if (rs == 1) {
                    return true
                } else {
                    false
                }
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    override fun delete(obj: Invoice): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val select = it.prepareStatement(SQL_DELETE)
                select.setString(1, obj.uuid)

                val rs = select.executeUpdate()
                if (rs == 1) {
                    return true
                } else {
                    false
                }

            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    override fun getByInvoiceNumber(invoiceNumber: String): Invoice? {
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_INVOICE_NUM)
                select.setString(1, invoiceNumber)
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return try {
                    Invoice(rs)
                } catch (error: IllegalStateException) {
                    println("$error")
                    null
                }
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return null

    }

    override fun getByCashierId(cashierId: String): List<Invoice> {
        val conn = DatabaseUtil.getConnection() ?: return emptyList()
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_CASHIER)
                select.setString(1, cashierId)

                val rs = select.executeQuery()
                var list = listOf<Invoice>()
                while (rs.next()) {
                    try {
                        list = list + Invoice(rs)
                    } catch (error: IllegalStateException) {
                        println("$error")
                        return emptyList()
                    }
                }
                return list.ifEmpty {
                    emptyList()
                }
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return emptyList()
    }

    companion object {
        private const val TABLE_NAME = "invoice"
        private const val SQL_INSERT =
            "INSERT INTO $TABLE_NAME (UUID, priceToPay, invoiceNumber, discount, card, cashierId, branch, register, transNumb, createdTime,modifiedTime, Customer_UUID, Issuer_UUID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        private const val SQL_DELETE = "DELETE FROM $TABLE_NAME WHERE UUID = ? LIMIT 1"
        private const val SQL_GET_ALL = "SELECT * FROM $TABLE_NAME"
        private const val SQL_GET_BY_ID = "SELECT * FROM $TABLE_NAME WHERE UUID = ? LIMIT 1"
        private const val SQL_UPDATE =
            "UPDATE $TABLE_NAME SET priceToPay = ?, invoiceNumber = ?, discount = ?, card = ?, cashierId = ?, branch = ?, register = ?, transNumb = ?, createdTime = ?,modifiedTime = ?, Customer_UUID = ?, Issuer_UUID = ? WHERE UUID = ?"
        private const val SQL_GET_INVOICE_NUM = "SELECT * FROM $TABLE_NAME WHERE invoiceNumber = ? LIMIT 1"
        private const val SQL_GET_CASHIER = "SELECT * FROM $TABLE_NAME WHERE cashierId = ?"

    }

}