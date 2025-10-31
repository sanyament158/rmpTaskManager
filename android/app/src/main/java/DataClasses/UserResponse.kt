package DataClasses

open class UserResponse(
    open val Username: String,
    open val IdRole: Int,
    open val Id: Int? = null,
    open val Fname: String? = null,
    open val Lname: String? = null,
    open val IsAuth: Boolean = false
)

// must not be empty: Username, IdRole