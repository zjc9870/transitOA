package com.expect.admin.service.vo;

import java.util.List;

public class MeetingroomVo {
    private String id;
    private String hysname; //会议室名 NN
    private String capacity; //容纳的最大人数
    private String location; //具体的位置
    private String description; //其他描述
    //    private String department_id; //所属部门
    private String hydd; //所属部门
    private List<MeetingVo> meetingList;


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getHysname() { return hysname; }
    public void setHysname(String hysname) { this.hysname = hysname; }

    public String getCapacity() { return capacity; }
    public void setCapacity(String capacity) { this.capacity = capacity; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
//
//    public String getDepartment_id(){ return department_id; }
//    public void setDepartment_id(String department_id){ this.department_id = department_id; }

    public String getHydd(){ return hydd; }
    public void setHydd(String hydd){ this.hydd = hydd; }

    public List<MeetingVo> getMeetingList() {
        return meetingList;
    }

    public void setMeetingList(List<MeetingVo> meetingList) {
        this.meetingList = meetingList;
    }

}
