package pdbartlett.lascala.lib

import org.scalatest.FunSuite

class DemoTest extends FunSuite {
	
	test("sanity check") {
		assert(true)
	}
	
	test("actual test of demo code") {
		assert(Demo.answer === 42)
	}
}
