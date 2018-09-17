package skipvalidate

class Author {
    String name

    static constraints = {
        name(nullable: false, maxSize: 8)
    }
}
