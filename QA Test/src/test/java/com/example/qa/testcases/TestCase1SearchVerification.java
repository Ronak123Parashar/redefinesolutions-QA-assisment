package com.example.qa.testcases;

import com.example.qa.components.Components_flipkart;
import org.openqa.selenium.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class TestCase1SearchVerification {
    public static void main(String[] args) {
        WebDriver driver = Components_flipkart.initDriver();
        boolean testPassed = true;

        try {
            driver.get("https://www.flipkart.com/");
            Thread.sleep(3000);
            takeScreenshot(driver, "screenshots/test_case_1/homepage.png");

            try {
                WebElement closeBtn = driver.findElement(By.cssSelector("button._2KpZ6l._2doB4z"));
                closeBtn.click();
                Thread.sleep(1000);
            } catch (Exception ignored) {}

            WebElement searchBox = driver.findElement(By.name("q"));
            searchBox.sendKeys("mouse");
            searchBox.sendKeys(Keys.ENTER);
            Thread.sleep(4000);
            takeScreenshot(driver, "screenshots/test_case_1/search_results.png");

            List<WebElement> productLinks = null;
            int attempts = 0;
            while (attempts < 5) {
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 600);");
                Thread.sleep(1500);
                productLinks = driver.findElements(By.cssSelector("a.wjcEIp, a.wjcEIp, a.wjcEIp")); // common for laptop/mouse
                if (productLinks.size() >= 3) break;
                attempts++;
            }

            if (productLinks == null || productLinks.size() < 3) {
                throw new Exception("Less than 3 products found on Flipkart after scrolling.");
            }

            for (int i = 0; i < 3; i++) {
                String originalWindow = driver.getWindowHandle();
                WebElement product = productLinks.get(i);
                String productHref = product.getAttribute("href");

                if (!productHref.startsWith("http")) {
                    productHref = "https://www.flipkart.com" + productHref;
                }

                ((JavascriptExecutor) driver).executeScript("window.open(arguments[0])", productHref);
                Thread.sleep(2000);

                for (String windowHandle : driver.getWindowHandles()) {
                    if (!windowHandle.equals(originalWindow)) {
                        driver.switchTo().window(windowHandle);
                        takeScreenshot(driver, "screenshots/test_case_1/product" + (i + 1) + "_details.png");
                        break;
                    }
                }

                Thread.sleep(3000);

                String title = "", price = "", availability = "";
                boolean allValid = true;
                try {
                    for (int t = 0; t < 10; t++) {
                        try {
                            WebElement titleElem = driver.findElement(By.cssSelector("span.VU-ZEz"));
                            if (titleElem.isDisplayed()) {
                                title = titleElem.getText();
                                break;
                            }
                        } catch (Exception ignored) {
                            Thread.sleep(500);
                        }
                    }
                    if (title.isEmpty()) throw new Exception("Title not loaded");
                } catch (Exception e) {
                    System.out.println("Product " + (i + 1) + ": Title not found");
                    allValid = false;
                }

                try {
                    for (int p = 0; p < 10; p++) {
                        try {
                            WebElement priceElem = driver.findElement(By.cssSelector("div.Nx9bqj"));
                            if (priceElem.isDisplayed()) {
                                price = priceElem.getText();
                                break;
                            }
                        } catch (Exception ignored) {
                            Thread.sleep(500);
                        }
                    }
                    if (price.isEmpty()) throw new Exception("Price not loaded");
                } catch (Exception e) {
                    System.out.println("Product " + (i + 1) + ": Price not found");
                    allValid = false;
                }
                try {
                	 WebElement pincodeInput = driver.findElement(By.id("pincodeInputId"));
                     pincodeInput.clear();
                     pincodeInput.sendKeys("452020");
                     pincodeInput.sendKeys(Keys.ENTER);
                     Thread.sleep(3000); 
                    List<WebElement> availElems = driver.findElements(By.cssSelector("div.hVvnXm"));
                    if (!availElems.isEmpty()) {
                    	System.out.println("Available");
                        availability = availElems.get(0).getText();
                    } else {
                        availability = "Not clearly mentioned";
                    }
                } catch (Exception e) {
                    availability = "Not clearly mentioned exception";
                }


                System.out.println("Product " + (i + 1) + ":");
                System.out.println("Title: " + title);
                System.out.println("Price: " + price);
                System.out.println("Availability: " + availability);
                if(!allValid) testPassed = false;

                driver.close(); 
                driver.switchTo().window(originalWindow);
                Thread.sleep(2000);
            }

        } catch (Exception e) {
            System.out.println("Test Case 1 FAILED: " + e.getMessage());
            testPassed = false;
        } finally {
            try {
                logResult("Test Case 1: " + (testPassed ? "Passed" : "Failed"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            driver.quit();
        }
    }

    public static void takeScreenshot(WebDriver driver, String filePath) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File(filePath);
        dest.getParentFile().mkdirs();
        Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void logResult(String line) throws IOException {
        FileWriter fw = new FileWriter("testResults.txt", true);
        fw.write(line + "\n");
        fw.close();
    }
}
