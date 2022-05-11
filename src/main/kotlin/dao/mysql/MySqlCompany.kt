package dao.mysql

import dao.CompanyDao
import db.DatabaseUtil
import java.sql.SQLException
import java.util.*
import Company

class MySqlCompany : CompanyDao {
    override fun getById(id: String): Company? {
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_BY_ID)
                select.setString(1, id.toString())
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return try {
                    Company(rs)
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

    override fun getAll(): List<Company> {
        val conn = DatabaseUtil.getConnection() ?: return emptyList()
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_ALL)
                val rs = select.executeQuery()
                var list = listOf<Company>()
                while (rs.next()) {
                    try {
                        //list.plus(Company(rs))
                        list = list + Company(rs)
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

    override fun insert(obj: Company): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val select = it.prepareStatement(SQL_INSERT)
                select.setString(1, obj.uuid)
                select.setString(2, obj.name)
                select.setString(3, obj.taxNumber)
                select.setString(4, obj.registrationNumber)
                select.setBoolean(5, obj.taxpayer)
                select.setString(6, obj.address)
                select.setString(7, obj.postNumber)
                select.setString(8, obj.post)
                select.setString(9, obj.createdTime)
                select.setString(10, obj.lastModified)

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

    override fun update(obj: Company): Boolean {
        val conn = DatabaseUtil.getConnection() ?: return false
        conn.use {
            try {
                val select = it.prepareStatement(SQL_UPDATE)
                select.setString(1, obj.name)
                select.setString(2, obj.taxNumber)
                select.setString(3, obj.registrationNumber)
                select.setBoolean(4, obj.taxpayer)
                select.setString(5, obj.address)
                select.setString(6, obj.postNumber)
                select.setString(7, obj.post)
                select.setString(8, obj.createdTime)
                select.setString(9, obj.lastModified)
                select.setString(10, obj.uuid)

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

    override fun delete(obj: Company): Boolean {
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

    override fun getByTaxNumber(taxNumber: String): Company?{
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_GET_TAX_NUM)
                select.setString(1, taxNumber)
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return try {
                    Company(rs)
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
    override fun getByRegistrationNumber(registrationNumber: String) : Company?{
        val conn = DatabaseUtil.getConnection() ?: return null
        conn.use {
            try {
                val select = it.prepareStatement(SQL_REG_TAX_NUM)
                select.setString(1, registrationNumber)
                val rs = select.executeQuery()
                if (!rs.next()) return null
                return try {
                    Company(rs)
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

    companion object {
        private const val TABLE_NAME = "company"
        private const val SQL_INSERT =
            "INSERT INTO $TABLE_NAME (UUID, name, taxNumber, registrationNumber, taxpayer, address, postNumb, post, createdTime, modifiedTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        private const val SQL_DELETE = "DELETE FROM $TABLE_NAME WHERE UUID = ? LIMIT 1"
        private const val SQL_GET_ALL = "SELECT * FROM $TABLE_NAME"
        private const val SQL_GET_BY_ID = "SELECT * FROM $TABLE_NAME WHERE UUID = ? LIMIT 1"
        private const val SQL_UPDATE =
            "UPDATE $TABLE_NAME  SET UUID = ?, name = ?, taxNumber = ?, registrationNumber = ?, taxpayer = ?, address = ?, post = ?, postNumb = ?, createdTime = ?, modifiedTime = ?, WHERE UUID = ?"
        private const val SQL_GET_TAX_NUM = "SELECT * FROM $TABLE_NAME WHERE taxNumber = ? LIMIT 1"
        private const val SQL_REG_TAX_NUM = "SELECT * FROM $TABLE_NAME WHERE registrationNumber = ? LIMIT 1"
    }

}