package com.dwang;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import com.dwang.sound.SoundClipPlayer;

public class IphoneAvailSelenium {

	private WebDriver driver;
	private String baseUrl;
	private String url;
	private boolean websiteAvailability;
	
	public IphoneAvailSelenium() {
		init();
	}

	public static void main(String[] args) throws Exception {
		IphoneAvailSelenium iphoneAvail = new IphoneAvailSelenium();
		iphoneAvail.checkAll();
	}
	
	private void init() {
		//driver = new FirefoxDriver();
		baseUrl = "https://reserve.cdn-apple.com/";
		url = baseUrl + "/HK/en_HK/reserve/iPhone/availability";
		websiteAvailability = true;
	}

	@Deprecated
	public void run() {
		driver = new FirefoxDriver();
		baseUrl = "https://reserve.cdn-apple.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		driver.get(baseUrl + "/HK/en_HK/reserve/iPhone/availability");
		
		if (driver.findElement(By.cssSelector("BODY")).getText().contains("Please come back")) {
			System.out.println("IPhone reservation is closed. Try again tomorrow.");
			driver.quit();
			return;
		}
		
		new Select(driver.findElement(By.name("products")))
				.selectByVisibleText("iPhone 6 Plus");
		new Select(driver.findElement(By.name("stores")))
				.selectByVisibleText("ifc mall");
		// Warning: assertTextPresent may require manual changes
		if (driver.findElement(By.cssSelector("BODY")).getText().contains("Available")) {
			new SoundClipPlayer();
		} else {
			System.out.println("IPhone is not available at this time!");
		}

		driver.quit();
	}
	
	public void checkAll() {
		checkIphoneAvailability("iPhone 6 Plus", "ifc mall");
		checkIphoneAvailability("iPhone 6", "ifc mall");
	}
	
	public boolean checkWebsiteAvailability() {
		this.websiteAvailability = !driver.findElement(By.cssSelector("BODY")).getText().contains("Please come back");
		return websiteAvailability;
	}
	
	public boolean checkIphoneAvailability(String product, String store) {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(url);
		if (!checkWebsiteAvailability()) {
			System.out
					.println("IPhone reservation is closed. Try again tomorrow.");
			driver.quit();
			return false;
		}

		new Select(driver.findElement(By.name("products")))
				.selectByVisibleText(product);
		new Select(driver.findElement(By.name("stores")))
				.selectByVisibleText(store);
		// Warning: assertTextPresent may require manual changes
		if (driver.findElement(By.cssSelector("BODY")).getText()
				.contains("Available")) {
			System.out.println(String.format("%s is available in %s at %s", product, store, new Date()));
			new SoundClipPlayer();
		} else {
			System.out.println(String.format("%s is not available in %s at %s", product, store, new Date()));
		}

		driver.quit();
		return true;
	}
	
	public boolean getWebsiteAvailability() {
		return this.websiteAvailability;
	}

}
