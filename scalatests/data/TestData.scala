package pdbartlett.lascala.scala.data

trait TestData {
  val oneDimData = List(("op1", 6), ("op2", 9), ("ans", 42))
  val twoDimData = List(
      (("UK", Colour.Red), 1),
      (("US", Colour.Blue), 2)
  )
  val twoDimAggData = twoDimData ++ List(
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
      assert(actualIter.hasNext, "actual shorter than expected")
      val expectedValue = expectedIter.next
      val actualValue = actualIter.next
      assertEquals(expectedValue, actualValue)
    }
    assert(!actualIter.hasNext, "actual longer than expected")
  }

  def assertPairIterablesEqualIgnoreOrder[K, V](expected: Iterable[(K, V)], actual: Iterable[(K, V)]) {
    var expectedMap: Map[K,V] = Map()
    expectedMap ++= expected
    var actualMap: Map[K, V] = Map()
    actualMap ++= actual
    assertEquals(expectedMap.size, actualMap.size)
    for (key <- expectedMap.keys) {
      assertEquals(expectedMap(key), actualMap(key), key)
    }
  }

  def assertEquals[T](expected: T, actual: T) {
    assertEquals(expected, actual, "")
  }

  // TODO: get === to work instead
  def assertEquals[T, U](expected: T, actual: T, context: U) {
    assert(expected == actual, actual + " != " + expected + ": " + context)
  }
}

object Colour extends Enumeration { val Red, Green, Blue = Value }

class SimpleNode[T](val value: T, val parent: Option[Node[T]]) extends Node[T]

object ColoursDim extends Dimension[Colour.Value] {
  private val rootNode = new SimpleNode[Colour.Value](null, None)
  private val redNode = new SimpleNode[Colour.Value](Colour.Red, Some(rootNode))
  private val greenNode = new SimpleNode[Colour.Value](Colour.Green, Some(rootNode))
  private val blueNode = new SimpleNode[Colour.Value](Colour.Blue, Some(rootNode))
  private val nodes = Map(
      Colour.Red -> redNode,
      Colour.Green -> greenNode,
      Colour.Blue -> blueNode)
  def node(colour: Colour.Value) = if (colour == null) rootNode else nodes(colour)
}

object CountriesDim extends Dimension[String] {
  private val rootNode = new SimpleNode("WW", None)
  private val ukNode = new SimpleNode("UK", Some(rootNode))
  private val usNode = new SimpleNode("US", Some(rootNode))
  private val nodes = Map("WW" -> rootNode, "UK" -> ukNode, "US" -> usNode)
  def node(country: String) = nodes(country)
}

class TestBulkDataSource extends BulkDataSource[String, Int] {
  val data = Map("op1" -> 6, "op2" -> 9, "ans" -> 42)
}

class ReadOnceData extends Iterable[((String, Colour.Value), Int)] with TestData {
  var count = 0
  def elements = {
    count += 1
    if (count == 1) twoDimAggData.elements else throw new IllegalStateException
  }
}

class TestFact(val country: String, val colour: Colour.Value, val count: Int)

object TestFact {
  def apply(country: String, colour: Colour.Value, count: Int) = new TestFact(country, colour, count)
  def unapply(f: TestFact) = Some((f.country, f.colour, f.count)) // Extractors seem a natural fit for fact-based measures, but aren't yet
}

object TestPreAggMeasure1 extends TestBulkDataSource with PreAggregated[Int] with Dimensionality1[String]

object TestPreAggMeasure2 extends PreAggregated[Int] with Dimensionality2[String, Colour.Value] with TestData {
  val data = twoDimAggData
}

object TestBulkAggMeasure extends BulkAggregated2[Int, String, Colour.Value] with IntsAggregatedBySum with TestData {
  val data = twoDimData
  val dim1 = CountriesDim
  val dim2 = ColoursDim
}

object TestReadOnceMeasure extends PreAggregated[Int] with Dimensionality2[String, Colour.Value] {
  val data = new ReadOnceData
}

object TestStorageMeasure extends PreAggregated[Int] with InMemoryStorage[Int] with Dimensionality2[String, Colour.Value] {
  val data = new ReadOnceData
}

object TestFactBasedMeasure extends FactBasedMeasure2[TestFact, Int, String, Colour.Value] with BulkAggregated2[Int, String, Colour.Value]
    with IntsAggregatedBySum {
  val factData = List(TestFact("UK", Colour.Red, 1), TestFact("US", Colour.Blue, 2))
  def extract(f: TestFact) = ((f.country, f.colour), f.count)
  val dim1 = CountriesDim
  val dim2 = ColoursDim
}
