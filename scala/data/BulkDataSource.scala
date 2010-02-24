package pdbartlett.lascala.scala.data

trait BulkDataSource[K, V] {
  def data: Iterable[(K, V)]
}
