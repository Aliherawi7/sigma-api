package com.herawi.sigma.dto;

import java.util.Collection;
import java.util.List;

public class PageContainerDTO<T> {
    private long recordCount;
    private Collection<T> records;

    public PageContainerDTO(long recordCount, Collection<T> records) {
        this.recordCount = recordCount;
        this.records = records;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public Collection<T> getRecords() {
        return records;
    }

    public void setRecords(Collection<T> records) {
        this.records = records;
    }
}
