package pdbartlett.lascala.lib

import pdbartlett.lascala.lib.Jobs._

import org.scalatest.FunSuite

class JobsTest extends FunSuite {
	
	test("sanity check") {
		assert(true)
	}
	
	test("simple test of jobs code") {
		defineJob("test1") as { () => println("Testing") }
		job("test1").apply()
	}
	
	test("test of functions forming a pipeline job") {
		val f1 = { () => "abc" }
		val f2 = { (s: String) => println(s) }
		defineJob("test2") as pipeline(f1, f2)
		job("test2").apply()
		
		val f15 = { (s: String) => s * 2 }
		defineJob("test3") as pipeline(f1, f15, f2)
		job("test3").apply()
	}
}
