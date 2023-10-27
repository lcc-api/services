package com.languagecomputer.services.multimodal;

import com.languagecomputer.services.util.JacksonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author stuart
 */
class MultiModalSerializationTests {
    @Test
    void boundingBox() {
        String json = Assertions.assertDoesNotThrow(() -> JacksonUtil.toJson(new BoundingBox(
          1,
          2,
          3,
          4
        )));
        Assertions.assertDoesNotThrow(() -> JacksonUtil.fromJson(json, BoundingBox.class));
    }

    @Test
    void richText() {
        String json = Assertions.assertDoesNotThrow(() -> JacksonUtil.toJson(new RichText(
          "",
          "2",
          "3"

        )));
        Assertions.assertDoesNotThrow(() -> JacksonUtil.fromJson(
          json,
          RichText.class
        ));
        Assertions.assertThrows(NullPointerException.class, () -> new RichText("", "", null, ""));
    }

    @Test
    void richTextSpan() {
        Map<String, String> foo = new HashMap<>();
        Assertions.assertThrows(NullPointerException.class, () -> new RichTextSpan(
          null,
          0,
          1,
          null, null, 0,
          null,
          foo,
                null
        ));
        String json = Assertions.assertDoesNotThrow(() -> JacksonUtil.toJson(new RichTextSpan(
          "",
          0,
          1,
          null, null, 0,
          null,
          foo,
                null
        )));
        Assertions.assertDoesNotThrow(() -> JacksonUtil.fromJson(json, RichTextSpan.class));
    }
}
