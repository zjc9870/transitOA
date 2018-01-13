package com.expect.admin.service.vo;

import java.util.Comparator;

public class TzsjCompare<T> implements Comparator<T>{
   public int compare(T a, T b) {
       if (a.getClass().isInstance(new MeetingVo())){
            MeetingVo A = (MeetingVo)a;
            MeetingVo B = (MeetingVo)b;
            if (A.getTzsj() != null) {
                return B.getTzsj().compareTo(A.getTzsj());
            }
       }else{
            DocumentVo A = (DocumentVo)a;
            DocumentVo B = (DocumentVo)b;
            if (A.getTzsj() != null) {
                return B.getTzsj().compareTo(A.getTzsj());
            }
       }
       return -1;
   }
}


/* Location:           Z:\spring-boot-admin-0.0.1-SNAPSHOT.jar
 * Qualified Name:     BOOT-INF.classes.com.expect.admin.service.vo.TzsjCompare
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */