package com.rfdhd.scraper.services;

import com.rfdhd.scraper.model.Thread;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Batch {

    public Batch() {
        Connection connection = new Connection();
    }

    public void readThreads(Elements threads) {
        List<Thread> threadList = new ArrayList<>();

        Elements list = threads.select("li");
        for (Element line : list) {
            Thread thread = new Thread();
//            Elements line = thread.select("li");
            String id = line.attr("data-thread-id");
            if (!id.equals("")) {
                thread.setThreadID(id);
                threadList.add(thread);
            }
//            System.out.println(thread.getElementsByClass("topictitle"));

        }
        threadList.forEach(thread -> {
            System.out.println(thread.getThreadID());
        });
    }
}
