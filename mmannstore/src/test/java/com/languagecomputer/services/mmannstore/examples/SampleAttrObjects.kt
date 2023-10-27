package com.languagecomputer.services.mmannstore.examples

import com.fasterxml.jackson.annotation.JsonAlias
import com.languagecomputer.services.client.sample.LCCServiceInfo
import com.languagecomputer.services.client.util.CommandLineUtils
import com.languagecomputer.services.client.util.CommandLineUtils.ServiceArgs
import com.languagecomputer.services.mmannstore.*
import com.languagecomputer.services.multimodal.BoundingBox
import com.languagecomputer.services.util.readValue
import picocli.CommandLine.Option
import java.io.File
import kotlin.system.exitProcess

/**
 * @author stuart
 */
// Many of the fields in the sample objects are actually a python object instead of JSON
// so you MUST convert to proper JSON using the convert_json_format.py script before using the objects below.
data class SamplePartAttr(
  val confidence: Double,
  override val bboxSize: List<Double>,
  override val centerLoc: List<Double>,
): SampleCenterBBox

interface SampleCenterBBox {
  val centerLoc: List<Double>
  val bboxSize: List<Double>

  val lccBbox: BoundingBox
    get() = BoundingBox(
      (centerLoc[0] - bboxSize[0] / 2).toInt(),
      (centerLoc[1] - bboxSize[1] / 2).toInt(),
      (centerLoc[0] + bboxSize[0] / 2).toInt(),
      (centerLoc[1] + bboxSize[1] / 2).toInt(),
    )
}

data class SampleAttrObject(
  @field:JsonAlias("class") val clazz: String,
  val surroundingTags: List<String>,
  val llavaDescription: String,
  override val centerLoc: List<Double>,
  override val bboxSize: List<Double>,
  val partAttributes: Map<String, SamplePartAttr>,
): SampleCenterBBox
data class SampleAttrJsonFile(
  val llavaImageDescription: String,
  val imgTags: List<String>,
  val objects: Map<String, SampleAttrObject>,
)

class SampleUploadArgs: ServiceArgs() {
  @Option(names = ["--file-id"])
  lateinit var fileId: String
  @Option(names = ["--input-file"])
  lateinit var jsonFile: String
}

// this program must be run on a file converted to correct JSON via the convert_json_format.py
// example command
// mvn exec:java -Dexec.classpathScope=test -Dexec.mainClass="com.languagecomputer.services.mmannstore.examples.SampleAttrObjectsKt" -Dexec.args="--config-url http://localhost:2185 --token=${ATESSA_TOKEN}"
fun main(stringArgs: Array<String>) {
  val args = CommandLineUtils.parseArgs(SampleUploadArgs(), stringArgs)
  val annotationStore = LCCServiceInfo(args)
    .getService("MM_DOCUMENT_STORE", MultimodalAnnotationStore::class.java)

  val loaded = readValue(File(args.jsonFile)
    .readText(), SampleAttrJsonFile::class.java) ?: exitProcess(1)
  loaded.objects.forEach { (altName, entityInfo) ->
    val entityId = annotationStore.createEntity(args.fileId, CreateEntity(altName))
    annotationStore.addEntityBoundingBox(args.fileId, entityId, CreateEntityBoundingBox(entityInfo.lccBbox))
    // putting confidence of 0.5 since none is supplied
    annotationStore.addEntityAttributeExample(args.fileId, entityId, CreateEntityAttributeExample(entityInfo.clazz, 0.5, AnalyticProvenance.HUMAN))
    entityInfo.partAttributes.forEach { (relType, relInfo) ->
      val headEntityAltName = "${entityId}_$relType"
      val headEntityID = annotationStore.createEntity(args.fileId, CreateEntity(headEntityAltName))
      annotationStore.addEntityBoundingBox(args.fileId, headEntityID, CreateEntityBoundingBox(relInfo.lccBbox))
      annotationStore.addEntityRelationExample(args.fileId, CreateEntityRelationExample(
        headEntityID,// the relations are part-whole so I put the part first (e.g. HEAD door of ORIGINAL)
        entityId,
        relType,
        relInfo.confidence,
        "something"
      ))
    }
  }
}
