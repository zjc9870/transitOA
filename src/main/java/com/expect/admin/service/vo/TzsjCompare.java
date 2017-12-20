/*  1:   */package com.expect.admin.service.vo;
/*  2:   */
/*  3:   */import java.util.Comparator;

/*  4:   */
/*  5:   */public class TzsjCompare<T>
/*  6:   */  implements Comparator<T>{
    /*  7:   *A
    /*  8:   */  public int compare(T a, T b)
/*  9:   */  {

        if (a.getClass().isInstance(new MeetingVo())){
            MeetingVo A = (MeetingVo)a;
            MeetingVo B = (MeetingVo)b;
            if (A.getTzsj() != null) {
/* 11:11 */             return B.getTzsj().compareTo(A.getTzsj());
/* 12:   */         }
        }else{
            DocumentVo A = (DocumentVo)a;
            DocumentVo B = (DocumentVo)b;
            if (A.getTzsj() != null) {
                return B.getTzsj().compareTo(A.getTzsj());
            }
        }
        return -1;
/* 14:   */  }
/* 15:   */}


/* Location:           Z:\spring-boot-admin-0.0.1-SNAPSHOT.jar
 * Qualified Name:     BOOT-INF.classes.com.expect.admin.service.vo.TzsjCompare
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */