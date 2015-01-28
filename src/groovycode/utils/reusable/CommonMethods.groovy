package groovycode.utils.reusable

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import groovycode.global.constants.BnppConstants
import groovycode.utils.logging.Blog

import java.util.regex.Matcher

/**
 * Class that contains common methods that is reused throughout the project.<br />
 * These methods are reusable components, that can be later used in some other projects.<br />
 *
 * @author Saravana / 587418
 */
class CommonMethods {

    /** To identify if the command line command is executed properly */
    public static final int PROPERLY_EXECUTED_CODE = -1

    public static final int COMMAND_NOT_EXECUTED_CODE = -100

    public static final int CLOSURE_DO_NOT_BREAK_CODE = -300

    /**
     * Takes a String of format "...$1...$2...$3" and replaces the "$(\d)" in it with contents of a list where "(\d)" is the position in the list.
     *
     * @param content The String that if of format "...($(\d)+)*..."
     * @param groups The list whose items should be placed in the content string
     * @return The replaced string
     */
    public static String regexCaptureParser(String content, groups) {
        final int MAX_SIZE = groups.size()
        boolean isError = false
        final def regex = /\$(\d+)/
        final Matcher matcher = content =~ regex
        matcher.each {match ->
            if ((Integer.parseInt((String) match[1]) > MAX_SIZE)) {
                Blog.e "Incorrect Rename Regex | match[1]=${(int) match[1]} | MAX_SIZE=${MAX_SIZE}"
                isError = true
            }
        }
        if (isError) {
            return null
        }

        matcher.reset()

        StringBuffer buffer = new StringBuffer()
        String toReplace
        while (matcher.find()) {
            int index = (Integer.parseInt((String) matcher.group(1)))
            toReplace = groups.get(index)
            matcher.appendReplacement(buffer, toReplace)
        }
        matcher.appendTail(buffer)

        return buffer.toString()
    }

    public static executeCommandLine(String command, map, Closure userFunction) {
        /** This shows how many times the command has been executed. For the first time, mvn in the PATH is used, if it is not
         * present, then exception is thrown and mvnExecutableFilePath is used, if present. If that too fails, then the Maven file
         * chooser dialog is shown. This variable is used to show the maven file chooser only on the second time */
        int noOfTimesCommandExecuted = 0;
        // Loop until command is properly executed

        while (noOfTimesCommandExecuted != -1) {
            try {
                final process = Runtime.getRuntime().exec(command)
                // To get the output from console. Also, if this block is removed, the process never stops.
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                def line
                while ((line = reader.readLine()) != null) {
                    //TODO: Show this as log to user, if needed
                    println line
                }

                // TODO: Didn't try waitFor() scenario for MAC. It should return 0 only when artifact is pushed into repository
                // If exit code is 0, then mvn has been executed properly
                def exitCode = process.waitFor()
                if (exitCode) {
                    map['exitCode'] = COMMAND_NOT_EXECUTED_CODE
                } else {
                    noOfTimesCommandExecuted = -1
                    map['exitCode'] = PROPERLY_EXECUTED_CODE
                }
                reader.close()
            } catch (IOException e) {

                // Initially exitCode is set, if the closure modifies this code, then the loop is exited.
                map['exitCode'] = CLOSURE_DO_NOT_BREAK_CODE
                def varMap = userFunction(noOfTimesCommandExecuted, map)
                if (varMap['exitCode'] != CLOSURE_DO_NOT_BREAK_CODE) {
                    break
                }
                command = varMap['command']
                noOfTimesCommandExecuted++
            }
        }
        return map
    }

    public static void notifyUser(String title, String description, NotificationType type) {
        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                Notifications.Bus.notify(new Notification(BnppConstants.GROUP_NOTIFICATION_ID, title, description, type))
            }
        });
    }
}