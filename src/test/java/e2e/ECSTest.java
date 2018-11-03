package e2e;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ECSTest{

	private WebDriver browser;

	@Test
	public void test() {
		initialiseDriver();
		browseToLocalhost3000();
		clickRenderTheChallengeButton();
		waitTillSecondChallengeIsLoaded();
		int[][] tableData = loadDataFromTableToArrays();
		populateBalancedIndices(tableData);
		populateName();
		submitAnswers();
		closePopUp();
		closeBrowser();
	}



	public void initialiseDriver() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
		browser = new ChromeDriver();
	}

	private void browseToLocalhost3000() {
		browser.get("http://localhost:3000");
	}

	private void clickRenderTheChallengeButton() {
		browser.findElement(By.xpath("//button[@data-test-id = 'render-challenge']")).click();
	}

	private void waitTillSecondChallengeIsLoaded() {
		WebDriverWait wait = new WebDriverWait(browser, 20);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("challenge")));
	}

	private int[][] loadDataFromTableToArrays() {
		WebElement table = browser.findElement(By.tagName("tbody"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		int[][] result = new int[rows.size()][];
		
		for (int i = 0; i < rows.size(); i++) {
			List<WebElement> columns = rows.get(i).findElements(By.tagName("td"));
			int[] tmpArray = new int[columns.size()];
			
			for (int j = 0; j < columns.size(); j++)
				tmpArray[j] = Integer.parseInt(columns.get(j).getText());
			
			result[i] = tmpArray;
		}
		return result;
	}
	
	private void populateBalancedIndices(int[][] tableData){
		
		for(int i = 0; i < tableData.length ; i++){
			String balancedIndex = findBalancedIndex(tableData[i])  < 0 ? null : String.valueOf(findBalancedIndex(tableData[i]));
			String xpath = "//input[@data-test-id = 'submit-" + (i+1) + "']";
			browser.findElement(By.xpath(xpath)).sendKeys(balancedIndex);
		}
	}
	
	private int findBalancedIndex(int[] arr){
		//Assuming arrays with less than 3 elements do not qualify
		if(arr.length < 3) 
			return -1;
		
		int leftSum = arr[0], rightSum = 0;
		for(int i = 2; i < arr.length ; i++){
			rightSum += arr[i];
		}
		if(leftSum == rightSum) 
			return 1;
		
		for(int i = 2; i < arr.length-1 ; i++){
			leftSum += arr[i-1];
			rightSum -= arr[i];
			if(leftSum == rightSum) 
				return i;
		}
		
		return -1;
	}
	
	private void populateName(){
		browser.findElement(By.xpath("//input[@data-test-id = 'submit-4']")).sendKeys("Anjali Nair");
	}
	
	private void submitAnswers(){
		browser.findElement(By.xpath("//button[contains(.,'Submit Answers')]")).click();
	}

	private void closePopUp() {
		WebDriverWait wait = new WebDriverWait(browser, 20);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(.,'Congratulations you have succeeded. Please submit your challenge')]")));
		browser.findElement(By.xpath("//button[contains(.,'Close')]")).click();
	}


	private void closeBrowser() {
		browser.close();
	}

}
