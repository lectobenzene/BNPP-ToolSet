package javacode.regexrename.ui.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by Saravana on 17/03/14.
 */
public class EntryDialog extends DialogWrapper {

    private JCheckBox fieldsCheckBox;
    private JCheckBox methodsCheckBox;

    private JCheckBox localVariablesCheckBox;

    private JTextField findTextField;
    private JTextField renameTextField;
    private JPanel contentPanel;
    public EntryDialog(@Nullable Project project) {
        super(project);
        setTitle("Regex Rename");
        init();
    }

    @Nullable
    @Override
    protected String getDimensionServiceKey() {
        return "#javacode.regexrename.ui.dialogs.EntryDialog";
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPanel;
    }

    public JCheckBox getFieldsCheckBox() {
        return fieldsCheckBox;
    }

    public JCheckBox getMethodsCheckBox() {
        return methodsCheckBox;
    }

    public JCheckBox getLocalVariablesCheckBox() {
        return localVariablesCheckBox;
    }

    public JTextField getFindTextField() {
        return findTextField;
    }

    public JTextField getRenameTextField() {
        return renameTextField;
    }
}
