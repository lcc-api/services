package com.languagecomputer.services.docprocess.multimedia;

import java.util.List;

/**
 * Object used to create payload for document processing for MultiMedia documents.
 */
public record MultiMediaJson(String title, List<MultiMediaSection> sections) {}
