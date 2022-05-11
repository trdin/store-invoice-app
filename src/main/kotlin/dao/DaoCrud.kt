

package dao
import java.util .*
interface DaoCrud<T> {
    fun getById(id: String): T?
    fun getAll(): List<T>
    fun insert(obj: T): Boolean
    fun update(obj: T): Boolean
    fun delete(obj: T): Boolean
}