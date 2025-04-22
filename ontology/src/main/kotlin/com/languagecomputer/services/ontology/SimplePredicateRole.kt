package com.languagecomputer.services.ontology

import com.google.common.base.MoreObjects
import com.google.common.collect.Lists
import com.google.common.collect.Maps
import java.util.*

/**
 * Represents a role of a predicate type.
 * @author smonahan
 */
class SimplePredicateRole : SimpleOntologyRecord {
  /**
   * @return the name of the predicate this role is associated with
   */
  var predName: String? = null; private set

  constructor(predName: String?, name: String, parent: ConceptIdentifier?, label: String?,
              props: Map<String, Any?>?) : this(predName, name, parent, "", label, ArrayList<ConceptIdentifier>(), props)

  /**
   * @param predName - the name of the predicate
   * @param name - the name of the role
   * @param parent - the parent of the role
   * @param definition - a human-readable definition string
   * @param children - any children of the role. Roles on the predicate don't have children.
   * @param label - a human readable label
   * @param props - various properties of the role
   */
  constructor(predName: String?, name: String, parent: ConceptIdentifier?, definition: String?,
              label: String?, children: Collection<ConceptIdentifier>?, props: Map<String, Any?>?) : super(ConceptClass.PREDICATE_ROLE, name, parent, definition, label, children, props!!) {
    this.predName = predName
  }

  private constructor() : super(ConceptClass.PREDICATE_ROLE, "predicate_roles") {}

  fun copy(): SimplePredicateRole {
    val dupProps: Map<String, Any?> = if(props == null) HashMap() else HashMap<String, Any>(props)
    return SimplePredicateRole(predName, name, parent, label, "", ArrayList(children), dupProps)
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
    val that = other as SimplePredicateRole
    return predName == that.predName
  }

  override fun hashCode(): Int {
    return Objects.hash(super.hashCode(), predName)
  }

  override fun toString(): String {
    return MoreObjects.toStringHelper(SimplePredicateRole::class.java)
            .add("label", label)
            .add("name", name)
            .add("definition", definition)
            .add("parent", parent)
            .add("predName", predName)
            .add("props", getCleanProps()).toString()
  }

  override fun asConceptIdentifier() : ConceptIdentifier {
    if(predName == null) {
      return super.asConceptIdentifier()
    }
    return ConceptIdentifier(predName + "#" + name, label, conceptClass)
  }

}

class SimplePredicateRoleBuilder(private val name: String) {
  constructor(other: SimplePredicateRole) : this(other.name) {
    copy(other)
  }

  private var label: String
  private var parent: ConceptIdentifier? = null
  private var definition: String? = null
  private var predName: String? = null
  private var children: MutableCollection<ConceptIdentifier>
  private var props: MutableMap<String, Any?>

  init {
    label = name
    children = HashSet()
    props = HashMap()
  }

  fun copy(other: SimplePredicateRole) : SimplePredicateRoleBuilder {
    label(other.label)
    definition(other.definition)
    predicateName(other.predName)
    parent(other.parent)
    children(Lists.newArrayList(other.children))
    properties(Maps.newHashMap(other.props))
    return this
  }

  fun label(label: String?): SimplePredicateRoleBuilder {
    this.label = label ?: ""
    return this
  }

  fun definition(definition: String?): SimplePredicateRoleBuilder {
    if(definition == null) return this
    this.definition = definition
    return this
  }

  fun predicateName(predName: String?): SimplePredicateRoleBuilder {
    this.predName = predName
    return this
  }

  fun parent(parent: ConceptIdentifier?): SimplePredicateRoleBuilder {
    this.parent = parent
    return this
  }

  fun children(children: MutableCollection<ConceptIdentifier>): SimplePredicateRoleBuilder {
    this.children = children
    return this
  }

  fun properties(props: MutableMap<String, Any?>): SimplePredicateRoleBuilder {
    this.props = props
    return this
  }

  fun addChild(child: ConceptIdentifier): SimplePredicateRoleBuilder {
    children.add(child)
    return this
  }

  fun setProperty(ontProperty: String, value: Any?) : SimplePredicateRoleBuilder {
    props[ontProperty] = value
    return this
  }

  fun build(): SimplePredicateRole {
    return SimplePredicateRole(
            predName,
            name,
            parent,
            definition,
            label,
            children,
            props.toSortedMap()
    )
  }
}

class SimplePredicateRoleList (
        var list: List<SimplePredicateRole>
) : SimpleRecordListProvider {
  override val simpleList = list
}
