package pdbartlett.lascala.scala

import org.scalatest.FunSuite

class MeasureTest extends FunSuite {

  test("testMeasure0_data") {
    val measure = new TestMeasure0
    assert(measure.data == 42)
  }

  test("testMeasure0_query") {
    val measure = new TestMeasure0
    val query = measure.newQuery()
    assert(query.execute() == 42)
  }

  test("testMeasure1_data") {
    val measure = new TestMeasure1
    val data = measure.data
    assert(data.hasNext)
    assert(data.next == ("Question", 54))
    assert(data.hasNext)
    assert(data.next == ("Answer", 42))
    assert(data.hasNext)
    assert(data.next == (null, 96))
    assert(!data.hasNext)
  }

  test("testMeasure1_query0") {
    val measure = new TestMeasure1
    val query = measure.newQuery()
    val data = query.execute()
    checkSingleItem(data, (null, 96))
  }

  test("testMeasure1_query1_valid") {
    checkMeasure1_query1("Question", 54)
    checkMeasure1_query1("Answer", 42)
    checkMeasure1_query1(null, 96)
  }

  test("testMeasure1_query1_invalid") {
    val measure = new TestMeasure1
    val query = measure.newQuery().select(List("Foo"))
    val data = query.execute()
    assert(!data.hasNext)
  }

  test("testMeasure1_query1_multipleTogether") {
    val measure = new TestMeasure1
    val query = measure.newQuery().select(List("Question", "Answer"))
    val data = query.execute()
    checkMeasure1Query1MultipleData(data)
  }

  test("testMeasure1_query1_multipleSeparate") {
    val measure = new TestMeasure1
    val query = measure.newQuery().select(List("Question")).select(List("Answer"))
    val data = query.execute()
    checkMeasure1Query1MultipleData(data)
  }

  test("testMeasure2_data") {
    val measure = new TestMeasure2
    val data = measure.data
    assert(data.collect.size == 10)
  }

  test("testMeasure2_query0") {
    val measure = new TestMeasure2
    val data = measure.newQuery().execute()
    checkSingleItem(data, ((null, null), 14.79))
  }

  test("testMeasure2_query1") {
    val measure = new TestMeasure2
    val query = measure.newQuery().select(List(Blue))
    val data = query.execute()
    checkSingleItem(data, ((Blue, null), 9.00))
  }

  private def checkMeasure1_query1(name: String, expected: Int) {
    val measure = new TestMeasure1
    val query = measure.newQuery().select(List(name))
    val data = query.execute()
    checkSingleItem(data, (name, expected))
  }

  private def checkMeasure1Query1MultipleData(data: Iterator[(String, Int)]) {
    assert(data.hasNext)
    assert(data.next == ("Question", 54))
    assert(data.hasNext)
    assert(data.next == ("Answer", 42))
    assert(!data.hasNext)
  }

  private def checkSingleItem[A](it: Iterator[A], a: A) {
    assert(it.hasNext)
    assert(it.next == a)
    assert(!it.hasNext)
  }
}

class TestMeasure0 extends Measure0[Int] {
  def data = 42
}

class TestMeasure1 extends Measure1[Int, String] {
  def data = Iterator.fromValues(
      ("Question", 54),
      ("Answer", 42),
      (null, 96))
}

class TestMeasure2 extends Measure2[Double, Colour, Size] {
  def data = Iterator.fromValues(
      ((Red, Small), 1.23),
      ((Green, Large), 4.56),
      ((Blue, Small), 7.89),
      ((Blue, Large), 1.11),
      ((Red, null), 1.23),
      ((Green, null), 4.56),
      ((Blue, null), 9.00),
      ((null, Small), 9.12),
      ((null, Large), 5.67),
      ((null, null), 14.79))
}

class Colour
case object Red extends Colour
case object Green extends Colour
case object Blue extends Colour

class Size
case object Small extends Size
case object Large extends Size
