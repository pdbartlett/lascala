package lascala

// Helper methods, and possibly untyped versions later

trait MeasureData {}
trait Measure {}
trait Query {
  def isMatchingNode[D](node: D, nodeList: Seq[D], default: D): Boolean = {
    (nodeList.isEmpty && node == default) || nodeList.contains(node)
  }
}

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
  def newQuery(): Query1[V, D1] = new FilterQuery1[V, D1](this, List())
}

trait Query1[V, D1] extends Query {
  def execute(): Iterator[(D1, V)]
  def select(nodes: Seq[D1]): Query1[V, D1]
}

class FilterQuery1[V, D1](measure: Measure1[V, D1], nodes1: Seq[D1]) extends Query1[V, D1] {
  private var defaultV: V = _
  private var default1: D1 = _

  def execute() = {
    val predicate = (entry: (D1, V)) => isMatchingNode(entry._1, nodes1, default1)
    measure.data.filter(predicate)
  }

  def select(newNodes1: Seq[D1]) = new FilterQuery1(measure, nodes1 ++ newNodes1)
}

// Two-dimensional

trait MeasureData2[V, D1, D2] extends MeasureData {
  def data: Iterator[((D1, D2), V)]
}

trait Measure2[V, D1, D2] extends MeasureData2[V, D1, D2] with Measure {
  def newQuery(): Query2[V, D1, D2] = new FilteredQuery2[V, D1, D2](this, List(), List())
}

trait Query2[V, D1, D2] extends Query {
  def execute(): Iterator[((D1, D2), V)]
  def select(nodes1: Seq[D1]): Query2[V, D1, D2]
}

class FilteredQuery2[V, D1, D2](measure: Measure2[V, D1, D2], nodes1: Seq[D1], nodes2: Seq[D2]) extends Query2[V, D1, D2] {
  private var defaultV: V = _
  private var default1: D1 = _
  private var default2: D2 = _

  def execute() = {
    val predicate = (entry: ((D1, D2), V)) => isMatchingNode(entry._1._1, nodes1, default1) && isMatchingNode(entry._1._2, nodes2, default2)
    measure.data.filter(predicate)
  }

  def select(newNodes1: Seq[D1]) = new FilteredQuery2(measure, nodes1 ++ newNodes1, nodes2)
}
