package team.yi.jacksync.support.dto

class Author {
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null

    constructor()
    constructor(firstName: String?, lastName: String?, email: String?) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (email == null) 0 else email.hashCode()
        result = prime * result + if (firstName == null) 0 else firstName.hashCode()
        result = prime * result + if (lastName == null) 0 else lastName.hashCode()

        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false

        val target = other as Author

        if (email == null) {
            if (target.email != null) return false
        } else if (email != target.email) {
            return false
        }

        if (firstName == null) {
            if (target.firstName != null) return false
        } else if (firstName != target.firstName) {
            return false
        }

        return if (lastName == null) target.lastName == null else lastName == target.lastName
    }

    override fun toString(): String {
        return "Author {firstName=$firstName, lastName=$lastName, email=$email}"
    }
}
