package com.expect.admin.service.convertor;

import com.expect.admin.data.dataobject.Meetingroom;
import com.expect.admin.service.vo.MeetingroomVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableButtonFactory;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by twinkleStar on 2017/10/22.
 */
public class MeetingroomConvertor {

    /**
     * vo to do
     */
    public static Meetingroom convert(MeetingroomVo meetingroomVo) {
        Meetingroom meetingroom = new Meetingroom();
        BeanUtils.copyProperties(meetingroomVo, meetingroom);
        return meetingroom;
    }

    /**
     * vo to do
     */
    public static void convert(MeetingroomVo meetingroomVo, Meetingroom meetingroom) {
        meetingroom.setId(meetingroomVo.getId());
        meetingroom.setHysname(meetingroomVo.getHysname());
        meetingroom.setLocation(meetingroomVo.getLocation());
        meetingroom.setCapacity(meetingroomVo.getCapacity());
        meetingroom.setDescription(meetingroomVo.getDescription());
        meetingroom.setHydd(meetingroomVo.getHydd());
    }

    /**
     * do to vo
     */
    public static MeetingroomVo convert(Meetingroom meetingroom) {
        MeetingroomVo meetingroomVo = new MeetingroomVo();
        if (meetingroom == null) {
            return meetingroomVo;
        }
//        BeanUtils.copyProperties(meetingroom, meetingroomVo);

        meetingroomVo.setId(meetingroom.getId());
        meetingroomVo.setHysname(meetingroom.getHysname());
        meetingroomVo.setLocation(meetingroom.getLocation());
        meetingroomVo.setCapacity(meetingroom.getCapacity());
        meetingroomVo.setDescription(meetingroom.getDescription());
        meetingroomVo.setHydd(meetingroom.getHydd());
        return meetingroomVo;
    }

//    /**
//     * do to vo
//     */
//    public static MeetingroomVo convert2(Meetingroom meetingroom) {
//        MeetingroomVo meetingroomVo = new MeetingroomVo();
//        if (meetingroom == null) {
//            return meetingroomVo;
//        }
//        BeanUtils.copyProperties(meetingroom, meetingroom);
//        meetingroomVo.setId(meetingroom.getId());
//        meetingroomVo.setHysname(meetingroom.getHysname());
//        meetingroomVo.setLocation(meetingroom.getLocation());
//        meetingroomVo.setCapacity(meetingroom.getCapacity());
//        meetingroomVo.setDescription(meetingroom.getDescription());
//        meetingroomVo.setDepartment(meetingroom.getDepartment());
//        return meetingroomVo;
//    }

    /**
     * do to vo
     */
    public static MeetingroomVo convert3(Meetingroom meetingroom) {
        MeetingroomVo meetingroomVo = new MeetingroomVo();
        if (meetingroom == null) {
            return meetingroomVo;
        }
        meetingroomVo.setId(meetingroom.getId());
        meetingroomVo.setHysname(meetingroom.getHysname());
        meetingroomVo.setLocation(meetingroom.getLocation());
        meetingroomVo.setCapacity(meetingroom.getCapacity());
        meetingroomVo.setDescription(meetingroom.getDescription());
        meetingroomVo.setHydd(meetingroom.getHydd());
        return meetingroomVo;
    }




    /**
     * dos to vos
     */
    public static List<MeetingroomVo> convert(Collection<Meetingroom> meetingrooms) {
        List<MeetingroomVo> meetingroomVos = new ArrayList<>();
        for (Meetingroom meetingroom : meetingrooms) {
            MeetingroomVo meetingroomVo = convert(meetingroom);
            meetingroomVos.add(meetingroomVo);
        }
        return meetingroomVos;
    }
//
//    /**
//     * vos to sov
//     */
//    public static SelectOptionVo convertSov(List<MeetingroomVo> meetingroomVos, MeetingroomVo checkedMeetingroom) {
//        SelectOptionVo sov = new SelectOptionVo();
//        if (!CollectionUtils.isEmpty(meetingroomVos)) {
//            sov.addOption("", "设置为上级部门");
//            for (MeetingroomVo meetingroomVo : meetingroomVos) {
//                sov.addOption(meetingroomVo.getId(), meetingroomVo.getHysname());
//            }
//        }
//        return sov;
//    }

    /**
     * do to dtrv
     */
    public static void convertDtrv(DataTableRowVo mrrv, Meetingroom meetingroom) {
        MeetingroomVo meetingroomVo = convert(meetingroom);
        mrrv.setObj(meetingroomVo);
        mrrv.setCheckbox(true);
        mrrv.addData(meetingroom.getHysname());
        mrrv.addData(meetingroom.getLocation());
        mrrv.addData(meetingroom.getCapacity());
        mrrv.addData(meetingroom.getDescription());
        mrrv.addData(meetingroom.getHydd());

        // 设置操作的button
        StringBuilder sb = new StringBuilder();
        sb.append(DataTableButtonFactory.getGreenButton("使用详情", "data-id='" + meetingroom.getId() + "'"));
        sb.append(DataTableButtonFactory.getUpdateButton("data-id='" + meetingroom.getId() + "'"));
        sb.append(DataTableButtonFactory.getDeleteButton("data-id='" + meetingroom.getId() + "'"));
        mrrv.addData(sb.toString());
    }

//    /**
//     * do to dtrv
//     */
//    public static void convertDtrv2(DataTableRowVo mrrv, Meetingroom meetingroom) {
//        MeetingroomVo meetingroomVo = convert(meetingroom);
//        mrrv.setObj(meetingroomVo);
//        mrrv.setCheckbox(true);
//        mrrv.addData(meetingroom.getHysname());
//        mrrv.addData(meetingroom.getLocation());
//        mrrv.addData(meetingroom.getCapacity());
//        mrrv.addData(meetingroom.getDescription());
//        mrrv.addData(meetingroom.getDepartment());
//
//        // 设置操作的button
//        StringBuilder sb = new StringBuilder();
//        sb.append(DataTableButtonFactory.getUpdateButton("data-id='" + meetingroom.getId() + "'"));
//        sb.append(DataTableButtonFactory.getDeleteButton("data-id='" + meetingroom.getId() + "'"));
//        mrrv.addData(sb.toString());
//    }

    /**
     * vo to dtrv
     */
    public static void convertDtrv(DataTableRowVo mrrv, MeetingroomVo meetingroomVo) {
        mrrv.setObj(meetingroomVo);
        mrrv.setCheckbox(true);
        mrrv.addData(meetingroomVo.getHysname());
        mrrv.addData(meetingroomVo.getLocation());
        mrrv.addData(meetingroomVo.getCapacity());
        mrrv.addData(meetingroomVo.getDescription());
        mrrv.addData(meetingroomVo.getHydd());

        // 设置操作的button
        StringBuilder sb = new StringBuilder();
        sb.append(DataTableButtonFactory.getGreenButton("使用详情", "data-id='" + meetingroomVo.getId() + "'"));
        sb.append(DataTableButtonFactory.getUpdateButton("data-id='" + meetingroomVo.getId() + "'"));
        sb.append(DataTableButtonFactory.getDeleteButton("data-id='" + meetingroomVo.getId() + "'"));
        mrrv.addData(sb.toString());
    }

    /**
     * vos to dtrvs
     */
    public static List<DataTableRowVo> convertDtrv(List<MeetingroomVo> meetingroomVos, String departmentName) {
        List<DataTableRowVo> mrrvs = new ArrayList<DataTableRowVo>();

        if (!CollectionUtils.isEmpty(meetingroomVos)) {
            for (MeetingroomVo meetingroomVo : meetingroomVos) {
                DataTableRowVo mrrv = new DataTableRowVo();
                convertDtrv(mrrv, meetingroomVo);
                String department_name = meetingroomVo.getHydd();
                if (departmentName.contains(department_name)){
                    mrrvs.add(mrrv);
                }
            }
        }
        return mrrvs;
    }

}
