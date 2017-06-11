/*  1:   */package com.expect.admin.service.vo;
/*  2:   */
/*  3:   */import java.util.Comparator;
/*  4:   */
/*  5:   */public class TzsjCompare
/*  6:   */  implements Comparator<DocumentVo>
/*  7:   */{
/*  8:   */  public int compare(DocumentVo a, DocumentVo b)
/*  9:   */  {
/* 10:10 */    if (b.getTzsj() != null) {
/* 11:11 */      return b.getTzsj().compareTo(a.getTzsj());
/* 12:   */    }
/* 13:13 */    return -1;
/* 14:   */  }
/* 15:   */}


/* Location:           Z:\spring-boot-admin-0.0.1-SNAPSHOT.jar
 * Qualified Name:     BOOT-INF.classes.com.expect.admin.service.vo.TzsjCompare
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */