package javacode.multilangexcelparser.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.DialogWrapper;
import groovycode.global.constants.BnppConstants;
import groovycode.multilangexcelparser.corecode.CentralController;
import javacode.multilangexcelparser.ui.dialogs.EntryDialog;

/**
 * Action that performs multi lang integration
 *
 * @author Saravana
 */
public class MultiLangIntegrator extends AnAction {

    public static final String MY_OS_NAME = "Mac OS X";
    public static final String MY_OS_VERSION = "10.8.5";

    public void actionPerformed(AnActionEvent e) {
        final EntryDialog dialog = new EntryDialog(e.getProject());
        dialog.show();

        // Registers the Notification type. Not necessary. Just sets the preferred type of notifications
        Notifications.Bus.register(BnppConstants.GROUP_NOTIFICATION_ID, NotificationDisplayType.BALLOON);
        // Execute if OK button is pressed in the dialog
        if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            CentralController.SOURCE_FILE_LOCATION = dialog.getExcelSheetPath();
            CentralController.DESTINATION_FOLDER_LOCATION = dialog.getProjectPath();

            // To run in a separate thread rather than on the main thread
            ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
                @Override
                public void run() {
                    Notifications.Bus.notify(new Notification(BnppConstants.GROUP_NOTIFICATION_ID, "INFO", "Multi-Lang Integration Started", NotificationType.INFORMATION));
                    CentralController.runIntegration();
                    Notifications.Bus.notify(new Notification(BnppConstants.GROUP_NOTIFICATION_ID, "INFO", "Multi-Lang Integration Ended", NotificationType.INFORMATION));
                }
            });

        }
    }

}
