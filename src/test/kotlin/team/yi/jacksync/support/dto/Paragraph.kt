package team.yi.jacksync.support.dto

class Paragraph {
    var title: String? = null
    var content: String? = null

    constructor() : super()
    constructor(title: String?, content: String?) : super() {
        this.title = title
        this.content = content
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (content == null) 0 else content.hashCode()
        result = prime * result + if (title == null) 0 else title.hashCode()

        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false

        val target = other as Paragraph

        if (content == null) {
            if (target.content != null) return false
        } else if (content != target.content) {
            return false
        }

        return title == target.title
    }

    override fun toString(): String {
        return "Paragraph {title=$title, content=$content}"
    }
}
