package groovycode.utils.logging

/**
 * Created by Saravana on 17/03/14.
 */
class Blog {

    private final String DEBUG = 'DEBUG'
    private final String ERROR = 'ERROR'
    private final String INFO = 'INFO'

    public static void d(message){
        println "DEBUG : $message"
    }

    public static void e(message){
        println "ERROR : $message"
    }

    public static void i(message){
        println "INFO : $message"
    }

}
