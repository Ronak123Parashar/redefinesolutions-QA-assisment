package com.example.qa.testcases;

import com.example.qa.components.Components_Apple;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class TestCase3FormValidation {

    public static void main(String[] args) {
        WebDriver driver = Components_Apple.initDriver();
        boolean testPassed = true;

        try {
            driver.get("https://appleid.apple.com/account");
            Thread.sleep(5000);

            takeScreenshot(driver, "screenshots/test_case_3/page_loaded.png");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            //Blank form submission
            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[form='create']")));
            scrollToViewAndClick(driver, continueBtn);
            Thread.sleep(2000);
            takeScreenshot(driver, "screenshots/test_case_3/required_fields_error.png");
            System.out.println("Required fields error");

            // Invalid email
            driver.findElement(By.name("email")).sendKeys("invalidemail");
            scrollToViewAndClick(driver, continueBtn);
            Thread.sleep(2000);
            takeScreenshot(driver, "screenshots/test_case_3/invalid_email.png");
            System.out.println("Invalid email format error");

            // Weak password
            driver.findElement(By.name("email")).clear();
            driver.findElement(By.name("email")).sendKeys("test@example.com");
            driver.findElement(By.name("password")).sendKeys("123");
            driver.findElement(By.name("confirmPassword")).sendKeys("123");
            scrollToViewAndClick(driver, continueBtn);
            Thread.sleep(2000);
            takeScreenshot(driver, "screenshots/test_case_3/weak_password.png");
            System.out.println("Weak password error");

            // Password mismatch
            driver.findElement(By.name("password")).clear();
            driver.findElement(By.name("confirmPassword")).clear();
            driver.findElement(By.name("password")).sendKeys("Password123");
            driver.findElement(By.name("confirmPassword")).sendKeys("Mismatch123");
            scrollToViewAndClick(driver, continueBtn);
            Thread.sleep(2000);
            takeScreenshot(driver, "screenshots/test_case_3/password_mismatch.png");
            System.out.println("Password mismatch error shown");

            //Valid Inputs
            driver.findElement(By.name("password")).clear();
            driver.findElement(By.name("confirmPassword")).clear();
            driver.findElement(By.name("password")).sendKeys("Password123");
            driver.findElement(By.name("confirmPassword")).sendKeys("Password123");
            scrollToViewAndClick(driver, continueBtn);
            Thread.sleep(3000);
            takeScreenshot(driver, "screenshots/test_case_3/valid_inputs.png");
            System.out.println("Valid input submitted");

        } catch (Exception e) {
            System.out.println("FAILED: " + e.getMessage());
            testPassed = false;
        } finally {
            try (FileWriter fw = new FileWriter("testResults.txt", true)) {
                fw.write("Test Case 3: " + (testPassed ? "Passed" : "Failed") + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Test Case: " + (testPassed ? "Passed" : "Failed"));
            driver.quit();
        }
    }

    public static void takeScreenshot(WebDriver driver, String path) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dst = new File(path);
        dst.getParentFile().mkdirs();
        Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void scrollToViewAndClick(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
        try {
            Thread.sleep(500); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        js.executeScript("arguments[0].click();", element);
    }
}

