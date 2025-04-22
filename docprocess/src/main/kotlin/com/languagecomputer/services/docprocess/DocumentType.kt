package com.languagecomputer.services.docprocess

/**
 * Possible types for the underlying string in the message
 */
enum class DocumentType {
    LCC_XML,  // Proprietary internal LCC XML format
    LCC_XML_ZIPPED,  // compressed LCC_XML
    LCC_XML_URL,  //Processsed like a URL, but expects LCC_XML to be returned.
    RAW_TEXT,  // Raw text in the payload
    MULTI_MEDIA_JSON,  // JSON of a MultiMediaJson object used for MultiMedia documents.
    HTML,  // HTML in the payload.  The URL is where this originally came from
    ATEA,  // Used for integration with AFRL ATEA format
    URL,  // There is no payload, the payload should be downloaded from the URL
    PDF,  // the payload is in PDF
    DOCID,  // we are only passing in the docid and not the payload
    CDR,  // Causal Exploration file format
    XML, PUBMED_XML, MS_WORD, MS_EXCEL, MS_POWERPOINT, SYSTEM
}
