package main.java.com.ader.classes;

import com.ader.annotations.HtmlForm;
import main.java.com.ader.annotations.HtmlInput;

@HtmlForm(action = "/submitUser", method = "post")
public class UserForm {
    @HtmlInput(type = "text", name = "username", placeholder = "Enter Username")
    private String username;

    @HtmlInput(type = "password", name = "password", placeholder = "Enter Password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
