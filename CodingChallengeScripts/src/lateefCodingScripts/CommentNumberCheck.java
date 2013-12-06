package lateefCodingScripts;

import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
/**
 * 
 * @author Lateef
 * Date: Dec 6, 2013
 * 
 * 
 * 
 * 
 */
public class CommentNumberCheck {

	private WebDriver driver;

	@BeforeTest
	public void initalize() {
		driver = new FirefoxDriver();
	}


	@DataProvider
	public Object[][] getData() {
		Object[][] data = new Object[1][5];

		data[0][0] = "Test";
		data[0][1] = "User";
		data[0][2] = "anything";
		data[0][3] = "http://10.238.242.37:4000/";
		data[0][4] = new FirefoxDriver();
		return data;
	}

	@DataProvider
	public Object getDriver() {
		Object drive = new FirefoxDriver();
		return drive;

	}

	/**
	 * Logs into site
	 * @param FirstName
	 * @param LastName
	 * @param Pass
	 * @param Site
	 */
	@Parameters({ "FirstName", "LastName", "Pass", "Site" })
	@Test
	public void login(String FirstName, String LastName, String Pass,
			String Site) {

		System.out.println("Logging in......");

		String User = WordUtils.capitalize(FirstName) + "." + WordUtils.capitalize(LastName);
		User = User.toLowerCase();
		driver.get(Site);
		driver.manage().window().maximize();
		System.out.println("Logging in");
		WebElement element = driver.findElement(By.name("user[username]"));

		element.sendKeys(User);

		element = driver.findElement(By.name("user[password]"));
		element.sendKeys(Pass);
		element.submit();

		boolean PassFail = driver.findElement(By.partialLinkText(WordUtils.capitalize(FirstName))).isDisplayed();

		if (PassFail) {
			System.out.println("Logging Successful");
		} else {
			System.out.println("Logging UnSuccessful");
		}
		assert (PassFail);
	}


	@Test(dependsOnMethods = { "login" })
	/**
	 * 
	 */
	public void navigateToTargetLink(){
		WebElement element = driver.findElement(By.linkText("Bronze"));
		element.click();	
	}
	
	@Test(dependsOnMethods = { "navigateToTargetLink" })
	/**
	 * @returns
	 */
	public void GetNumberOfComments() {

		getChallengeTitle();

		int totalComments = 0;
		WebElement e = driver.findElement(By.partialLinkText(" Comments"));
		String text = e.getText();
		e.click();
		System.out.println(text);
		int commentTotal = driver.findElements(By.className("created-time")).size();

		int replies = 0;
		List<WebElement> img = driver.findElements(By.cssSelector("#comments>article>header>img"));
		for (WebElement imgs : img) {
			if (imgs.getAttribute("alt").equals("Reply")) {
				replies++;
			}

		}

		commentTotal = commentTotal - replies;
		totalComments = commentTotal + replies;
		System.out.println("Comment totals " + commentTotal);
		System.out.println("replies " + replies);
		System.out.println("Comments + Replies: " + totalComments);

	}
	
	/**
	 * Gets Title for current challenge
	 */
	private void getChallengeTitle() {
		WebElement e = driver.findElement(By.id("challengeTitle"));
		System.out.println("Challenge: " + e.getText());
		
	}


	@AfterTest
	public void allDone(){
		driver.quit();
	}

}
