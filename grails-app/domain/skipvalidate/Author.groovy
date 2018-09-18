package skipvalidate

class Author {
    String name

    static constraints = {
        name(nullable: false, maxSize: 8, validator: { val, obj ->
            println "Validate called"
            true
        })
    }
}
