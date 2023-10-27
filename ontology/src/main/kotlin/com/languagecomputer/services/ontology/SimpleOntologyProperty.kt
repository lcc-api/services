package com.languagecomputer.services.ontology

import java.io.Serializable

// a specific property of an ontology concept.
open class SimpleOntologyProperty : Serializable {
    var name: String? = null
    var label: String? = null
    var valueType: PropertyClass? = null
    var definition: String? = null

    companion object {
        var NAME = SimpleOntologyProperty("NAME", "NAME", PropertyClass.STRING, "The permanent name of the ontology record.")
        var LABEL = SimpleOntologyProperty("LABEL", "LABEL", PropertyClass.STRING, "The human-readable label associated with the record.")
        var DEFINITION = SimpleOntologyProperty("DEFINITION", "DEFINITION", PropertyClass.STRING, "The definition of the ontology concept.")
        var PARENT_ID = SimpleOntologyProperty("PARENT_ID", "PARENT_ID", PropertyClass.STRING, "The parent of the ontology record.")
        var CHILD_IDS = SimpleOntologyProperty("CHILD_IDS", "CHILD_IDS", PropertyClass.COLLECTION, "The children of the ontology record.")
        var OTHER_CHILDREN = SimpleOntologyProperty("OTHER_CHILDREN", "OTHER_CHILDREN", PropertyClass.COLLECTION, "The \"other\" children of the ontology record.")
        var OTHER_PARENTS = SimpleOntologyProperty("OTHER_PARENTS", "OTHER_PARENTS", PropertyClass.COLLECTION, "The \"other\" parents of the contology record.")
    }

    constructor(name: String, label: String?, valueType: PropertyClass,
                          definition: String?) {
        this.name = name
        this.label = label ?: name
        this.valueType = valueType
        this.definition = definition
    }

    override fun equals(other: Any?): Boolean {
        // make sure the subclass is right
        return if(other!!.javaClass != javaClass) {
            false
        } else (other as SimpleOntologyProperty?)!!.name == name
    }

    override fun hashCode(): Int {
        return if(name != null) name.hashCode() else 0
    }

    override fun toString(): String {
        return "com.languagecomputer.services.api.messages.ontology.SimpleOntologyProperty{" +
                "name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", valueType='" + valueType + '\'' +
                ", definition='" + definition + '\'' +
                '}'
    }
}

class SimpleOntologyPropertyList(val list: Collection<SimpleOntologyProperty>)
