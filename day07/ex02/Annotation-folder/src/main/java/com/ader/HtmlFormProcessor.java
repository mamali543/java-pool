package com.ader;

import com.ader.UserForm;
import com.ader.HtmlForm;
import com.ader.HtmlInput;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import java.io.Writer;
import java.util.Set;
import java.lang.reflect.Field;

@SupportedAnnotationTypes({"com.ader.HtmlForm", "com.ader.HtmlInput"}) //This annotation tells the compiler that this processor is interested in classes annotated with @HtmlForm.
@SupportedSourceVersion(SourceVersion.RELEASE_14) //This specifies the latest version of the Java programming language that this processor supports
public class HtmlFormProcessor extends AbstractProcessor {

    //Overriding AbstractProcessor Methods
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        System.out.println("Processor initialized"); // Confirm initialization

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("Processing started"); // Confirm method entry
        for (Element elem : roundEnv.getElementsAnnotatedWith(HtmlForm.class)) {
            HtmlForm form = elem.getAnnotation(HtmlForm.class);
            if (elem.getKind() == ElementKind.CLASS) {
                try {
                    String fileName = form.fileName(); //The name of the file to be generated, as specified in the annotation.
                    //Creates a new file resource in the specified location target/classes.
                    FileObject file = processingEnv.getFiler().createResource(
                        javax.tools.StandardLocation.CLASS_OUTPUT, 
                        "", 
                        fileName,
                        elem
                    );
                    // JavaFileObject file = processingEnv.getFiler().createResource(javax.tools.StandardLocation.CLASS_OUTPUT, "", fileName);
                    //Opens a writer to write into the file.
                    try (Writer writer = file.openWriter()) {
                        System.out.println("file opened");
                        writer.write("<form action='" + form.action() + "' method='" + form.method() + "'>\n");
                        //Iterates through all fields (enclosed elements) of the class.
                        for (Element enclosed : elem.getEnclosedElements()) {
                            //Checks if they are annotated with @HtmlInput
                            if (enclosed.getKind() == ElementKind.FIELD && enclosed.getAnnotation(HtmlInput.class) != null) {
                                HtmlInput input = enclosed.getAnnotation(HtmlInput.class);
                                writer.write("<input type='" + input.type() + "' name='" + input.name() + 
                                             "' placeholder='" + input.placeholder() + "' />\n");
                            }
                        }
                        writer.write("<input type='submit' value='Send' />\n");
                        writer.write("</form>");
                        System.out.println("file closed");
                    }
                } catch (Exception e) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Error generating file: " + e.getMessage());
                }
            }
        }
        return true;
    }
}
