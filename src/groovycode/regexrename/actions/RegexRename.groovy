package groovycode.regexrename.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.*
import com.intellij.psi.util.PsiUtil
import com.intellij.refactoring.RefactoringFactory
import com.intellij.refactoring.RenameRefactoring
import groovycode.utils.logging.Blog
import groovycode.utils.reusable.CommonMethods
import javacode.regexrename.ui.dialogs.EntryDialog

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Saravana on 17/03/14.
 */
public class RegexRename extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        println 'Executing...'

        final Editor editor = e.getData(DataKeys.EDITOR)
        final PsiFile psiFile = e.getData(DataKeys.PSI_FILE)


        EntryDialog entryDialog = new EntryDialog(e.getProject())
        entryDialog.show()

        if (entryDialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            Blog.d('OK clicked')

            final PsiClass psiClass = obtainPsiClass(editor, psiFile)

            // TODO: Replace this with DataBinding
            def isFields = entryDialog.getFieldsCheckBox().isSelected()
            def isMethods = entryDialog.getMethodsCheckBox().isSelected()

            def findRegex = entryDialog.getFindTextField().getText()
            def renameRegex = entryDialog.getRenameTextField().getText()

            if (psiClass) {
                if (isFields) {
                    Blog.d('Fields processing...')
                    PsiField[] fields = psiClass.getFields()
                    renameElements(e.getProject(), fields, findRegex, renameRegex)
                }

                if (isMethods) {
                    Blog.d('Methods processing...')
                    PsiMethod[] methods = psiClass.getMethods()
                    renameElements(e.getProject(), methods, findRegex, renameRegex)
                }
            }
        }
    }

    private void renameElements(Project project, psiElements, String findRegex, String renameString) {
        final Pattern pattern = Pattern.compile(findRegex)

        final RefactoringFactory factory = RefactoringFactory.getInstance(project)

        psiElements.each { psiElement ->
            final String elementName = psiElement.getName()
            final Matcher matcher = pattern.matcher(elementName)

            if (matcher.matches()) {
                Blog.d 'Refactor Renaming...'
                final String stringToReplace = CommonMethods.regexCaptureParser(renameString, matcher[0])
                final RenameRefactoring rename = factory.createRename(psiElement, stringToReplace)

                // TODO: Do not rename String contents, refactor only code occurrences.
                rename.doRefactoring(rename.findUsages())
            } else {
                Blog.d "Rename not matching | elementName=$elementName | findRegex=$findRegex"
            }
        }
    }

    private PsiClass obtainPsiClass(Editor editor, PsiFile psiFile) {
        if (editor && psiFile) {
            final int caretOffset = editor.getCaretModel().getOffset()
            final PsiElement psiElement = psiFile.findElementAt(caretOffset)
            if (psiElement) {
                return PsiUtil.getTopLevelClass(psiElement)
            }
        }
        return null
    }

}
