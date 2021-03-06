package ru.jihor.mapper.tests.simpleConverter

import groovy.util.logging.Slf4j
import ru.jihor.mapper.exceptions.TransformationException
import ru.jihor.mapper.tests.simpleConverter.converters.SimpleConverter
import ru.jihor.mapper.tests.simpleConverter.dictionaries.SimpleDictionary
import ru.jihor.mapper.tests.simpleConverter.entities.BusinessSection
import ru.jihor.mapper.tests.simpleConverter.entities.SampleSource
import ru.jihor.mapper.tests.simpleConverter.entities.TechSection
import spock.lang.Shared
import spock.lang.Specification
/**
 *
 *
 * @author Dmitry Zhikharev (jihor@ya.ru)
 * Created on 2016-07-01
 */
@Slf4j
class ConverterTest extends Specification {

    @Shared SimpleDictionary dictionary = new SimpleDictionary()
    @Shared SimpleConverter converter = new SimpleConverter()

    def "Test valid mapping (source errorCode == 0)"() {

        setup: "Source is valid and has errorCode == 0"
        def data = "nice"
        SampleSource src = new SampleSource(techSection: new TechSection(errorCode: 0, description: ""), businessSection: new BusinessSection(data: data))
        def target

        when: "Map to targetClass"
        target = converter.convert(src)

        then: "No exceptions, valid data in targetClass"
        target.data.value == dictionary.map(data)
        !target.error
    }

    def "Test valid mapping (source errorCode != 0)"() {

        setup: "Source is valid and has errorCode != 0"
        def code = 5
        def description = "some nice description"
        SampleSource src = new SampleSource(techSection: new TechSection(errorCode: code, description: description))
        def target

        when: "Map to targetClass"
        target = converter.convert(src)

        then: "No exceptions, error data in targetClass"
        target.error.message == "Error code [$code], description [$description]"
        !target.data
    }

    def "Test valid mapping (source is invalid)"() {

        setup: "Source is valid and has errorCode != 0"
        SampleSource src = new SampleSource(techSection: new TechSection(errorCode: 0, description: ""))
        def target

        when: "Map to targetClass"
        target = converter.convert(src)

        then: "No exceptions, error data in targetClass"
        target.error.message == "Source contains no error code but business data is empty"
        !target.data
    }

    def "Test mapping fault"() {

        setup: "Source is valid and has errorCode == 0"
        def data = "some nonexistent value"
        SampleSource src = new SampleSource(techSection: new TechSection(errorCode: 0, description: ""), businessSection: new BusinessSection(data: data))
        def target

        when: "Map to targetClass"
        target = converter.convert(src)

        then: "No exceptions, valid data in targetClass"
        def exception = thrown(TransformationException)
        exception.message == "Step [Map data] failed with IllegalArgumentException: No mapping defined for [$data]"
        exception.step == "[Check errorCode] -> [Check if business data exists] -> [Map data]"
    }
}
