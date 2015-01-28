package javacode.textfinder.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.templates.ArchivedTemplatesFactory;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.impl.FileManagerImpl;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;
import groovycode.textfinder.corecode.poc.FileReaderPOC;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

/**
 * Created by Saravana on 12/03/14.
 */
public class TextFinder extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {




        PsiElement psiElement = e.getData(PlatformDataKeys.PSI_ELEMENT);
        String psiElementText;
        if (psiElement != null) {
            psiElementText = psiElement.getText();
            PsiReference psiElementReference = psiElement.getReference();
            Query<PsiReference> search = ReferencesSearch.search(psiElement);
            Collection<PsiReference> psiReferences = search.findAll();

            for (PsiReference reference : psiReferences){
                String text = reference.getElement().getText();

                System.out.println("reference.getCanonicalText() = " + reference.getCanonicalText());
                System.out.println("text = " + text);
                int text1 = reference.resolve().getTextOffset();
                System.out.println("Index = " + text1);
            }
        }

        VirtualFile virtualFile = e.getData(DataKeys.VIRTUAL_FILE);

        PsiFile psiFile = e.getData(DataKeys.PSI_FILE);
        System.out.println("psiFile.getName() = " + psiFile.getName());
        System.out.println("psiFile.getVirtualFile().getCanonicalPath() = " + psiFile.getVirtualFile().getCanonicalPath());

        File templateFile = ArchivedTemplatesFactory.getTemplateFile("icons/hello.txt");
        String name = templateFile.getName();
        System.out.println("name = " + name);

        ArchivedTemplatesFactory factory = new ArchivedTemplatesFactory();
        URL resource = getClass().getResource("/assets/files/hello.txt");
        System.out.println("resource.toString() = " + resource.toString());


        FileReaderPOC.readFile();

    }
}
