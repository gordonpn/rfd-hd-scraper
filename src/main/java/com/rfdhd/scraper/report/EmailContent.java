package com.rfdhd.scraper.report;

import java.util.Map;

public interface EmailContent {

    String getReportName();

    String getTemplate();

    Map<String, Object> getContent();

}
