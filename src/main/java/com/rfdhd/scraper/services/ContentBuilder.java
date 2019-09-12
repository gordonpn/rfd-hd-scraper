package com.rfdhd.scraper.services;

import com.rfdhd.scraper.report.EmailContent;
import org.apache.commons.io.FileUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Locale;

public class ContentBuilder {

    private EmailContent emailContent;

    public ContentBuilder(EmailContent emailContent) {
        this.emailContent = emailContent;
    }

    public void write() throws IOException {
        String content = getHtmlContent();
        File file = new File(emailContent.getReportName());
        FileUtils.writeStringToFile(file, content, Charset.defaultCharset());
    }

    public String getHtmlContent() {
        ClassLoaderTemplateResolver resolver = getClassLoaderTemplateResolver();

        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        IContext context = new Context(Locale.getDefault(), emailContent.getContent());

        StringWriter stringWriter = new StringWriter();
        engine.process(emailContent.getTemplate(), context, stringWriter);

        return stringWriter.toString();
    }

    private ClassLoaderTemplateResolver getClassLoaderTemplateResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();

        resolver.setPrefix("thymeleaf/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);

        return resolver;
    }

}
