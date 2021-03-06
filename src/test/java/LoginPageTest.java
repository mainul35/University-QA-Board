import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.Augmenter;

public class LoginPageTest {
	//intregation test. 
//detect crome driver
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\Mainul Hasan\\Desktop\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		//url to test
		driver.get("http://localhost:8080/");
		Thread.sleep(7000); // Let the user actually see something!
		captureScreen(driver, "login_page");

		
		//invalid email and pass entered. try wrong email
		WebElement emailBox = driver.findElement(By.id("inputEmail"));
		emailBox.sendKeys("admin");
		WebElement passwordBox=driver.findElement(By.id("inputPassword"));
		passwordBox.sendKeys("secre");
		captureScreen(driver, "invalide Input_Entered");
		WebElement submit = driver.findElement(By.className("btn"));

		Thread.sleep(5000); // Let the user actually see something!
		submit.submit();
		captureScreen(driver, "invelade_entry");
		
		
		
		//valid entry
		emailBox = driver.findElement(By.id("inputEmail"));
		emailBox.sendKeys("admin");
		passwordBox=driver.findElement(By.id("inputPassword"));
		passwordBox.sendKeys("secret");
		captureScreen(driver, "Valide_Input_Entered");
		submit = driver.findElement(By.className("btn"));

		Thread.sleep(3000); // Let the user actually see something!
		submit.submit();
		Thread.sleep(4000);
		captureScreen(driver, "Loged In");
		driver.quit();
		
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
