package lascala

// Untyped (not yet used)

trait MeasureData {}
trait Measure {}
trait Query {}

// Zero-dimensional

trait MeasureData0[V] extends MeasureData {
  def data: V
}

trait Measure0[V] extends MeasureData0[V] with Measure {
  def newQuery(): Query0[V] = new Query0_0(this)
}

trait Query0[V] extends Query {
  def execute(): V
}

class Query0_0[V](measure: Measure0[V]) extends Query0[V] {
  def execute() = measure.data
}

// One-dimensional

trait MeasureData1[V, D1] extends MeasureData {
  def data: Iterator[(D1, V)]
}

trait Measure1[V, D1] extends MeasureData1[V, D1] with Measure {
  def newQuery(): Query1_0[V, D1] = new FilterQuery1_0[V, D1](this)
}

trait Query1_0[V, U1] extends Query {
  def execute(): V
  def restrict(nodes: Seq[U1]): Query1_1[V, U1]
}

trait Query1_1[V, B1] extends Query {
  def execute(): Iterator[(B1, V)]
}

class FilterQuery1_0[V, U1](measure: Measure1[V, U1]) extends Query1_0[V, U1] {
  private var defaultV: V = _
  private var default1: U1 = _

  def execute() = {
    val predicate = (entry: (U1, V)) => entry._1 == default1
    val maybeEntry = measure.data.find(predicate)
    maybeEntry.getOrElse((default1, defaultV))._2
  }

  def restrict(nodes1: Seq[U1]) = new FilterQuery1_1(measure, nodes1)
}

class FilterQuery1_1[V, B1](measure: Measure1[V, B1], nodes1: Seq[B1]) extends Query1_1[V, B1] {
  def execute() = {
    val predicate = (entry: (B1, V)) => nodes1.contains(entry._1)
    measure.data.filter(predicate)
  }
}

// Two-dimensional

trait MeasureData2[V, D1, D2] extends MeasureData {
  def data: Iterator[((D1, D2), V)]
}
