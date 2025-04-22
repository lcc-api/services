package com.languagecomputer.services.docprocess.multimedia;

/**
 * Section of MultiMedia document.
 * @param body - Text body (or an empty string) of the section. This is not used for an IMAGE section. If you need IMAGE text, use a CAPTION instead.
 * @param type - type of section. Most are some type of text, but IMAGE is for when an image should be embedded in the doc.
 * @param id - ID of the MultiModal document associated with the section. for an IMAGE this should also be the file ID of the image in the filestore.
 */
public record MultiMediaSection(String body, SectionType type, String id) {
    public enum SectionType {
        HEADER,
        TEXT,
        IMAGE,
        CAPTION,
    }
}
