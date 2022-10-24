package team.yi.jacksync.support.dto

class Section {
    var title: String? = null
    var paragraphs: List<Paragraph>? = null
    var privateNote: String? = null

    constructor() : super()
    constructor(title: String?) : super() {
        this.title = title
    }

    constructor(title: String?, paragraphs: List<Paragraph>?) : super() {
        this.title = title
        this.paragraphs = paragraphs
    }

    constructor(title: String?, paragraphs: List<Paragraph>?, privateNote: String?) : super() {
        this.title = title
        this.paragraphs = paragraphs
        this.privateNote = privateNote
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (paragraphs == null) 0 else paragraphs.hashCode()
        result = prime * result + if (privateNote == null) 0 else privateNote.hashCode()
        result = prime * result + if (title == null) 0 else title.hashCode()

        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false

        val target = other as Section

        if (paragraphs == null) {
            if (target.paragraphs != null) return false
        } else if (paragraphs != target.paragraphs) {
            return false
        }

        if (privateNote == null) {
            if (target.privateNote != null) return false
        } else if (privateNote != target.privateNote) {
            return false
        }

        return if (title == null) target.title == null else title == target.title
    }

    override fun toString(): String {
        return "Section {title=$title, paragraphs=$paragraphs, privateNote=$privateNote}"
    }
}
