package pdbartlett.lascala.scala.data

trait Measure[V] {
  type K
  def aggData: Iterable[(K, V)]
}

trait PreAggregated[V] extends Measure[V] {
  def data: Iterable[(K, V)]
  def aggData = data
}
