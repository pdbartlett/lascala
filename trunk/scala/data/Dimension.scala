package pdbartlett.lascala.scala.data

abstract class Node[T] {
  def value: T
  def parent: Option[Node[T]]
  def selfAndAncestors: List[Node[T]] = parent match {
    case None => List(this)
    case Some(node) => this :: node.selfAndAncestors
  }
}

trait Dimension[T] {
  def node(t: T): Node[T]
}

