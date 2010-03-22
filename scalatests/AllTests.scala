package pdbartlett.lascala.scala

import pdbartlett.lascala.scala.data._

import org.scalatest.SuperSuite

class AllTests extends SuperSuite(List(
    new BulkDataSourceTest,
    new MeasureTest,
    new QueryTest
)) {}
