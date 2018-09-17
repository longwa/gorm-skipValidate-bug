package skipvalidate

import grails.buildtestdata.TestDataBuilder
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
@Rollback
class AuthorSpec extends Specification implements TestDataBuilder {
    void "calling validate with property list after save should validate again"() {
        // Save but don't flush, this causes the new author to have skipValidate = true
        Author author = new Author(name: 'Aaron').save(failOnError: true)

        when: "validate is called again with a property list"
        author.name = "ThisNameIsTooLong"
        def isValid = author.validate(['name'])

        then: "it should be invalid but it skips validation instead"
        !isValid
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
