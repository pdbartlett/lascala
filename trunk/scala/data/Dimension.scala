package pdbartlett.lascala.scala.data

abstract class Node[T] {
  def value: T
  def parent: Option[Node[T]]
  def isLeaf: Boolean
  def selfAndAncestors: List[Node[T]] = parent match {
    case None => List(this)
    case Some(node) => this :: node.selfAndAncestors
  }
}

abstract class NodeSpec[T] {
  def nodes: Iterable[T]
  def matches(t: T): Boolean
}

class NodeSet[T](ts: Seq[T]) extends NodeSpec[T] {
  def nodes = ts
  def matches(t: T) = ts.contains(t)
}

object NodeSet {
  def apply[T](ts: T*) = new NodeSet[T](ts)
}

class PredicateNodes[T](dim: Dimension[T], pred: T => Boolean) extends NodeSpec[T] {
  def nodes = dim.values
  def matches(t: T) = pred(t)
}

object PredicateNodes {
  def apply[T](dim: Dimension[T], pred: T => Boolean) = new PredicateNodes(dim, pred)
}

trait Dimension[T] {
  def node(t: T): Node[T]
  def defaultValue: T
  def values: Iterable[T]
}

