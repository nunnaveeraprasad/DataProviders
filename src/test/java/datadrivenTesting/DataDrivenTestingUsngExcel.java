package datadrivenTesting;
import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DataDrivenTestingUsngExcel {
	WebDriver driver;
	@BeforeClass
	public void setup()
	{
		//System.setProperty("webdriver.chrome.driver", "");
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		
	}
	
	
	@Test(dataProvider = "LoginData")
	public void LoginTest(String username,String password, String exp)
	{
		driver.get("https://admin-demo.nopcommerce.com/login");
		WebElement email = driver.findElement(By.id("Email"));
		email.clear();
		email.sendKeys(username);
		
		WebElement Pass = driver.findElement(By.id("Password"));
		Pass.clear();
		Pass.sendKeys(password);
		
		WebElement loginbutton = driver.findElement(By.cssSelector("button[type='submit']"));
		loginbutton.click();
		
		String exp_Title = "Dashboard / nopCommerce administration";
		String actualTitle = driver.getTitle();
		
		if(exp.equals("Valid"))
		{
			if(exp_Title.equals(actualTitle))
			{
				driver.findElement(By.linkText("Logout")).click();
				Assert.assertTrue(true);
				
			}else {
				Assert.assertTrue(false);
			}
		}else if(exp.equals("Invalid"))
		{
			if(exp_Title.equals(actualTitle))
			{
				driver.findElement(By.linkText("Logout")).click();
				Assert.assertTrue(false);
				
			}else {
				Assert.assertTrue(true );
			}
		}
		 
				
	}
	
	
	@AfterClass
	public void closedriver()
	{
		driver.close();
	}
	
	@DataProvider(name="LoginData")
	public String [][] getDate() throws IOException
	{
		String filePath =".\\src\\test\\resources\\TestData.xlsx";
		XLUtility xlutils = new XLUtility(filePath);
		int rowCount = xlutils.getRowCount("Sheet1");
		int totlacols = xlutils.getCellCount("Sheet1", 1);
		
		String loginData[][] = new String[rowCount][totlacols];
		
		for(int i=1;i<rowCount;i++)
		{
			for(int j=0;j<totlacols;j++)
			{
				loginData[i-1][j]= xlutils.getCellData("Sheet1", i, j);//[i-1] is ignore first row
			}
		}
		
		return loginData;
		
	}
}
