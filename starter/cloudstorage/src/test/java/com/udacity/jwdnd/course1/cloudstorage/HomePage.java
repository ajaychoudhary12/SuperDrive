package com.udacity.jwdnd.course1.cloudstorage;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    @FindBy(id = "logout-button")
    WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    WebElement notesTabButton;

    @FindBy(id = "add-note-button")
    WebElement addNoteButton;

    @FindBy(id = "note-title")
    WebElement noteTitleTextField;

    @FindBy(id = "note-description")
    WebElement noteDescriptionTextField;

    @FindBy(id = "note-submit-button")
    WebElement noteSubmitButton;

    @FindBy(id = "note-item-title")
    WebElement noteTitleFromTable;

    @FindBy(id = "edit-note-button")
    WebElement editNoteButton;

    @FindBy(id="delete-note-button")
    WebElement deleteNoteButton;

    WebDriver webDriver;

    public HomePage(WebDriver driver) {
        this.webDriver = driver;
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        logoutButton.click();
    }

    public void createNote(String title, String description) {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));

        navigateToNotesTab();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note-button")));

        addNoteButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));

        noteTitleTextField.sendKeys(title);
        noteDescriptionTextField.sendKeys(description);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-submit-button")));
        noteSubmitButton.click();
    }

    public void editNote(String title, String description) {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-note-button")));

        editNoteButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));

        noteTitleTextField.clear();
        noteTitleTextField.sendKeys(title);
        noteDescriptionTextField.sendKeys(description);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-submit-button")));
        noteSubmitButton.click();
    }

    public void deleteNote() {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-note-button")));

        deleteNoteButton.click();
    }

    public String getFirstNoteTitle() {
        return noteTitleFromTable.getText();
    }

    public void navigateToNotesTab() {
        notesTabButton.click();
    }
}
