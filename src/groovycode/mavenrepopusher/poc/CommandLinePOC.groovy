package groovycode.mavenrepopusher.poc

/**
 * Created by Saravana on 23/3/14.
 */
class CommandLinePOC {
    public static void main(String[] args) {
        def output = new StringBuffer()
        String command = "automator -D title='MultiLang Integration' -D subtitle='Integrated Successfully' -D message='Excel File = MR1-2014' ~/Library/Workflows/'DisplayNotification.workflow'"
        Process process = Runtime.getRuntime().exec(command)
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line = "";
        while ((line = reader.readLine())!= null) {

            output.append(line + "\n");
        }
        process.waitFor()
        reader.close()
        println output
    }
}
