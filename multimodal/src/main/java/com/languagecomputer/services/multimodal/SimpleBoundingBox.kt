package com.languagecomputer.services.multimodal

import kotlin.math.abs

/**
 * Interface for simple bounding box.
 * Coordinates are top left and bottom right points so it should hold that
 * x1 <= x2
 * and
 * y1 <= y2
 * <br/>
 * If a dimension is unused, both values should be set to 0
 *
 * @author stuart
 */
interface SimpleBoundingBox {
  val x1: Int
  val x2: Int
  val y1: Int
  val y2: Int

  val width: Int
    get() = abs(x1 - x2)
  val height: Int
    get() = abs(y1 - y2)

  fun area(): Int = width * height
}
