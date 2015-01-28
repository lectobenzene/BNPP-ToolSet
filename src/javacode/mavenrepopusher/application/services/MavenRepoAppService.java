package javacode.mavenrepopusher.application.services;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import groovycode.mavenrepopusher.models.ParametersModel;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Saravana on 20/03/14.
 */

@State(
        name = "MavenRepoAppService",
        storages = {
                @Storage(id = "other", file = StoragePathMacros.APP_CONFIG + "/others.xml")
        }
)
public class MavenRepoAppService implements PersistentStateComponent<ParametersModel> {

    public ParametersModel model;

    public MavenRepoAppService() {
        model = new ParametersModel();
    }

    @Nullable
    @Override
    public ParametersModel getState() {
        return model;
    }

    @Override
    public void loadState(ParametersModel state) {
        model = state;
    }

}
