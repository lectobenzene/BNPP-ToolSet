package groovycode.textfinder.corecode.poc

import com.intellij.platform.templates.ArchivedTemplatesFactory

/**
 * Created by Saravana on 14/03/14.
 */
class FileReaderPOC {

    public static readFile() {
        def string = FileReaderPOC.class.toString()
        println "string = $string"
        URL resource = FileReaderPOC.class.getResource("/assets/files/hello.txt");
        def uri = new URI(resource.toString())

        System.out.println("resource.toString() = " + resource.toString());
        def file = new File(uri)
        file.eachLine {println it}
    }

}
