package com.languagecomputer.services.ontology

import com.google.common.base.MoreObjects
import java.util.*
import java.util.stream.Collectors


/**
 * Class representing the JSON data for a predicate record
 */
class SimplePredicateRecord : SimpleOntologyRecord {
  var roles: List<SimplePredicateRole>? = null; private set

  constructor(name: String, parent: ConceptIdentifier?, definition: String?, label: String?,
              children: Collection<ConceptIdentifier>?, map: Map<String, Any?>?, roles: Collection<SimplePredicateRole>?)
          : super(ConceptClass.PREDICATE_TRIGGER, name, parent, definition, label, children, map!!) {
    this.roles = ArrayList(roles)
  }

  private constructor() : super(ConceptClass.PREDICATE_TRIGGER, "predicates") {}

  fun copy(): SimplePredicateRecord {
    val dupRoles = roles!!.stream().map { obj: SimplePredicateRole -> obj.copy() }.collect(Collectors.toList())
    val dupChildren: Set<ConceptIdentifier> = if(children == null) HashSet() else HashSet<ConceptIdentifier>(children)
    val dupProps: Map<String, Any?> = if(props == null) HashMap() else HashMap<String, Any>(props)
    return SimplePredicateRecord(name, parent, definition, label, dupChildren, dupProps, dupRoles)
  }

  override fun toString(): String {
    return MoreObjects.toStringHelper(SimplePredicateRecord::class.java)
            .addValue(super.toString()).add("roles", roles).toString()
  }

  override fun equals(other: Any?): Boolean {
    if(this === other) {
      return true
    }
    if(other == null || javaClass != other.javaClass) {
      return false
    }
    if(!super.equals(other)) {
      return false
    }
    val that = other as SimplePredicateRecord
    return roles == that.roles
  }

  override fun hashCode(): Int {
    return Objects.hash(super.hashCode(), roles)
  }
}

class SimplePredicateRecordList (
   var list: List<SimplePredicateRecord>
) : SimpleRecordListProvider {
  override val simpleList = list
}
