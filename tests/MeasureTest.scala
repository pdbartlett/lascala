package lascala

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
    assert(data == 96)
  }

  test("testMeasure1_query1_valid") {
    checkMeasure1_query1("Question", 54)
    checkMeasure1_query1("Answer", 42)
    checkMeasure1_query1(null, 96)
  }

  test("testMeasure1_query1_invalid") {
    val measure = new TestMeasure1
    val query = measure.newQuery().restrict(List("Foo"))
    val data = query.execute()
    assert(!data.hasNext)
  }

  test("testMeasure1_query1_multiple") {
    val measure = new TestMeasure1
    val query = measure.newQuery().restrict(List("Question", "Answer"))
    val data = query.execute()
    assert(data.hasNext)
    assert(data.next == ("Question", 54))
    assert(data.hasNext)
    assert(data.next == ("Answer", 42))
    assert(!data.hasNext)
  }

  test("testMeasure2_data") {
    val measure = new TestMeasure2
    val data = measure.data
    assert(data.collect.size == 3)
  }

  private def checkMeasure1_query1(name: String, expected: Int) {
    val measure = new TestMeasure1
    val query = measure.newQuery().restrict(List(name))
    val data = query.execute()
    assert(data.hasNext)
    assert(data.next == (name, expected))
    assert(!data.hasNext)
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

class TestMeasure2 extends MeasureData2[Double, String, Int] {
  def data = Iterator.fromValues(
      (("Paul", 2), 1.23),
      (("Rosie", 2), 4.56),
      (("Sophie", 1), 7.89))
}
