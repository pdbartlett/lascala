package pdbartlett.lascala.scala.data

trait Measure[V] {
  type K
  def aggData: Iterable[(K, V)]
}

trait PreAggregated[V] extends Measure[V] {
  def data: Iterable[(K, V)]
  def aggData = data
}

trait BulkAggregated2[V, D1, D2] extends Measure[V] with Dimensionality2[D1, D2] {
  def data: Iterable[(K, V)]
  def defValue: V
  def combine(v1: V, v2: V) : V
  def d1: Dimension[D1]
  def d2: Dimension[D2]
  def aggData = {
    var map = Map[K,V]()
    for {
      datum <- data
      leaf1 = d1.node(datum._1._1)
      leaf2 = d2.node(datum._1._2)
      node1 <- leaf1.selfAndAncestors
      node2 <- leaf2.selfAndAncestors
    } {
      val key = (node1.value, node2.value)
      val existing = map.getOrElse(key, defValue)
      map += ((key, combine(existing, datum._2)))
    }
    map
  }
}

trait Storage[V] extends Measure[V] {
  type K
  def store(aggregated: Iterable[(K, V)]): Unit
  def retrieve(): Iterable[(K, V)]
  private var stored = false
  abstract override def aggData = {
    if (!stored) {
      store(super.aggData)
      stored = true
    }
    retrieve()
  }
}

trait InMemoryStorage[V] extends Storage[V] {
  var map: Map[K, V] = Map()
  def store(aggregated: Iterable[(K, V)]) { map ++= aggregated }
  def retrieve() = map
}
    
