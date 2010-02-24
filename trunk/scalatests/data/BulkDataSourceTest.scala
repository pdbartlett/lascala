package pdbartlett.lascala.scala.data

import org.scalatest.FunSuite

class BulkDataSourceTest extends FunSuite with DataTestUtils {
  test("data") {
    val bulkData = new TestBulkDataSource
    assertIterablesEqual(oneDimData, bulkData.data)
  }
}
