package com.languagecomputer.services.ontology

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.math.min

/**
 * Structures ontology information as a tree
 *
 * @param record - ontology record
 * @param children - list of children
 */
class OntologyTree(val ci: ConceptIdentifier, val children: List<OntologyTree>) {
  /**
   * the label as a String
   */
  val label: String?

  /**
   * the name (id) as a String
   */
  val name: String?

  /**
   * @return the number of descendants as an int
   */
  var numberOfDescendants = 0

  init {
    label = ci.label
    name = ci.name
    for(child in children) {
      numberOfDescendants += child.numberOfDescendants + 1
    }
  }

  override fun toString(): String {
    val selfString = "${ci.name}: ${ci.label}"
    if (children.isEmpty()) {
      return selfString
    }
    val children = children.flatMap { it.toString().split("\n") }
    val sb = StringBuilder()
    children.forEach {
      sb.append("    ")
      sb.append(it)
      sb.append("\n")
    }
    return selfString + "\n" + sb.toString().trim('\n')
  }

  @JvmOverloads
  fun walk(apply: (parents: List<OntologyTree>, tree: OntologyTree) -> Boolean, threads: Int = 1) {
    if (threads <= 0) {
      throw RuntimeException()
    }
    if (threads == 1) {
      walkSingleThread(apply, ArrayList())
    } else {
      val executorService = Executors.newFixedThreadPool(threads)
      val futures = ConcurrentLinkedQueue<Future<*>>()
      futures.add(executorService.submit { walkParallelized(apply, ArrayList(), executorService, futures) })
      var sleepTime = 2L
      while (futures.isNotEmpty()) {
        Thread.sleep(sleepTime)
        sleepTime = min(100, sleepTime * 2)
        while (futures.peek()?.isDone == true) {
          futures.poll()
        }
      }
      executorService.shutdown()
    }
  }

  private fun walkParallelized(apply: (parents: List<OntologyTree>, tree: OntologyTree) -> Boolean, parents: List<OntologyTree>, executor: ExecutorService, futures: ConcurrentLinkedQueue<Future<*>>) {
    val skipSubtree = apply(parents, this)
    if (skipSubtree) {
      return
    }
    val newParents = ArrayList(parents)
    children.forEach {
      val future: Future<*> = executor.submit { it.walkParallelized(apply, newParents, executor, futures) }
      futures.add(future)
    }
  }

  private fun walkSingleThread(apply: (parents: List<OntologyTree>, tree: OntologyTree) -> Boolean, parents: MutableList<OntologyTree>) {
    val skipSubtree = apply(parents, this)
    if (skipSubtree) {
      return
    }
    parents.add(this)
    children.forEach { it.walkSingleThread(apply, parents) }
    parents.removeLast()
  }
}
