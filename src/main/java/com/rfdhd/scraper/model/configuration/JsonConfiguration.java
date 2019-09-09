package com.rfdhd.scraper.model.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonConfiguration {

    @SerializedName("ProdConfiguration")
    @Expose
    private ProdConfiguration prodConfiguration;
    @SerializedName("TestConfiguration")
    @Expose
    private TestConfiguration testConfiguration;

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
