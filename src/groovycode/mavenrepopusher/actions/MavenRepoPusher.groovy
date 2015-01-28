package groovycode.mavenrepopusher.actions

import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import groovycode.utils.logging.Blog
import groovycode.utils.reusable.CommonMethods
import javacode.mavenrepopusher.application.services.MavenRepoAppService
import javacode.mavenrepopusher.ui.dialogs.EntryDialog

/**
 * Created by Saravana on 20/03/14.
 */
class MavenRepoPusher extends AnAction {

    public static final int CANCEL_PRESSED_CODE = -200

    /** Path that points to Maven executable file, saved and restored */
    private mvnExecutableFilePath

    @Override
    void actionPerformed(AnActionEvent e) {
        // Restore maven executable file path
        mvnExecutableFilePath = PropertiesComponent.getInstance().getValue('mvnExecutableFilePath')
        // Get ServiceManager to save the fields in the dialog and to restore them
        final def serviceManager = ServiceManager.getService(MavenRepoAppService.class)
        // Get the model with previously stored values
        final def model = serviceManager.getState()


        final EntryDialog dialog = new EntryDialog(e.project)
        // Pre fill dialog fields with values from model
        dialog.setData(model)
        dialog.show()


        if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            // Fills model with data from the dialog fields
            dialog.getData(model)


            final String pusherCommand = "install:install-file -Dfile=${dialog.getTxtFieldBrowseBtnArtifactFilePath().getText()} -DgroupId=${model.groupId} -DartifactId=${model.artifactId} -Dversion=${model.version} -Dpackaging=${model.packaging}"
            def command = "mvn ${pusherCommand}"

            final mvnFileChooseDescriptor = new FileChooserDescriptor(true, false, false, false, false, false)
            mvnFileChooseDescriptor.setTitle('Choose Maven')
            // In effect for the first time
            mvnFileChooseDescriptor.setDescription('Maven is not present in PATH. Select the Maven executable file')

            def map = [:]

            println 'running'
            map['mvnExecutableFilePath'] = mvnExecutableFilePath
            map['project'] = e.getProject()
            map['pusherCommand'] = pusherCommand
            map = CommonMethods.executeCommandLine(command, map) {noOfTimesCommandExecuted, mvnMap ->
                if (!mvnMap['mvnExecutableFilePath'] || noOfTimesCommandExecuted) {
                    final mvnExecutableFile = FileChooser.chooseFile(mvnFileChooseDescriptor, (Project) mvnMap['project'], null)

                    // Break when Cancel is clicked
                    if (!mvnExecutableFile) {
                        mvnMap['exitCode'] = CANCEL_PRESSED_CODE
                        return mvnMap
                    }

                    // From second time on, changed the description
                    mvnFileChooseDescriptor.setDescription('Not Proper Maven executable. Select the Maven executable file')
                    mvnMap['mvnExecutableFilePath'] = mvnExecutableFile.canonicalPath
                }
                mvnMap['command'] = "${mvnMap['mvnExecutableFilePath']} ${mvnMap['pusherCommand']}"
                return mvnMap
            }

            switch (map['exitCode']) {
                case CommonMethods.PROPERLY_EXECUTED_CODE:
                    Blog.i('Artifact Pushed successfully')
                    PropertiesComponent.getInstance().setValue('mvnExecutableFilePath', (String) map['mvnExecutableFilePath'])
                    CommonMethods.notifyUser('Maven Success', 'Artifact successfully pushed', NotificationType.INFORMATION)
                    break
                case CommonMethods.COMMAND_NOT_EXECUTED_CODE:
                    Blog.i('Artifact Not Pushed')
                    CommonMethods.notifyUser('Maven Error', 'Artifact Not pushed into repository', NotificationType.ERROR)
                    break
                case CANCEL_PRESSED_CODE:
                    Blog.i('User Cancelled Selection of Maven executable')
                    break
                default:
                    Blog.e("Wrong exit code returned : ${map['exitCode']}")
                    break
            }

            // The model is stored
            serviceManager.loadState(model)
        }
    }
}
