package com.inzent.commonMethod.common;

import java.util.HashMap;
import java.util.Map;

public class Pagination {

    public static Map<String, Object> setPage(Integer pageNum){
        Map<String, Object> page = new HashMap<>();
        if(pageNum != null){
            int pageStart = (pageNum-1) * Consts.PAGE_SIZE + 1;
            int pageEnd = pageStart + (Consts.PAGE_SIZE -1);

            page.put(Consts.PAGE_START, pageStart);
            page.put(Consts.PAGE_END, pageEnd);
        }
        return  page;
    }

    public static Map<String, Object> setModalPage(Integer pageNum){
        Map<String, Object> page = new HashMap<>();
        if(pageNum != null){
            int pageStart = (pageNum-1) * Consts.MODAL_PAGE_SIZE + 1;
            int pageEnd = pageStart + (Consts.MODAL_PAGE_SIZE -1);

            page.put(Consts.PAGE_START, pageStart);
            page.put(Consts.PAGE_END, pageEnd);
        }
        return  page;
    }
}
