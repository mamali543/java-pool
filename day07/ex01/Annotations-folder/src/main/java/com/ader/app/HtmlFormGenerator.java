package main.java.com.ader.app;

import main.java.com.ader.classes.UserForm;
import com.ader.annotations.HtmlForm;
import main.java.com.ader.annotations.HtmlInput;
import java.lang.reflect.Field;

public class HtmlFormGenerator {

    public static void main(String[] args) {
        Class<?> clazz = UserForm.class;
        if (clazz.isAnnotationPresent(HtmlForm.class)) {
            HtmlForm formAnnotation = clazz.getAnnotation(HtmlForm.class);
            StringBuilder html = new StringBuilder();
            html.append("<form action='").append(formAnnotation.action())
                .append("' method='").append(formAnnotation.method()).append("'>\n");

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(HtmlInput.class)) {
                    HtmlInput inputAnnotation = field.getAnnotation(HtmlInput.class);
                    html.append("\t<input type='").append(inputAnnotation.type())
                        .append("' name='").append(inputAnnotation.name())
                        .append("' placeholder='").append(inputAnnotation.placeholder())
                        .append("'/>\n");
                }
            }

            html.append("\t<input type='submit' value='Submit'/>\n");
            html.append("</form>");

            System.out.println(html.toString());
        }
    }
}
