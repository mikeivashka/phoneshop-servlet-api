package com.es.phoneshop.security.impl;

import com.es.phoneshop.security.DosProtectionService;
import com.es.phoneshop.security.RequestFrequencyController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DosProtectionServiceImpl implements DosProtectionService {

    private static final long THRESHOLD = 100;

    private final Map<String, RequestFrequencyController> requestsStatistics = new ConcurrentHashMap<>();

    private DosProtectionServiceImpl() {
    }

    public static DosProtectionServiceImpl getInstance() {
        return DosProtectionServiceImpl.LazySingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public boolean isAllowed(String ip) {
        RequestFrequencyController count = requestsStatistics.computeIfAbsent(ip, k ->
                new RequestFrequencyController(THRESHOLD));
        count.addVisit();
        return count.isAllowed();
    }

    private static class LazySingletonHolder {
        public static final DosProtectionServiceImpl HOLDER_INSTANCE = new DosProtectionServiceImpl();
    }
}
