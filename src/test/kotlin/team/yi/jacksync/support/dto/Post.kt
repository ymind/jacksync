package team.yi.jacksync.support.dto

import java.time.LocalDateTime
import java.util.*

class Post {
    var id: String? = null
    var version: Long? = null
    var title: String? = null
    var publishedTime: LocalDateTime? = null
    var sections: List<Section>? = null
    var categories: List<String>? = null
    var tags: List<String>? = null
    var author: Author? = null

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (author == null) 0 else author.hashCode()
        result = prime * result + if (categories == null) 0 else categories.hashCode()
        result = prime * result + if (id == null) 0 else id.hashCode()
        result = prime * result + if (publishedTime == null) 0 else publishedTime.hashCode()
        result = prime * result + if (sections == null) 0 else sections.hashCode()
        result = prime * result + if (tags == null) 0 else tags.hashCode()
        result = prime * result + if (title == null) 0 else title.hashCode()
        result = prime * result + if (version == null) 0 else version.hashCode()

        return result
    }

    @Suppress("ComplexMethod")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false

        val target = other as Post

        if (author == null) {
            if (target.author != null) return false
        } else if (author != target.author) {
            return false
        }

        if (categories == null) {
            if (target.categories != null) return false
        } else if (categories != target.categories) {
            return false
        }

        if (id == null) {
            if (target.id != null) return false
        } else if (id != target.id) {
            return false
        }

        if (publishedTime == null) {
            if (target.publishedTime != null) return false
        } else if (publishedTime != target.publishedTime) {
            return false
        }

        if (sections == null) {
            if (target.sections != null) return false
        } else if (sections != target.sections) {
            return false
        }

        if (tags == null) {
            if (target.tags != null) return false
        } else if (tags != target.tags) {
            return false
        }

        if (title == null) {
            if (target.title != null) return false
        } else if (title != target.title) {
            return false
        }

        return if (version == null) target.version == null else version == target.version
    }

    override fun toString(): String {
        return String.format(
            Locale.getDefault(),
            "Post {id=%s, version=%d, title=%s, publishedTime=%s, sections=%s, categories=%s, tags=%s, author=%s}",
            id,
            version,
            title,
            publishedTime,
            sections,
            categories,
            tags,
            author,
        )
    }
}
