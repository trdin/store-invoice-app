import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

import db.*
import kotlinx.serialization.json.Json


fun main(args: Array<String>) {
    //var ss = "jdbc:" + "mysql" + "://" + "127.0.0.1" + ":" + "3306" + "/" + "sakila"

 //Json.saveToFile("src/main/kotlin/db/config.json",  DbCredentials(ss, username, pasword) )
    val dbCredentials = Json.readFromFile<DbCredentials>("src/main/kotlin/db/config.json")
    //println(dbCredentials.toString())
    DatabaseUtil.testConnection()
}



