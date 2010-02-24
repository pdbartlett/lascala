package pdbartlett.lascala.scala.data

trait TestData {
  val oneDimData = List(("op1", 6), ("op2", 9), ("ans", 42))
  val twoDimData = List(
      (("UK", Colour.Red), 1),
      (("US", Colour.Blue), 2),
      (("WW", Colour.Red), 1),
      (("WW", Colour.Blue), 2),
      (("UK", null), 1),
      (("US", null), 2),
      (("WW", null), 3)
  )
}

trait DataTestUtils extends TestData {
  def assertIterablesEqual[E](expected: Iterable[E], actual: Iterable[E]) {
    val expectedIter = expected.elements
    val actualIter = actual.elements
    while (expectedIter.hasNext) {
      assert(actualIter.hasNext)
      assert(expectedIter.next == actualIter.next)
    }
    assert(!actualIter.hasNext)
  }
}

object Colour extends Enumeration { val Red, Green, Blue = Value }

class TestBulkDataSource extends BulkDataSource[String, Int] {
  val data = Map("op1" -> 6, "op2" -> 9, "ans" -> 42)
}

object TestPreAggMeasure1 extends TestBulkDataSource with PreAggregated[Int] with Dimensionality1[String]

object TestPreAggMeasure2 extends PreAggregated[Int] with Dimensionality2[String, Colour.Value] with TestData {
  val data = twoDimData
}
