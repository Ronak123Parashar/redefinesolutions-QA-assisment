# QA Automation Assisment Redefine Solutions Pvt.Ltd. Solution

This project contains Selenium WebDriver automation test scripts written in Java. It is structured according to the exam guidelines and includes the following test cases:

1. **Search and Product Validation** – on Flipkart   
2. **Cart Functionality Validation** – on Flipkart
3. **Form Validation** – on Apple’s Account Page (https://account.apple.com/account)

---

## 📁 Project Structure
- As per given in the Assisment pdf 
---

## ⚙️ Prerequisites

- Java JDK 22
- Chrome browser
- ChromeDriver 
- Selenium WebDriver 4.18.1

---

## ▶️ Running the Tests

1. Open the project in anu ide  
2. Go to one of the test classes under:
   ```
   src/test/java/com/example/qa/testcases/
   ```
3. Right-click on the file > Run `main()`  
4. View:
   - Console log (PASS/FAIL for each step)
   - Screenshots in `/screenshots/test_case_n/`
   - Summary results in `testResults.txt`

---

## 🖼 Screenshot Output

Screenshots for each validation step are stored in:

```
screenshots/test_case_1/
screenshots/test_case_2/
screenshots/test_case_3/
```

Filenames are meaningful, such as:

- `homepage.png`
- `product1_details.png`
- `product2_details.png`
- `and soo on........`

---

## 📄 Test Results Summary

After running, the file `testResults.txt` will contain (Result in the below form):

```
Test Case 1: Passed / Failed 
Test Case 2: Passed /Failed
Test Case 3: Passed/ Failed
```

