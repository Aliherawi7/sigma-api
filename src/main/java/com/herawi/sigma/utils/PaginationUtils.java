package com.herawi.sigma.utils;

public class PaginationUtils {

    public static Paginate getStartAndEndPoint(int listSize, int offset, int pageSize){
        int start, end;

        int calc = offset * pageSize;

        end = Math.min((offset * pageSize), listSize);
        if(calc - pageSize > listSize){
            return null;
        }
        start = Math.max((pageSize * (offset-1)), 0);
        return new Paginate(start, end);
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
