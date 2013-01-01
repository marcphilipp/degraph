package de.schauderhaft.degraph.graph

import scalax.collection.mutable.{ Graph => SGraph }
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._
import scalax.collection.edge.Implicits._
import scalax.collection.edge.LkDiEdge

/**
 * a special graph for gathering and organizing dependencies in a hirachical fashion.
 *
 * Argument is a category which is by default the identity. A category is a function that returns an outer node for any node and the node itself if no out node is available
 */
class Graph(category: AnyRef => AnyRef = (x) => x,
    filter: AnyRef => Boolean = _ => true,
    edgeFilter: ((AnyRef, AnyRef)) => Boolean = _ => true) {

    private val internalGraph = SGraph[AnyRef, LkDiEdge]()

    def topNodes: Set[AnyRef] = {
        val g = internalGraph
        g.nodes.filter(_.incoming.forall(_.label != "contains")).map(_.value).toSet
    }
    def contentsOf(group: AnyRef): Set[AnyRef] =
        for {
            n <- internalGraph.find(group).toSet[internalGraph.NodeT]
            e <- n.outgoing
            if (e.label == "contains")
        } yield e._2.value

    def connectionsOf(node: AnyRef): Set[AnyRef] =
        for {
            n <- internalGraph.find(node).toSet[internalGraph.NodeT]
            e <- n.outgoing
            if (e.label == "references")
        } yield e._2.value

    def add(node: AnyRef) = if (filter(node)) unfilteredAdd(node)

    private def unfilteredAdd(node: AnyRef) {
        val cat = category(node)
        if (cat == node) {
            internalGraph += node
        } else {
            addNodeToCategory(node, cat)
            unfilteredAdd(cat)
        }
    }

    def connect(a: AnyRef, b: AnyRef) {
        addEdge(a, b)
        add(a)
        add(b)
    }

    def allNodes: Set[AnyRef] = internalGraph.nodes.map(_.value).toSet

    private def addEdge(a: AnyRef, b: AnyRef) {
        implicit val factory = scalax.collection.edge.LkDiEdge
        if (filter(a) && filter(b) && edgeFilter(a, b))
            internalGraph.addLEdge(a, b)("references")
    }

    private def addNodeToCategory(node: AnyRef, cat: AnyRef) = {
        implicit val factory = scalax.collection.edge.LkDiEdge
        internalGraph.addLEdge(cat, node)("contains")
    }
}