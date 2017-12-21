/*  1:   */package com.expect.admin.service.vo;
/*  2:   */
/*  3:   */import java.util.Comparator;

/*  4:   */
/*  7:   */public class YdsjCompare<T>
/*  8:   */  implements Comparator<T>
/*  9:   */{
    /* 10:   */  public int compare(T a, T b)
/* 11:   */  {
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
/* 15:15 */    return -1;
/* 16:   */  }
/* 17:   */}


/* Location:           Z:\spring-boot-admin-0.0.1-SNAPSHOT.jar
 * Qualified Name:     BOOT-INF.classes.com.expect.admin.service.vo.YdsjCompare
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */