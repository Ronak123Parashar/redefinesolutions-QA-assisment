package com.example.qa.testcases;

import com.example.qa.components.Components_flipkart;
import org.openqa.selenium.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class TestCase2CartValidation {
    public static void main(String[] args) {
        WebDriver driver = Components_flipkart.initDriver();
        boolean testPassed = true;

        try {
//        	Driver.get help to calling the api using get requst
            driver.get("https://www.flipkart.com/");
            Thread.sleep(3000);
            takeScreenshot(driver, "screenshots/test_case_2/homepage.png");

            try {
                WebElement closeBtn = driver.findElement(By.cssSelector("button._2KpZ6l._2doB4z"));
                closeBtn.click();
                Thread.sleep(1000);
            } catch (Exception ignored) {}

            WebElement searchBox = driver.findElement(By.name("q"));
            searchBox.sendKeys("headphones");
            searchBox.sendKeys(Keys.ENTER);
            Thread.sleep(4000);
//            take screenshot of the search result
            takeScreenshot(driver, "screenshots/test_case_2/search_results.png");

            List<WebElement> productLinks = null;
            int attempts = 0;
            while (attempts < 5) {
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 600);");
                Thread.sleep(1500);
                productLinks = driver.findElements(By.cssSelector("a.wjcEIp")); // selector
                if (productLinks.size() >= 1) break;
                attempts++;
            }

            if (productLinks == null || productLinks.size() < 1) {
                throw new Exception("No products found");
            }

            String originalWindow = driver.getWindowHandle();
            String productHref = productLinks.get(0).getAttribute("href");
            if (!productHref.startsWith("http")) {
                productHref = "https://www.flipkart.com" + productHref;
            }

            ((JavascriptExecutor) driver).executeScript("window.open(arguments[0])", productHref);
            Thread.sleep(2000);

            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    takeScreenshot(driver, "screenshots/test_case_2/product_page.png");
                    break;
                }
            }

            Thread.sleep(3000);

            String productName = "", productPrice = "",availability="";
            boolean allValid = true;
            try {
           	 WebElement pincodeInput = driver.findElement(By.id("pincodeInputId"));
//           	 inputing pincode to checck availability off the product in the area or city 
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
            try {
                WebElement titleElem = driver.findElement(By.cssSelector("span.VU-ZEz"));
                productName = titleElem.getText();
            } catch (Exception e) {
                System.out.println("title not available" + e);
                allValid = false;
            }

            try {
                WebElement priceElem = driver.findElement(By.cssSelector("div.Nx9bqj"));
                productPrice = priceElem.getText();
            } catch (Exception e) {
                System.out.println("price not available"+e);
                allValid = false;
            }
//            print the result if the products 
            System.out.println("Avilibility:"+ availability);
            System.out.println("Product: " + productName);
            System.out.println("Price: " + productPrice);
            System.out.println("below process require login thats why they are throwing erroa");

            try {
                WebElement pincodeInput = driver.findElement(By.id("pincodeInputId"));
                pincodeInput.clear();
//                to check that the product is available or not 
                pincodeInput.sendKeys("452020");
                pincodeInput.sendKeys(Keys.ENTER);
                Thread.sleep(3000);
            } catch (Exception ignored) {}

            try {
                WebElement addToCartBtn = driver.findElement(By.xpath("//button[contains(text(),'Add to cart')]"));
                addToCartBtn.click();
                Thread.sleep(4000);
                takeScreenshot(driver, "screenshots/test_case_2/after_add_to_cart.png");
            } catch (Exception e) {
                System.out.println(e);
                allValid = false;
            }
// check the cart 
            driver.get("https://www.flipkart.com/viewcart");
            Thread.sleep(3000);
            takeScreenshot(driver, "screenshots/test_case_2/cart_page.png");
            try {
                WebElement cartItem = driver.findElement(By.cssSelector("a._2Kn22P.gBNbID"));
                String cartItemName = cartItem.getText();
                if (!cartItemName.contains(productName.substring(0, 5))) {
                    throw new Exception("Item was not added");
                }
            } catch (Exception e) {
                System.out.println("Product not found inside cart");
                allValid = false;
            }
            try {
                WebElement removeBtn = driver.findElement(By.xpath("//div[text()='Remove']"));
                removeBtn.click();
                Thread.sleep(1000);
                WebElement confirmRemove = driver.findElement(By.xpath("//div[text()='Remove Item']"));
                confirmRemove.click();
                Thread.sleep(3000);
                takeScreenshot(driver, "screenshots/test_case_2/empty_cart.png");
            } catch (Exception e) {
                System.out.println("unable to remove the product");
                allValid = false;
            }
            if (!driver.getPageSource().contains("no item")) {
                System.out.println("cart is not empty");
                allValid = false;
            }

            if (!allValid) testPassed = false;

        } catch (Exception e) {
            System.out.println("Test Case 2 FAILED: " + e.getMessage());
            testPassed = false;
        } finally {
            try {
//            	log  result pass or fail 
                logResult("Test Case 2: " + (testPassed ? "Passed" : "Failed"));
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
