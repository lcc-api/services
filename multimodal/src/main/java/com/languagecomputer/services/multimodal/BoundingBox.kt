package com.languagecomputer.services.multimodal

/**
 * Object to contain bounding box information.
 * <br />
 * 1-d payloads some fields will be unused.
 *   Text: x1 and x2 are required, the rest should be 0.
 *   Audio: startTimeMillis and endTimeMillis are required.
 *          x1 and x2 are optional. If used, x1/x2 should be the sample number (not milliseconds). Else, set both to 0.
 *          y1 and y2 are unused. They should be set to 0.
 * <br />
 * <br />
 * 2-d payloads will also have unused fields.
 *    Images: bounding box is an actual bounding box.
 *            (x1, y1) is the top left corner, (x2, y2) is the bottom right.
 *            startTimeMillis and endTimeMillis are unused and should be set to 0.
 * <br />
 * <br />
 * 3-d payloads (e.g. Video) use all fields.
 *
 * @author stuart
 */
data class BoundingBox(
  override val x1: Int = 0,
  override val y1: Int = 0,
  override val x2: Int = 0,
  override val y2: Int = 0,
  override val startTimeMillis: Int = 0,
  override val endTimeMillis: Int = 0,
) : Timed, SimpleBoundingBox {

  constructor(x1: Int, y1: Int) : this(x1, y1, 0, 0, 0, 0)
  constructor(x1: Int, y1: Int, x2: Int, y2: Int) : this(x1, y1, x2, y2, 0, 0)

  companion object {
    fun textBoundingBox(startCharacter: Int, endCharacter: Int): BoundingBox {
      return BoundingBox(startCharacter, 0, endCharacter, 0, 0, 0)
    }
  }
}
