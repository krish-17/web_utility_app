package com.wau.clock;

import java.util.ArrayList;
import java.util.List;

public class MockClockStoreAPIImpl implements ClockStoreAPI {
    @Override
    public boolean createClock(ClockMetaAPIImpl clockMeta) {
        return clockMeta.getUserId().equalsIgnoreCase("group-6");
    }

    @Override
    public List<ClockMetaAPIImpl> getClocksByUserId(String userId) {
        List<ClockMetaAPIImpl> clockMetaList = new ArrayList<>();
        if (userId.equalsIgnoreCase("group-6")) {
            ClockMetaAPIImpl clockMeta = new ClockMetaAPIImpl("5308653086", "group-6",
                    "test clock", 2);
            clockMetaList.add(clockMeta);
        }
        return clockMetaList;
    }

    @Override
    public ClockMetaAPIImpl getClockByClockId(String clockId) {
        if (clockId.equalsIgnoreCase("5308653086")) {
            return new ClockMetaAPIImpl("5308653086", "group-6", "test clock", 1);
        }
        if (clockId.equalsIgnoreCase("5308653085")) {
            return new ClockMetaAPIImpl("5308653085", "group-6", "test clock", 100);
        }
        return null;
    }

    @Override
    public boolean modifyTimeZone(String userId, String clockId, int timeZoneId) {
        return clockId.equalsIgnoreCase("5308653086") &&
                userId.equalsIgnoreCase("group-6") &&
                timeZoneId == 2;
    }
}
