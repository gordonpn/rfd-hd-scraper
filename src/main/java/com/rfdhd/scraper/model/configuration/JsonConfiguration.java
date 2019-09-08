package com.rfdhd.scraper.model.configuration;

public class JsonConfiguration {

    ProdConfiguration prodConfiguration;
    TestConfiguration testConfiguration;

    public ProdConfiguration getProdConfiguration() {
        return prodConfiguration;
    }

    public void setProdConfiguration(ProdConfiguration prodConfiguration) {
        this.prodConfiguration = prodConfiguration;
    }

    public TestConfiguration getTestConfiguration() {
        return testConfiguration;
    }

    public void setTestConfiguration(TestConfiguration testConfiguration) {
        this.testConfiguration = testConfiguration;
    }
}
