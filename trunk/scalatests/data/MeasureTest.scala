package pdbartlett.lascala.scala.data

import org.scalatest.FunSuite

class MeasureTest extends FunSuite with DataTestUtils {
  test("preAggMeasure1_data") {
    assertIterablesEqual(oneDimData, TestPreAggMeasure1.data)
  }

  test("preAggMeasure1_aggData") {
    assertIterablesEqual(oneDimData, TestPreAggMeasure1.aggData)
  }

  test("preAggMeasure2_data") {
    assertIterablesEqual(twoDimData, TestPreAggMeasure2.data)
  }

  test("preAggMeasure2_aggData") {
    assertIterablesEqual(twoDimData, TestPreAggMeasure2.aggData)
  }
}
