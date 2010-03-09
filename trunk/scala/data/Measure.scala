package pdbartlett.lascala.scala.data

trait Measure[V] {
  type K
  def aggData: Iterable[(K, V)]
}

trait PreAggregated[V] extends Measure[V] {
  def data: Iterable[(K, V)]
  def aggData = data
}

trait Accumulator[V] {
  def value: V
  def +(v: V): Accumulator[V]
}

trait IntsAggregatedBySum {
  def newAccumulator() = new IntSummer(0)
  class IntSummer(val value: Int) extends Accumulator[Int] {
    def +(i: Int) = new IntSummer(value + i)
  }
}

trait BulkAggregated2[V, D1, D2] extends Measure[V] with Dimensionality2[D1, D2] {
  def data: Iterable[(K, V)]
  def dim1: Dimension[D1]
  def dim2: Dimension[D2]
  def newAccumulator(): Accumulator[V]
  
  def aggData = {
    var map = Map[K, Accumulator[V]]()
    for {
      datum <- data
      leaf1 = dim1.node(datum._1._1)
      leaf2 = dim2.node(datum._1._2)
      node1 <- leaf1.selfAndAncestors
      node2 <- leaf2.selfAndAncestors
    } {
      val key = (node1.value, node2.value)
      val existing = map.getOrElse(key, newAccumulator())
      map += ((key, existing + datum._2))
    }
    map.map((entry: (K, Accumulator[V])) => (entry._1, entry._2.value))
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
