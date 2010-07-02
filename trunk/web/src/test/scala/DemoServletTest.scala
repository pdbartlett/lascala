package pdbartlett.lascala.web

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
		driver.quit
	}
}
