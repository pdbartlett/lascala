package pdbartlett.lascala.scala.data

trait Measure {
  type K
  type V
  def aggData: Iterable[(K, V)]
}

trait Measure1[U, T1] extends Measure {
  type V = U
  type D1 = T1
  type K = D1
}

trait Measure2[U, T1, T2] extends Measure {
  type V = U
  type D1 = T1
  type D2 = T2
  type K = (D1, D2)
}

trait PreAggregated extends Measure {
  def data: Iterable[(K, V)]
  def aggData = data
}

trait BulkAggregated2 {
  type V
  type D1
  type D2
  type P = (D1, D2)

  def data: Iterable[(P, V)]
  def dim1: Dimension[D1]
  def dim2: Dimension[D2]
  def newAccumulator(): Accumulator[V]
  
  def aggData = {
    var map = Map[P, Accumulator[V]]()
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
    map.map((entry: (P, Accumulator[V])) => (entry._1, entry._2.value))
  }
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

trait Storage extends Measure {
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

trait InMemoryStorage extends Storage {
  var map: Map[K, V] = Map()
  def store(aggregated: Iterable[(K, V)]) { map ++= aggregated }
  def retrieve() = map
}

trait FactBasedMeasure2[F, V, D1, D2] extends Measure2[V, D1, D2] {
  def factData: Iterable[F]
  def extract(f: F): (K, V)
  def data = factData map extract
}
