package pdbartlett.lascala.web

import pdbartlett.lascala.lib._

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver

import org.scalatest.FunSuite

class DemoServletTest extends FunSuite {
	
	test("sanity check") {
		assert(true)
	}
	
	test("actual test of demo servlet") {
		val url = "http://localhost:8080/"
		val driver: WebDriver = new HtmlUnitDriver
		driver.get(url)
		assert(driver.getCurrentUrl === url)
		assert(driver.getTitle === "Demo Servlet")
		val paraText = driver.findElement(By.tagName("p")).getText
		assert(paraText.endsWith(Demo.answer.toString))
		driver.quit
	}
}
