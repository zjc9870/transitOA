package com.expect.admin.service.convertor;

import com.expect.admin.data.dataobject.Meeting;
import com.expect.admin.service.vo.MeetingVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;

import java.util.*;

public class MeetingConvertor {
    /**
     * 转换
     * TODO:测试
     */
    public static List<MeetingVo> convert(Set<Meeting> meetingList) {
        List<MeetingVo> meetingVoList = new ArrayList<MeetingVo>();
        if(meetingList == null || meetingList.size() == 0) return meetingVoList;
        for (Meeting meeting : meetingList) {
            meetingVoList.add(new MeetingVo(meeting));
        }
        Collections.sort(meetingVoList, new Comparator<MeetingVo>() {

            @Override
            public int compare(MeetingVo c1, MeetingVo c2) {
                if(StringUtil.isBlank(c1.getHyrq())) return -1;
                if(StringUtil.isBlank(c2.getHyrq())) return 1;
                Date d1 = DateUtil.parse(c1.getHyrq(), DateUtil.fullFormat);
                Date d2 = DateUtil.parse(c2.getHyrq(), DateUtil.fullFormat);
                long dif = DateUtil.getDiffSeconds(d1, d2);
                return (dif > 0) ? 1 :
                        ((dif < 0) ? -1 : 0);
            }
        });
        return meetingVoList;
    }
}
