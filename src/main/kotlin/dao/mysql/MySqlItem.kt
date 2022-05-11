package dao.mysql

import dao.ItemDao
import Item
import db.DatabaseUtil
import java.math.BigDecimal
import java.sql.SQLException
import java.util.*


class MySqlItem : ItemDao {
    override fun getByBarcode(barcode: String): Item? {
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_BY_BARCODE)
                select.setString(1, barcode)
                val rs = select.executeQuery()
                //println(rs.first())
                //rs.next()
                //println(rs.getString("name"))
                if (!rs.next()) return null
                return try {
                    Item(rs)
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

    override fun getById(id: String): Item? {
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_BY_ID)
                select.setString(1, id.toString())
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return try {
                    Item(rs)
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

    override fun getAll(): List<Item> {
        val conn = DatabaseUtil.getConnection() ?: return emptyList()
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_ALL)
                val rs = select.executeQuery()
                var list = listOf<Item>()
                while(rs.next()){
                    try {
                        //list.plus(Item(rs))
                        list = list + Item(rs)
                    }catch (error: IllegalStateException) {
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

    override fun insert(obj: Item): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val select = it.prepareStatement(SQL_INSERT)
                select.setString(1, obj.uuid)
                select.setString(2, obj.barcode)
                select.setString(3, obj.name)
                select.setString(4, obj.price.toString())
                select.setString(5, obj.tax.toString())
                select.setString(6, obj.country)
                select.setString(7, obj.createdTime)
                select.setString(8, obj.lastModified)

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

    override fun update(obj: Item): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val select = it.prepareStatement(SQL_UPDATE)
                select.setString(1, obj.barcode)
                select.setString(2, obj.name)
                select.setString(3, obj.price.toString())
                select.setString(4, obj.tax.toString())
                select.setString(5, obj.country)
                select.setString(6, obj.createdTime)
                select.setString(7, obj.lastModified)
                select.setString(8, obj.uuid)

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

    override fun delete(obj: Item): Boolean {
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

    override fun getByPriceRange(bottom: BigDecimal, top : BigDecimal): List<Item>{
        val conn = DatabaseUtil.getConnection() ?: return emptyList()
        conn.use {
            try {
                val select = it.prepareStatement(SQL_PRICE_RANGE)
                select.setString(1, bottom.toString())
                select.setString(2, top.toString())
                val rs = select.executeQuery()
                var list = listOf<Item>()
                while(rs.next()){
                    try {
                        //list.plus(Item(rs))
                        list = list + Item(rs)
                    }catch (error: IllegalStateException) {
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
        private const val TABLE_NAME = "item"
        private const val SQL_GET_BY_BARCODE = "SELECT * FROM $TABLE_NAME WHERE  barcode = ? LIMIT 1"
        private const val SQL_INSERT =
            "INSERT INTO $TABLE_NAME (UUID, barcode, name, price, tax, country, createdTime, modifiedTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        private const val SQL_DELETE = "DELETE FROM $TABLE_NAME WHERE UUID = ? LIMIT 1"
        private const val SQL_GET_ALL = "SELECT * FROM $TABLE_NAME"
        private const val SQL_GET_BY_ID = "SELECT * FROM $TABLE_NAME WHERE UUID = ? LIMIT 1"
        private const val SQL_UPDATE = "UPDATE $TABLE_NAME SET barcode = ?, name = ?, price = ?, tax = ?, country = ?, createdTime = ?, modifiedTime = ? WHERE UUID = ?"
        private const val SQL_PRICE_RANGE = "SELECT * FROM $TABLE_NAME WHERE price BETWEEN ? AND ?"
    }
}