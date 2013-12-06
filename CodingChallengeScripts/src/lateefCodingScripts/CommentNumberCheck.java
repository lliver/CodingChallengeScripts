package lateefCodingScripts;

import java.util.List;

import org.apache.commons.lang3.text.WordUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
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
	private static String[] Months = {"january","febuary","march","april","may","june","july","august","september","october","november","december"};
	private static String[] Challenge = {"bronze","silver","gold"};
	private  String home;
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
		home = Site;
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
//		System.out.println("Toggle attempt");
//		cycleDropdownToggle();
//		System.out.println("Out of Toggle attempt");
		WebElement element = driver.findElement(By.linkText("Bronze"));
		element.click();	
	}
	
	
	public void cycleDropdownToggle(){
		
		List<WebElement> challengeToggles = driver.findElements(By.cssSelector(".btn.btn-default.dropdown-toggle"));

		System.out.println("Toggle Loop attempt");
		for(WebElement toggle:challengeToggles ){
			toggle.click();
			
			System.out.println("Toggle Clicked");
			//cycleChallenges(toggle);
			toggle.click();
			List<WebElement> challengeListing = driver.findElements(By.cssSelector(".dropdown-menu>li>a"));
			for(WebElement challenge:challengeListing ){
				if(challenge.getText() == "Archive"){
					System.out.println("Stopped here");
					break;
				}else{
					System.out.println("Clicking Challenge attempt");
					challenge.click();
					System.out.println("Challenge test attempt");			
					GetNumberOfComments();
					System.out.println("Going home attempt");		
					new WebDriverWait(driver, 300);
					goHome();
					System.out.println("Setting up for next round attempt");	
					toggle.click();
				}
				System.out.println("Toggle Clicked");
				new WebDriverWait(driver, 300);
			//	cycleChallenges();
				
			}
			new WebDriverWait(driver, 300);
		//	cycleChallenges();
			
		}
	}
	
	private void cycleChallenges(WebElement toggle) {
		// TODO Auto-generated method stub  
		List<WebElement> challengeListing = driver.findElements(By.cssSelector(".dropdown-menu>li>a"));
		System.out.println("Challenge Loop attempt");
		for(WebElement challenge:challengeListing ){
			if(challenge.getText() == "Archive"){
				break;
			}else{
				System.out.println("Clicking Challenge attempt");
				challenge.click();
				System.out.println("Challenge test attempt");			
				GetNumberOfComments();
				System.out.println("Going home attempt");				
				goHome();
				System.out.println("Setting up for next round attempt");				
			}
			System.out.println("Toggle Clicked");
			new WebDriverWait(driver, 300);
		//	cycleChallenges();
			
		}
	}

	
	private void goHome() {
		driver.get(home);
		new WebDriverWait(driver, 300);
		// TODO Auto-generated method stub
		
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
		String ChallengeLevel = getChallengeLevel();
		String Month = getChallengeMonth();

	
		System.out.println(WordUtils.capitalize(Month) + "'s " + WordUtils.capitalize(ChallengeLevel) + " Challenge: " + e.getText());
		
	}

/**
 * 
 * @return
 */
	private String getChallengeMonth() {
		String[] url = driver.getCurrentUrl().split("/");
		int MonthCounter = 0;
		String currMonth = "november";
		for (MonthCounter = 0; MonthCounter > 11; MonthCounter++) {
			if (Months[MonthCounter] == url[2]) {
				currMonth = Months[MonthCounter];
				return currMonth;
			}
		}
		return currMonth;
	}


	private String getChallengeLevel() {
		String[] url = driver.getCurrentUrl().split("/");
		int LevelCounter = 0;
		String currChallenge = "bronze";
		for (LevelCounter = 0; LevelCounter > 2; LevelCounter++) {
			if (Challenge[LevelCounter] == url[3]) {
				currChallenge = Challenge[LevelCounter];
				return currChallenge;
			}
		}
		return currChallenge;
	}



	@AfterTest
	public void allDone(){
		driver.quit();
	}

}
