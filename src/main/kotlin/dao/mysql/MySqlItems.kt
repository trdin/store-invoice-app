package dao.mysql

import InternalItem
import Item
import Items
import db.DatabaseUtil
import java.math.BigDecimal
import java.sql.PreparedStatement
import java.sql.ResultSetMetaData
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*

class MySqlItems {
    fun insertItems(obj: Item, invoiceUUID: String): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                var testItem = getItem(obj.uuid, invoiceUUID);
                val select: PreparedStatement;
                //tying something                //todo if it exist  do an update
                if (testItem == null) {
                    select = it.prepareStatement(SQL_INSERT_ITEM)
                    select.setString(1, invoiceUUID)
                    select.setString(2, UUID.randomUUID().toString().substring(0, 16))
                    select.setString(3, SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date()))
                    select.setString(4, SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date()))
                    select.setString(5, obj.uuid)
                    select.setString(6, obj.quantity.toString())
                    select.setString(7, obj.discount.toString())
                    //(Invoice_UUID, UUID, createdTime, modifiedTime, Item_UUID, quantity, discount)
                } else {
                    select = it.prepareStatement(SQL_UPDATE_QUAN_ITEM)
                    select.setBigDecimal(1, obj.quantity)
                    select.setString(2, testItem.uuid)
                    select.setString(3, invoiceUUID)

                }
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

    fun insertInternalItems(obj: InternalItem, invoiceUUID: String): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                var testItem = getInternalItem(obj.uuid, invoiceUUID);
                val select: PreparedStatement;
                //tying something                //todo if it exist  do an update
                if (testItem == null) {
                    select = it.prepareStatement(SQL_INSERT_INTERNALITEM)
                    select.setString(1, invoiceUUID)
                    select.setString(2, UUID.randomUUID().toString().substring(0, 16))
                    select.setString(3, SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date()))
                    select.setString(4, SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date()))
                    select.setString(5, obj.uuid)
                    select.setBigDecimal(6, obj.quantity)
                    select.setBigDecimal(7, obj.discount)
                } else {

                    select = it.prepareStatement(SQL_UPDATE_QUAN_INTERNALITEM)
                    select.setBigDecimal(1, obj.quantity)
                    select.setString(2, testItem.uuid)
                    select.setString(3, invoiceUUID)

                }
                //(Invoice_UUID, UUID, createdTime, modifiedTime, Item_UUID, quantity, discount)
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

    fun getItem(itemId: String, invoiceId: String): Item? {
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_ITEM_INVOICE)
                select.setString(1, invoiceId)
                select.setString(2, itemId)
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return try {
                    var item = Item(rs)
                    item.quantity = rs.getBigDecimal("quantity")
                    item.discount = rs.getBigDecimal("discount")
                    item
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

    fun getInternalItem(itemId: String, invoiceId: String): InternalItem? {
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_INTERNALITEM_INVOICE)
                select.setString(1, invoiceId)
                select.setString(2, itemId)
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return try {
                    var item = InternalItem(rs)
                    item.quantity = rs.getBigDecimal("quantity")
                    item.discount = rs.getBigDecimal("discount")
//                    val rsmd: ResultSetMetaData = rs.metaData
//                    val columnsNumber = rsmd.columnCount
//                    for (i in 1..columnsNumber) {
//                        println(rsmd.getColumnName(i))
//                    }
                    item
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

    fun getItems(invoiceUUID: String): Items? {
        var items = Items()
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_ITEMS_INVOICE)
                select.setString(1, invoiceUUID)
                val rs = select.executeQuery()
                while (rs.next()) {
                    try {
                        //list.plus(Item(rs))
                        var item = Item(rs)
                        item.quantity = rs.getBigDecimal("quantity")
                        item.discount = rs.getBigDecimal("discount")

                        items.put(item.barcode, item);
                    } catch (error: IllegalStateException) {
                        println("$error")
                        return null
                    }
                }
                return items
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return null
    }

    fun getInternalItems(invoiceUUID: String): Items? {
        var items = Items()
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(MySqlItems.SQL_GET_INTERNALITEMS_INVOICE)
                select.setString(1, invoiceUUID)
                val rs = select.executeQuery()
                while (rs.next()) {
                    try {
                        //list.plus(Item(rs))
                        var item = InternalItem(rs)
                        item.quantity = rs.getBigDecimal("quantity")
                        item.discount = rs.getBigDecimal("discount")

                        items.put(item.barcode, item);
                    } catch (error: IllegalStateException) {
                        println("$error")
                        return null
                    }
                }
                return items
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return null
    }

    fun deleteItem(itemUUID: String, quan: BigDecimal, invoiceUUID: String): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                var testItem = getItem(itemUUID, invoiceUUID);

                var select: PreparedStatement;
                if (testItem != null) {
                    if (testItem.quantity.compareTo(quan) == 0) {
                        select = it.prepareStatement(SQL_DELETE_ITEM)
                        select.setString(1, itemUUID)
                        select.setString(2, invoiceUUID)
                    } else {
                        val sum = testItem.quantity.subtract(quan)
                        select = it.prepareStatement(SQL_UPDATE_QUAN_ITEM)
                        select.setBigDecimal(1, sum)
                        select.setString(2, itemUUID)
                        select.setString(3, invoiceUUID)
                        println(select)
                    }
                    val rs = select.executeUpdate()
                    if (rs == 1) {
                        return true
                    } else {
                        false
                    }
                } else {
                    throw IllegalStateException("Remove: this item does not exist")
                }

            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    fun deleteInternalItem(itemUUID: String, quan: BigDecimal, invoiceUUID: String): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                var testItem = getInternalItem(itemUUID, invoiceUUID);
                var select: PreparedStatement;
                if (testItem != null) {
                    if (testItem.quantity.compareTo(quan) == 0) {
                        select = it.prepareStatement(SQL_DELETE_INTERNAL_ITEM)
                        select.setString(1, itemUUID)
                        select.setString(2, invoiceUUID)
                    } else {
                        val sum = testItem.quantity - quan
                        select = it.prepareStatement(SQL_UPDATE_QUAN_INTERNALITEM)
                        select.setBigDecimal(1, sum)
                        select.setString(2, itemUUID)
                        select.setString(3, invoiceUUID)
                    }
                    val rs = select.executeUpdate()
                    if (rs == 1) {
                        return true
                    } else {
                        false
                    }
                } else {
                    throw IllegalStateException("Remove: this item does not exist")
                }

            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    /*fun removeQuanItem(itemUUID: String, quan: BigDecimal, invoiceUUID: String): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {

            try {
                var testItem = getItem(itemUUID, invoiceUUID);
                if (testItem != null) {
                    val sum = testItem.quantity - quan
                    val select = it.prepareStatement(SQL_UPDATE_QUAN_ITEM)
                    select.setString(1, sum.toString())
                    select.setString(2, testItem.uuid)
                    select.setString(3, invoiceUUID)
                    val rs = select.executeUpdate()
                    if (rs == 1) {
                        return true
                    } else {
                        false
                    }
                } else {
                    throw IllegalStateException("Remove: this item does not exist")
                }


            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }

    fun removeQuanInternalItem(itemUUID: String, quan: BigDecimal, invoiceUUID: String): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {

            try {
                var testItem = getInternalItem(itemUUID, invoiceUUID);
                if (testItem != null) {
                    val sum = testItem.quantity - quan
                    val select = it.prepareStatement(SQL_UPDATE_QUAN_INTERNALITEM)
                    select.setString(1, sum.toString())
                    select.setString(2, testItem.uuid)
                    select.setString(3, invoiceUUID)
                    val rs = select.executeUpdate()
                    if (rs == 1) {
                        return true
                    } else {
                        false
                    }
                } else {
                    throw IllegalStateException("Remove: this item does not exist")
                }


            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
        return false
    }*/

    companion object {
        //insert items
        private const val SQL_INSERT_ITEM =
            "INSERT INTO items (Invoice_UUID, UUID, createdTime, modifiedTime, Item_UUID, quantity, discount) VALUES (?, ?, ?, ?, ?, ?, ?)"
        private const val SQL_INSERT_INTERNALITEM =
            "INSERT INTO internalitems (Invoice_UUID, UUID, createdTime, modifiedTime, InternalItem_UUID, quantity, discount) VALUES (?, ?, ?, ?, ?, ?, ?)"

        //get one item
        private const val SQL_GET_ITEM_INVOICE =
            "SELECT item.*, quantity, discount FROM items INNER JOIN item ON item.UUID = items.Item_UUID WHERE items.Invoice_UUID = ? AND item.UUID = ? "
        private const val SQL_GET_INTERNALITEM_INVOICE =
            "SELECT internalitem.*, quantity, discount FROM internalitems INNER JOIN internalitem ON internalitem.UUID = internalitems.InternalItem_UUID WHERE internalitems.Invoice_UUID = ? AND internalitem.UUID = ? "

        //gets items list
        private const val SQL_GET_ITEMS_INVOICE =
            "SELECT item.*, quantity, discount FROM  items INNER JOIN item ON item.UUID = items.Item_UUID WHERE items.Invoice_UUID = ?"
        private const val SQL_GET_INTERNALITEMS_INVOICE =
            "SELECT internalitem.*, quantity, discount FROM  internalitems INNER JOIN internalitem ON internalitem.UUID = internalitems.InternalItem_UUID WHERE internalitems.Invoice_UUID = ?"

        //deleting
        private const val SQL_DELETE_ITEM = "DELETE FROM items WHERE Item_UUID = ? AND Invoice_UUID = ?"
        private const val SQL_DELETE_INTERNAL_ITEM =
            "DELETE FROM internalitems WHERE InternalItem_UUID = ? AND Invoice_UUID = ?"

        //adding quantity
        private const val SQL_UPDATE_QUAN_ITEM =
            "UPDATE items SET quantity = ? WHERE Item_UUID = ? AND Invoice_UUID = ?"
        private const val SQL_UPDATE_QUAN_INTERNALITEM =
            "UPDATE internalitems SET quantity = ? WHERE InternalItem_UUID = ? AND Invoice_UUID = ?"
    }
}