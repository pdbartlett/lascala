package pdbartlett.lascala.scala.data

import org.scalatest.FunSuite

class QueryTest extends FunSuite with DataTestUtils {

  test("query_nodeSet") {
    val expected = List((("UK", null), 1), (("US", null), 2))
    assertPairIterablesEqualIgnoreOrder(expected, TestPreAggMeasure2.query(Some(NodeSet("UK", "US", "UAE")), None))
  }

  test("query_predicateSet") {
    val expected = List((("UK", null), 1), (("US", null), 2))
    val isLeaf = (node: String) => CountriesDim.node(node).isLeaf
    assertPairIterablesEqualIgnoreOrder(expected, TestPreAggMeasure2.query(Some(PredicateNodes(CountriesDim, isLeaf)), None))
  }

  test("lookupQuery_nodeSet") {
    val expected = List((("UK", null), Some(1)), (("US", null), Some(2)), (("UAE", null), None))
    assertPairIterablesEqualIgnoreOrder(expected, TestStorageMeasure.lookupQuery(Some(NodeSet("UK", "US", "UAE")), None))
  }
}
