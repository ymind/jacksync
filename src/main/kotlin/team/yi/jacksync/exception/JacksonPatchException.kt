package team.yi.jacksync.exception

open class JacksonPatchException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)

    companion object {
        private const val serialVersionUID = 1L
    }
}
