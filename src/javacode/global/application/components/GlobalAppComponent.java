package javacode.global.application.components;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ApplicationComponent;
import groovycode.global.constants.BnppConstants;
import groovycode.utils.logging.Blog;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Saravana on 23/3/14.
 */
public class GlobalAppComponent implements ApplicationComponent {
    public GlobalAppComponent() {
    }

    public void initComponent() {
        // Notifications for the entire plugin is registered here
        Blog.i("Global Notification Registered");
        Notifications.Bus.register(BnppConstants.GROUP_NOTIFICATION_ID, NotificationDisplayType.BALLOON);
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "GlobalAppComponent";
    }
}
