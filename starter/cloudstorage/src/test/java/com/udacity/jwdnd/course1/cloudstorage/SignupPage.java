package com.udacity.jwdnd.course1.cloudstorage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(css = "#inputFirstName")
    WebElement firstnameElement;

    @FindBy(css = "#inputLastName")
    WebElement lastnameElement;

    @FindBy(css = "#inputUsername")
    WebElement usernameElement;

    @FindBy(css = "#inputPassword")
    WebElement passwordElement;

    @FindBy(css = "#buttonSignUp")
    WebElement submitButtonElement;

    public SignupPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void signup(String firstName, String lastName, String username, String password) {
        this.firstnameElement.sendKeys(firstName);
        this.lastnameElement.sendKeys(lastName);
        this.usernameElement.sendKeys(username);
        this.passwordElement.sendKeys(password);
        this.submitButtonElement.click();
    }
}
