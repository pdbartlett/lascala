package pdbartlett.lascala.scala.data

import org.scalatest.FunSuite

class MeasureTest extends FunSuite with DataTestUtils {
  test("preAggMeasure1") {
    assertIterablesEqual(oneDimData, TestPreAggMeasure1.data)
    assertIterablesEqual(oneDimData, TestPreAggMeasure1.aggData)
  }

  test("preAggMeasure2") {
    assertIterablesEqual(twoDimAggData, TestPreAggMeasure2.data)
    assertIterablesEqual(twoDimAggData, TestPreAggMeasure2.aggData)
  }

  test("bulkAggMeasure2") {
    assertIterablesEqual(twoDimData, TestBulkAggMeasure.data)
    assertPairIterablesEqualIgnoreOrder(twoDimAggData, TestBulkAggMeasure.aggData)
  }

  test("readOnceMeasure") {
    assertPairIterablesEqualIgnoreOrder(twoDimAggData, TestReadOnceMeasure.aggData)
    try {
      assertPairIterablesEqualIgnoreOrder(twoDimAggData, TestReadOnceMeasure.aggData)
      assert(false, "Should not reach here - expected IllegalStateException")
    } catch {
      case e: IllegalStateException => assert(true)
      case t: Throwable => assert(false, "Incorrect exception type " + t + " - IllegalStateException expected")
    }
  }

  test("storageMeasure") {
    assertPairIterablesEqualIgnoreOrder(twoDimAggData, TestStorageMeasure.aggData)
    assertPairIterablesEqualIgnoreOrder(twoDimAggData, TestStorageMeasure.aggData)
  }
}
