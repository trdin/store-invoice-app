package db

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSetMetaData
import java.sql.SQLException

inline fun <reified T> Json.readFromFile(filename: String): T = decodeFromString(File(filename).readText())

inline fun <reified T> Json.saveToFile(filename: String, obj: T): Boolean {
    return try {
        File(filename).writeText(encodeToString(obj))
        true
    } catch (ex: IOException) {
        println(ex.message)
        false
    }

}


@Serializable
data class DbCredentials(val url: String, val username: String, val password: String)


object DatabaseUtil {
    @JvmStatic
    fun getConnection(): Connection? {
        val dbCredentials = Json.readFromFile<DbCredentials>("src/main/kotlin/db/config.json")
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor()
                .newInstance()
            return DriverManager.getConnection(dbCredentials.url, dbCredentials.username, dbCredentials.password)
        } catch (ex: SQLException) {
            println("${ex.javaClass.simpleName} ${ex.message}")
        } catch (ex: Exception) {
            println("${ex.javaClass.simpleName} ${ex.message}")
        }
        return null
    }

    @JvmStatic
    fun testConnection() {
        val query = "SELECT * FROM film;"
        try {
           //val dbCredentials = Json.readFromFile<DbCredentials>("src/main/kotlin/db/config.json")
            val conn = getConnection() ?: return
            conn.use {
                try {
                    val select = it.prepareStatement(query)
                    val rs = select.executeQuery()

                    val rsmd: ResultSetMetaData = rs.metaData
                    val columnsNumber = rsmd.columnCount
               

                    while (rs.next()) {
                        for (i in 1..columnsNumber) {
                            if (i > 1) print(",  ")
                            var columnValue = ""
                            if (rs.getString(i) == null) {
                                columnValue = "null"
                            } else {
                                columnValue = rs.getString(i)
                            }
                            print(columnValue + " " + rsmd.getColumnName(i))
                        }
                        println("")
                        //println(rs.getString("Database"))
//                        val attribute = rs.getInt("film_id")
//                        println("$attribute")
                    }
                } catch (ex: SQLException) {
                    println(ex.message)
                }
            }
        } catch (ex: FileNotFoundException) {
            println(ex.message)
        }
    }
}
