package javacode.multilangexcelparser.ui.dialogs;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

public class EntryDialog extends DialogWrapper {
    private JPanel contentPane;
    private TextFieldWithBrowseButton txtFieldBrowseBtnExcelSheet;
    private TextFieldWithBrowseButton txtFieldBrowseBtnProjectPath;

    public EntryDialog(@Nullable Project project) {
        super(project);
        setTitle("Integrate Multi Lang");
        txtFieldBrowseBtnExcelSheet.addBrowseFolderListener("Select Excel Path", "Choose the Excel sheet for parsing multiLang", project, new FileChooserDescriptor(true, false, false, false, false, false));
        txtFieldBrowseBtnProjectPath.addBrowseFolderListener("Select Project Path", "Choose the Project to Integrate multiLang", project, new FileChooserDescriptor(false, true, false, false, false, false));

        txtFieldBrowseBtnProjectPath.setEditable(true);
        txtFieldBrowseBtnExcelSheet.setEditable(false);

        txtFieldBrowseBtnExcelSheet.setText("/Users/Saravana/Documents/BNPP/Documentation/Multi-Lang/MR1-2014");
        txtFieldBrowseBtnProjectPath.setText("/Users/Saravana/Documents/Workspace/MR1_MainBranch_FOR_BUILD");

        init();
    }


    @Nullable
    @Override
    public JComponent createCenterPanel() {
        return contentPane;
    }


    public String getExcelSheetPath() {
        return txtFieldBrowseBtnExcelSheet.getText();
    }


    public String getProjectPath() {
        return txtFieldBrowseBtnProjectPath.getText();
    }

    @Nullable
    @Override
    public ValidationInfo doValidate() {

        if (!(txtFieldBrowseBtnExcelSheet.getText().endsWith(".xls") || txtFieldBrowseBtnExcelSheet.getText().endsWith(".xlsx"))) {
            System.out.println("Not an Excel Document");
            return new ValidationInfo("Not an Excel Document", txtFieldBrowseBtnExcelSheet);
        }

        File file = new File(txtFieldBrowseBtnProjectPath.getText());
        if (!file.exists()) {
            return new ValidationInfo("Project Path does not Exist", txtFieldBrowseBtnProjectPath);
        } else if (!file.isDirectory()) {
            return new ValidationInfo("Project Path is not a Directory", txtFieldBrowseBtnProjectPath);
        } else {
            File androidManifestFile = new File(txtFieldBrowseBtnProjectPath.getText() + File.separator + "AndroidManifest.xml");
            if (!androidManifestFile.exists()) {
                return new ValidationInfo("Project Path is not Android Project", txtFieldBrowseBtnProjectPath);
            }
        }

        return null;
    }

    @Nullable
    @Override
    protected String getDimensionServiceKey() {
        //This is used to Remember the dialogs size and location. The returned string can be anything.
        return "javacode.multilangexcelparser.ui.dialogs.EntryDialog";
    }
}
