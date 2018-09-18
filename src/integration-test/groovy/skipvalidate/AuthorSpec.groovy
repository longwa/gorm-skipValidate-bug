package skipvalidate

import grails.buildtestdata.TestDataBuilder
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.validation.ValidationException
import spock.lang.Specification

@Integration
@Rollback
class AuthorSpec extends Specification implements TestDataBuilder {
    void "calling save with flush and invalid attribute"() {
        when:
        new Author(name: 'ThisNameIsTooLong').save(failOnError: true, flush: true)

        then:
        thrown(ValidationException)
    }

    void "calling validate with property list after save should validate again"() {
        // Save but don't flush, this causes the new author to have skipValidate = true
        Author author = new Author(name: 'Aaron').save(failOnError: true)

        when: "validate is called again with a property list"
        author.name = "ThisNameIsTooLong"
        def isValid = author.validate(['name'])

        then: "it should be invalid but it skips validation instead"
        !isValid
    }

    void "calling validate with property list after save with flush should validate again"() {
        // Save but don't flush, this causes the new author to have skipValidate = true
        Author author = new Author(name: 'Aaron').save(failOnError: true, flush: true)

        when: "validate is called again with a property list"
        author.name = "ThisNameIsTooLong"
        def isValid = author.validate(['name'])

        then: "it should be invalid but it skips validation instead"
        !isValid
    }

    void "calling validate with property list after save should validate again on explicit flush"() {
        // Save but don't flush, this causes the new author to have skipValidate = true
        Author author = new Author(name: 'Aaron').save(failOnError: true)

        when: "validate is called again with a property list"
        author.name = "ThisNameIsTooLong"
        Author.withSession { session ->
            session.flush()
        }

        then:
        author.hasErrors()
    }

    // This works
    void "calling validate with no list after save should validate again"() {
        // Save but don't flush, this causes the new author to have skipValidate = true
        Author author = new Author(name: 'Aaron').save(failOnError: true)

        when: "validate is called again without any parameters"
        author.name = "ThisNameIsTooLong"
        def isValid = author.validate()

        then: "this works since validate without params doesn't honor skipValidate for some reason"
        !isValid
    }
}
