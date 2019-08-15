package com.rfdhd.scraper.utility;

import com.rfdhd.scraper.model.ThreadInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum Calculate {

    VIEWS {
        public int getMedian(Map<String, ThreadInfo> threadMap) {
            List<Integer> views = new ArrayList<>();
            threadMap.values().forEach(threadInfo -> views.add(threadInfo.getViewsInt()));
            views.sort(Integer::compareTo);
            return views.get(views.size() / 2);
        }

        public int getAverage(Map<String, ThreadInfo> threadMap) {
            int sum = threadMap.values().stream().mapToInt(ThreadInfo::getViewsInt).sum();
            return sum / threadMap.size();
        }
    },

    POSTS {
        public int getMedian(Map<String, ThreadInfo> threadMap) {
            List<Integer> posts = new ArrayList<>();
            threadMap.values().forEach(threadInfo -> posts.add(threadInfo.getPostsInt()));
            posts.sort(Integer::compareTo);
            return posts.get(posts.size() / 2);
        }

        public int getAverage(Map<String, ThreadInfo> threadMap) {
            int sum = threadMap.values().stream().mapToInt(ThreadInfo::getPostsInt).sum();
            return sum / threadMap.size();
        }
    },

    VOTES {
        public int getMedian(Map<String, ThreadInfo> threadMap) {
            List<Integer> votes = new ArrayList<>();
            threadMap.values().forEach(threadInfo -> votes.add(threadInfo.getVotesInt()));
            votes.sort(Integer::compareTo);
            return votes.get(votes.size() / 2);
        }

        public int getAverage(Map<String, ThreadInfo> threadMap) {
            int sum = threadMap.values().stream().mapToInt(ThreadInfo::getVotesInt).sum();
            return sum / threadMap.size();
        }
    };

    public abstract int getMedian(Map<String, ThreadInfo> threadMap);

    public abstract int getAverage(Map<String, ThreadInfo> threadMap);
}