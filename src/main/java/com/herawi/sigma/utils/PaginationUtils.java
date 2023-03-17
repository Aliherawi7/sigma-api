package com.herawi.sigma.utils;

public class PaginationUtils {

    public static Paginate getStartAndEndPoint(int listSize, int offset, int pageSize){
        int start, end;
        end = (offset * pageSize) < listSize ? (offset * pageSize) : listSize;
        start = (end - pageSize) < 0 ? 0 : end - pageSize;
        Paginate paginate = new Paginate(start, end);

        return paginate;
    }

    public static class Paginate{
        public final int start;
        public final int end;
        public Paginate(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

}
