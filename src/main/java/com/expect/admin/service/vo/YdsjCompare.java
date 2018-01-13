package com.expect.admin.service.vo;

import java.util.Comparator;

public class YdsjCompare<T> implements Comparator<T> {
    public int compare(T a, T b) {
        if (a.getClass().isInstance(new MeetingVo())){
            MeetingVo A = (MeetingVo)a;
            MeetingVo B = (MeetingVo)b;
            if (B.getYdsj() != null) {
                return B.getYdsj().compareTo(A.getYdsj());
            }
        } else{
            DocumentVo A = (DocumentVo)a;
            DocumentVo B = (DocumentVo)b;
            if (B.getYdsj() != null) {
                return B.getYdsj().compareTo(A.getYdsj());
            }
        }
        return -1;
    }
}


/* Location:           Z:\spring-boot-admin-0.0.1-SNAPSHOT.jar
 * Qualified Name:     BOOT-INF.classes.com.expect.admin.service.vo.YdsjCompare
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */