package com.bar.gestiondesfichier.document.projection;

/**
 * CommThirdParty projection for commercial third party records
 */
public interface CommThirdPartyProjection extends BaseDocumentRelatedProjection {
    String getThirdPartyName();
    String getServiceType();
    String getContactInfo();
    String getContractTerms();
    String getServiceLevel();
}