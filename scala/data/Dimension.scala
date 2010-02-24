package pdbartlett.lascala.scala.data

trait Dimensionality1[D1] {
  type K = D1
}

trait Dimensionality2[D1, D2] {
  type K = (D1, D2)
}
