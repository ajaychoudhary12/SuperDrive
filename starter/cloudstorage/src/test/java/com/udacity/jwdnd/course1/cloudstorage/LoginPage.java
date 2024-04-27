package com.udacity.jwdnd.course1.cloudstorage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(css = "#inputUsername")
    WebElement userNameElement;

    @FindBy(css = "#inputPassword")
    WebElement passwordElement;

    @FindBy(css = "#login-button")
    WebElement loginButtonElement;

    public LoginPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void login(String username, String password) {
        this.userNameElement.sendKeys(username);
        this.passwordElement.sendKeys(password);
        this.loginButtonElement.click();
    }
}
