package com.ader;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Set;
import com.google.auto.service.AutoService;
import javax.annotation.processing.Processor;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.ader.HtmlForm", "com.ader.HtmlInput"})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class HtmlFormProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        System.out.println("<<<<<<<<< Processor Initialized >>>>>>>>");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(HtmlForm.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                HtmlForm htmlForm = element.getAnnotation(HtmlForm.class);
                System.out.println("<<<<<<<<< Process Method to call generate HtmlFile >>>>>>>>");
                generateHtmlFile((TypeElement) element, htmlForm);
            }
        }
        return true;
    }

    private void generateHtmlFile(TypeElement element, HtmlForm htmlForm) {
        try {
            String fileName = htmlForm.fileName();
            Filer filer = processingEnv.getFiler();
            FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", fileName);
            
            // Print the file path for debugging
            System.out.println(">>>>>>>>>>Generating file at  : " + fileObject.toUri().getPath());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileObject.openOutputStream()));
            writer.write("<form action=\"" + htmlForm.action() + "\" method=\"" + htmlForm.method() + "\">\n");

            for (Element field : element.getEnclosedElements()) {
                if (field.getKind() == ElementKind.FIELD && field.getAnnotation(HtmlInput.class) != null) {
                    HtmlInput htmlInput = field.getAnnotation(HtmlInput.class);
                    writer.write("<input type=\"" + htmlInput.type() + "\" name=\"" + htmlInput.name() +
                            "\" placeholder=\"" + htmlInput.placeholder() + "\">\n");
                }
            }
            writer.write("<input type=\"submit\" value=\"Send\">\n");
            writer.write("</form>\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
