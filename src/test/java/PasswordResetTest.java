import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.ScreenshotException;

public class PasswordResetTest {

	
	
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\Mainul Hasan\\Desktop\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		//url to test
		driver.get("http://localhost:8080/ewsd/login");
		Thread.sleep(2000); // Let the user actually see something!
		captureScreen(driver, "login_page");

		
		//click reset password link... 
		WebElement passwordResetLink = driver.findElement(By.linkText("I forgot my password"));
		passwordResetLink.click();
		Thread.sleep(3000); 
		captureScreen(driver, "enter email to reset password");
		
		//send link and input email
		WebElement inputEmail = driver.findElement(By.name("email"));
		inputEmail.sendKeys("sazzadhossainsakib@gmail.com");
		captureScreen(driver, "email enterd for password reset link");
		
		//submit button for link
		WebElement submit = driver.findElement(By.className("btn"));
		submit.click();
		Thread.sleep(2000);
		captureScreen(driver, "link sent to the email successfully");
		driver.get("https://mail.google.com");
		
		//driver.quit();
		
		
		
	}
	
	//function to take screenshot.
	public static void captureScreen(WebDriver driver, String screenShotName) {
	    String path;
	    try {
	        File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	        path = "F:\\Study\\4. BIT\\EWSD\\" + screenShotName+".png";
	        FileUtils.copyFile(source, new File(path)); 
	    }
	    catch(IOException e) {
	        path = "Failed to capture screenshot: " + e.getMessage();
	    }
	}

	
}
