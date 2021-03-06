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

class SimpleNode[T](val value: T, val parent: Option[Node[T]], val isLeaf: Boolean) extends Node[T]

object ColoursDim extends Dimension[Colour.Value] {
  private val rootNode = new SimpleNode[Colour.Value](null, None, false)
  private val redNode = new SimpleNode[Colour.Value](Colour.Red, Some(rootNode), true)
  private val greenNode = new SimpleNode[Colour.Value](Colour.Green, Some(rootNode), true)
  private val blueNode = new SimpleNode[Colour.Value](Colour.Blue, Some(rootNode), true)
  private val nodes = Map(Colour.Red -> redNode, Colour.Green -> greenNode, Colour.Blue -> blueNode)
  val defaultValue = null
  val values = nodes.keySet ++ List(defaultValue)
  def node(colour: Colour.Value) = if (colour == defaultValue) rootNode else nodes(colour)
}

object CountriesDim extends Dimension[String] {
  private val rootNode = new SimpleNode("WW", None, false)
  private val ukNode = new SimpleNode("UK", Some(rootNode), true)
  private val usNode = new SimpleNode("US", Some(rootNode), true)
  private val nodes = Map("WW" -> rootNode, "UK" -> ukNode, "US" -> usNode)
  val defaultValue = "WW"
  val values = nodes.keySet
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
  // Extractors seem as if they'd be a natural fit for fact-based measures, but aren't yet of any use yet.
  def unapply(f: TestFact) = Some((f.country, f.colour, f.count))
}

object TestPreAggMeasure1 extends TestBulkDataSource with Measure1[Int, String] with PreAggregated {
  val dim1 = null // So cannot be aggregared or queried.
}

object TestPreAggMeasure2 extends Measure2[Int, String, Colour.Value] with PreAggregated with TestData {
  val data = twoDimAggData
  val dim1 = CountriesDim
  val dim2 = ColoursDim
}

object TestBulkAggMeasure extends Measure2[Int, String, Colour.Value] with BulkAggregated2 with IntsAggregatedBySum with TestData {
  val data = twoDimData
  val dim1 = CountriesDim
  val dim2 = ColoursDim
}

object TestReadOnceMeasure extends Measure2[Int, String, Colour.Value] with PreAggregated {
  val data = new ReadOnceData
  val dim1 = CountriesDim
  val dim2 = ColoursDim
}

object TestStorageMeasure extends LookupMeasure2[Int, String, Colour.Value] with PreAggregated with InMemoryStorage {
  val data = new ReadOnceData
  val dim1 = CountriesDim
  val dim2 = ColoursDim
}

object TestFactBasedMeasure extends FactBasedMeasure2[TestFact, Int, String, Colour.Value] with BulkAggregated2 with IntsAggregatedBySum {
  val factData = List(TestFact("UK", Colour.Red, 1), TestFact("US", Colour.Blue, 2))
  def extract(f: TestFact) = ((f.country, f.colour), f.count)
  val dim1 = CountriesDim
  val dim2 = ColoursDim
}
