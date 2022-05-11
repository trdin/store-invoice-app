import java.sql.ResultSet
import java.text.SimpleDateFormat
import java.util.*


/**
 * *class* Company that contains information about the company
 *
 * @constructor creates an instance of Company
 *
 * @param newName setting the name of this company
 * @param taxNumber tax number of the company
 * @param registrationNumber registration number of the company
 * @param taxpayer is company a taxpayer
 * @param newAddress setting the address
 * @param newPostNumb setting the post number
 * @param newPost setting the post
 *
 * @property name the name of this item
 * @property uuid the id of the item
 * @property createdTime time when item instance was created
 * @property lastModified time when item was last modified
 * @property taxNumber tax number of the company
 * @property registrationNumber registration number of the company
 * @property taxpayer is company a taxpayer
 *
 */
class Company(
    newName: String,
    val taxNumber: String,
    val registrationNumber: String,
    val taxpayer: Boolean,
    newAddress: String,
    newPostNumb: String,
    newPost: String
) {

    var name = newName
        set(value) {
            field = value
            modify()
        }
    var address = newAddress
        set(value) {
            field = value
            modify()
        }
    var postNumber = newPostNumb
        set(value) {
            field = value
            modify()
        }
    var post = newPost
        set(value) {
            field = value
            modify()
        }

    var uuid = UUID.randomUUID().toString().substring(0, 16)
    var createdTime = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date())
    var lastModified = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date())
        private set

    /**
     * sets the adress, post and postNumber
     */
    fun SetAdressPost(newAddress: String, newPostNumb: String, newPost: String) {
        address = newAddress
        postNumber = newPostNumb
        post = newPost
        modify()
    }

    /**
     * updates [lastModified] when called
     */
    private fun modify() {
        lastModified = SimpleDateFormat("yyyy-M-dd hh:mm:ss").format(Date())
    }

    /**
     * overrides toString method and writes out parameters of Company
     * @return Company in string
     */
    override fun toString(): String {
        var string = ""
        string += "$name\n"
        string += "$address\n"
        string += "$postNumber $post\n"
        string += "ID for DDV SI$taxNumber"

        return string
    }

    constructor(rs: ResultSet) : this(
        rs.getString("name"),
        rs.getString("taxNumber"),
        rs.getString("registrationNumber"),
        rs.getBoolean("taxpayer"),
        rs.getString("address"),
        rs.getString("postNumb"),
        rs.getString("post")
    ) {

        this.uuid = rs.getString("UUID")
        this.lastModified = rs.getString("modifiedTime")
        this.createdTime = rs.getString("createdTime")
    }

}