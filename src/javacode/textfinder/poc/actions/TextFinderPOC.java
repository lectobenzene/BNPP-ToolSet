package javacode.textfinder.poc.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;

import java.util.Collection;

/**
 * Created by Saravana on 13/03/14.
 */
public class TextFinderPOC extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor data = e.getData(DataKeys.EDITOR);
        CaretModel caretModel = data.getCaretModel();
        int offset = caretModel.getOffset();

        PsiFile psiFile = e.getData(DataKeys.PSI_FILE);
        PsiElement elementAt = psiFile.findElementAt(offset);

        System.out.println("elementAt.getText() = " + elementAt.getText());

        PsiReference reference = elementAt.getReference();
        System.out.println("reference == null = " + (reference == null));

        Query<PsiReference> search = ReferencesSearch.search(elementAt);
        Collection<PsiReference> all = search.findAll();
        System.out.println("all.size() = " + all.size());



        PsiElement psiElement = e.getData(DataKeys.PSI_ELEMENT);
        String psiElementText;
        if (psiElement != null) {
            psiElementText = psiElement.getText();
            PsiReference psiElementReference = psiElement.getReference();
            System.out.println("psiElementText = " + psiElementText);
            System.out.println("psiElementReference == null = " + (psiElementReference == null));
            Query<PsiReference> searches = ReferencesSearch.search(psiElement);
            Collection<PsiReference> alles = searches.findAll();
            System.out.println("alles.size() = " + alles.size());
        }


        PsiMethodCallExpression psiMethodCallExpression = PsiTreeUtil.getParentOfType(psiFile.findElementAt(offset), PsiMethodCallExpression.class);
        String parentOfTypeText = null;
        if (psiMethodCallExpression != null) {
            parentOfTypeText = psiMethodCallExpression.getText();
        }
        System.out.println("parentOfTypeText = " + parentOfTypeText);

        PsiVariable psiVariable = PsiTreeUtil.getParentOfType(psiFile.findElementAt(offset), PsiVariable.class);
        if (psiVariable != null) {
            System.out.println("psiVariable.getName() = " + psiVariable.getName());
            System.out.println("psiVariable.getText() = " + psiVariable.getText());
        }
    }
}
