package com.languagecomputer.services.messages

/**
 * A property of some system that be represented by a type and the accepted values.
 * @author smonahan
 */
enum class GenericPropertyEnum {

  // values must be true/false
  // checkbox or radio button
  BOOLEAN,
  // value must come from a Java enum
  // dropdown, radio, or other UI can be used
  ENUM,
  // values must come from a Java enum
  // multi-select supported
  ENUMSET,
  // values must be a Double
  // can either be a text field with validation or a numeric field
  DOUBLE,
  // values are a Double with a unit, specified somewhere TBD
  DOUBLE_UNITS,
  // values are an integer
  INTEGER,
  // values are in a list
  LIST,
  // values must be String
  // normal text field
  STRING,
  // values are a temporal expression
  TIMEX;

}

/**
 * <ul>
 * <li> All properties have a name, a value type from the enum.
 * <li> Properties default to not required and editable.
 * <li> Non-editable properties can be displayed to users, but not edited.
 * <li> Required properties are things that the backend expects, so UI forms should not allow submission without these filled in.
 * <li> Default Value is not null is set upon loading the form if the value is not specified.
 * <li> Allowable values restrict the set of options (overriding min/max)
 *   These are strings, so need to cast to the appropriate type using the helpers.
 * <li> Min/max defined the minimum and maximum values for numeric fields, inclusive.
 * These default to Min/Max value for that data type if null.
 * <li> Enum Class String not used by frontend, but used by backend to fill in the
 * allowableValues for ENUM/ENUMSET at runtime, meaning the component is responsive to compile-time changes in the enum.
 * </ul>
 */
data class GenericPropertyType (
  var name : String, // the name of the property
  var valueType : GenericPropertyEnum, // the type of the value
  var required : Boolean = false, // if this is a required property
  var userEditable : Boolean = true, // if the user can edit the property
  var defaultValue: Any? = null,
  var allowableValues: List<String>? = null,
  var min: Number? = null,
  var max: Number? = null,
  val enumClassString: String? = null,
  val description: String? = null
)

data class PropertyTypeList (
    val list: List<GenericPropertyType>
)

data class GenericPropertyValue (
  var type: GenericPropertyType,
  var value: Any,
)
