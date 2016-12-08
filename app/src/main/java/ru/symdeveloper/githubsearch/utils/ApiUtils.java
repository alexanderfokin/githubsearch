package ru.symdeveloper.githubsearch.utils;

import java.util.concurrent.Executors;

import rx.Scheduler;
import rx.schedulers.Schedulers;

public class ApiUtils {

    private static final Scheduler NETWORK_SCHEDULER = Schedulers.from(Executors.newFixedThreadPool(4));
    private static final Scheduler LOCAL_IO_SCHEDULER = Schedulers.from(Executors.newFixedThreadPool(1));

    public static Scheduler networkScheduler() {
        return NETWORK_SCHEDULER;
    }
    public static Scheduler localIoScheduler() {
        return LOCAL_IO_SCHEDULER;
    }
}
