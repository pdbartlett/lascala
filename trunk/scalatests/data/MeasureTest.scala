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
    assertIterablesEqual(twoDimAggData, TestPreAggMeasure2.data)
  }

  test("preAggMeasure2_aggData") {
    assertIterablesEqual(twoDimAggData, TestPreAggMeasure2.aggData)
  }

  test("bulkAggMeasure2_data") {
    assertIterablesEqual(twoDimData, TestBulkAggMeasure2.data)
  }

  test("bulkAggMeasure2_aggData") {
    assertPairIterablesEqualIgnoreOrder(twoDimAggData, TestBulkAggMeasure2.aggData)
  }
}
