package com.languagecomputer.services.ontology

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.google.common.collect.Maps
import java.io.Serializable
import java.util.*

/**
 * Abstract class representing the generic JSON data for an ontology record
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.EXISTING_PROPERTY,
              property = "conceptClass")
@JsonSubTypes(JsonSubTypes.Type(name = "PREDICATE_TRIGGER", value = SimplePredicateRecord::class),
              JsonSubTypes.Type(name = "ENTITY", value = SimpleEntityRecord::class),
              JsonSubTypes.Type(name = "ATTRIBUTE", value = SimpleAttributeRecord::class),
              JsonSubTypes.Type(name = "TOPIC", value = SimpleTopicRecord::class),
              JsonSubTypes.Type(name = "ZONE", value = SimpleZoneRecord::class),
              JsonSubTypes.Type(name = "PROPERTY", value = SimplePropertyRecord::class),
              JsonSubTypes.Type(name = "META_DATA", value = SimpleMetadataRecord::class),
              JsonSubTypes.Type(name = "PREDICATE_ROLE", value = SimplePredicateRole::class))
abstract class SimpleOntologyRecord : Serializable {
  var conceptClass: ConceptClass; private set
  var name: String; private set
  var parent: ConceptIdentifier? = null; private set
  var definition: String? = null; private set
  var label: String? = null; private set
  var children: List<ConceptIdentifier>? = null; private set
  var other_children: MutableList<ConceptIdentifier> = ArrayList(); private set
  var other_parents: MutableList<ConceptIdentifier> = ArrayList(); private set

  var props: Map<String, Any?>? = null; private set

  protected constructor(conceptClass: ConceptClass, name: String,
                        parent: ConceptIdentifier?, definition: String?,
                        label: String?, children: Collection<ConceptIdentifier>?,
                        map: Map<String, Any?>) {
    this.conceptClass = conceptClass
    this.name = name
    this.parent = parent
    this.definition = definition
    this.label = label ?: name
    this.children = ArrayList(children)
    props = Maps.newHashMap(map)?.toSortedMap()
    if(map.containsKey(SimpleOntologyProperty.OTHER_CHILDREN.name)) {
      @Suppress("UNCHECKED_CAST")
      other_children.addAll(map[SimpleOntologyProperty.OTHER_CHILDREN.name] as Collection<ConceptIdentifier>)
    }
    if(map.containsKey(SimpleOntologyProperty.OTHER_PARENTS.name)) {
      @Suppress("UNCHECKED_CAST")
      other_parents.addAll(map[SimpleOntologyProperty.OTHER_PARENTS.name] as Collection<ConceptIdentifier>)
    }
  }

  protected constructor(conceptClass: ConceptClass, name: String) {
    this.conceptClass = conceptClass
    this.name = name
  }

  open fun asConceptIdentifier(): ConceptIdentifier {
    return ConceptIdentifier(name, label, conceptClass)
  }

  protected fun getCleanProps(): SortedMap<String, Any?>? {
    // This is here because the toString method is already displaying these
    val sorted = props?.toSortedMap()
    sorted?.remove(SimpleOntologyProperty.CHILD_IDS.name)
    sorted?.remove(SimpleOntologyProperty.DEFINITION.name)
    sorted?.remove(SimpleOntologyProperty.NAME.name)
    sorted?.remove(SimpleOntologyProperty.PARENT_ID.name)
    sorted?.remove(SimpleOntologyProperty.LABEL.name)
    return sorted
  }

  override fun equals(other: Any?): Boolean {
    // make sure the subclass is right
    return if(other!!.javaClass != javaClass) {
      false
    } else (other as SimpleOntologyRecord?)!!.name == name
  }

  override fun hashCode(): Int {
    return name.hashCode()
  }

  override fun toString(): String {
    val sorted = getCleanProps()
    return "AbstractSimpleRecord{" +
            "children=" + children?.sortedWith(compareBy({ it.name })) +
            ", name='" + name + '\'' +
            ", parent='" + parent + '\'' +
            ", definition='" + definition + '\'' +
            ", label='" + label + '\'' +
            ", props=" + sorted +
            '}'
  }

  companion object {
    fun <T : SimpleOntologyRecord> castList(toClass: Class<T>, input: SimpleRecordList): List<T> {
      return input.simpleList.map{toClass.cast(it)}
    }
  }
}

class SimpleRecordList(
    var list: List<SimpleOntologyRecord>
) : SimpleRecordListProvider {
  override val simpleList = list
}

interface SimpleRecordListProvider {
    val simpleList: List<SimpleOntologyRecord>
}
