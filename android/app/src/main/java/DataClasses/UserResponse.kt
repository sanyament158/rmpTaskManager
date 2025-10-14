package DataClasses

data class UserResponse(
    val Id: Int? = null,
    val Username: String,
    val Fname: String? = null,
    val Lname: String? = null,
    val IdRole: Int
)