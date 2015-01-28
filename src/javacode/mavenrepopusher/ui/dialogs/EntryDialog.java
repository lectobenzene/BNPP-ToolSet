package javacode.mavenrepopusher.ui.dialogs;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import groovycode.mavenrepopusher.models.ParametersModel;
import groovycode.utils.logging.Blog;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.io.File;

/**
 * Created by Saravana on 20/03/14.
 */

public class EntryDialog extends DialogWrapper {
    private JTextField txtFieldGroupId;
    private JTextField txtFieldArtifactId;

    @Nullable
    @Override
    protected String getDimensionServiceKey() {
        return "#javacode.mavenrepopusher.ui.dialogs.EntryDialog";
    }

    private JTextField txtFieldVersion;
    private JTextField txtFieldPackaging;
    private JPanel contentPanel;

    private TextFieldWithBrowseButton txtFieldBrowseBtnArtifactFilePath;

    public EntryDialog(@Nullable Project project) {
        super(project);
        setTitle("Maven Repository Pusher");
        txtFieldBrowseBtnArtifactFilePath.addBrowseFolderListener("Select Artifact", "Select the artifact file that has to be pushed to maven repository", project, new FileChooserDescriptor(true, false, true, true, false, false));
        txtFieldBrowseBtnArtifactFilePath.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                final Document document = documentEvent.getDocument();
                try {
                    final String documentText = document.getText(0, document.getLength());
                    System.out.println("documentText = " + documentText);
                    int indexOfPackaging = documentText.lastIndexOf(".");
                    int indexOfVersion = documentText.lastIndexOf("-");
                    int indexOfArtifactId = documentText.lastIndexOf(File.separator);

                    txtFieldPackaging.setText(documentText.substring(indexOfPackaging + 1));
                    txtFieldVersion.setText(documentText.substring(indexOfVersion + 1, indexOfPackaging));
                    txtFieldArtifactId.setText(documentText.substring(indexOfArtifactId + 1, indexOfVersion));
                } catch (BadLocationException e) {
                    Blog.e("Error in txtFieldBrowseBtnArtifactFilePath document listener");
                    e.printStackTrace();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                Blog.i("Remove Update called");
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {

                Blog.i("Change Update called");
            }
        });
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPanel;
    }

    public void setData(ParametersModel data) {
        txtFieldGroupId.setText(data.getGroupId());
        txtFieldArtifactId.setText(data.getArtifactId());
        txtFieldVersion.setText(data.getVersion());
        txtFieldPackaging.setText(data.getPackaging());
    }

    public void getData(ParametersModel data) {
        data.setGroupId(txtFieldGroupId.getText());
        data.setArtifactId(txtFieldArtifactId.getText());
        data.setVersion(txtFieldVersion.getText());
        data.setPackaging(txtFieldPackaging.getText());
    }

    public TextFieldWithBrowseButton getTxtFieldBrowseBtnArtifactFilePath() {
        return txtFieldBrowseBtnArtifactFilePath;
    }
}
