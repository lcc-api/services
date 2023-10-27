package com.languagecomputer.services.api.text;

import com.languagecomputer.services.messages.text.TokenSpanIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TokenSpanIdentifierTest {
  @Test
  void testFrom() {
    String spanString = "FWTT3SLZHK_012JCRKQUOVDDH3VPAI6MY9XYLWRV5PNRPPWGN2O5LRKIVMEFNMEL3699GR9Z_IGCO2QA_ATIEKVHNZZT71TP1773:5461:2";
    TokenSpanIdentifier identifier = TokenSpanIdentifier.Companion.from(spanString);
    Assertions.assertEquals("FWTT3SLZHK_012JCRKQUOVDDH3VPAI6MY9XYLWRV5PNRPPWGN2O5LRKIVMEFNMEL3699GR9Z_IGCO2QA_ATIEKVHNZZT71TP1773", identifier.getDocumentID());
    Assertions.assertEquals(5461, identifier.getStartToken());
    Assertions.assertEquals(2, identifier.getTokenLength());
  }
}