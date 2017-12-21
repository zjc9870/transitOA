package com.expect.admin.data.dataobject;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="m_meeting_room")
public class Meetingroom {
    private String id; //PK 会议室ID NN
    private String hysname; //会议室名 NN
    private String description; //描述
    private String department_id; //管理员所属部门
    private String capacity; //容纳的最大人数
    private String location; //具体的位置
    private String hydd; //具体的位置
    private Set<Meeting> meetings;//附件
    private Set<User> users; //

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id", nullable = false, unique = true, length = 45)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "m_meetingroom_info", joinColumns = @JoinColumn(name = "meetingroom_id"),
            inverseJoinColumns = @JoinColumn(name = "meetinginfo_id"))
    public Set<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(Set<Meeting> meetings) {
        this.meetings = meetings;
    }


    @Column(name = "hysname",nullable = false,length = 45)
    public String getHysname() { return hysname; }
    public void setHysname(String hysname) { this.hysname = hysname; }

    @Column(name = "description",length = 45)
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
//
//    @Column(name = "department_id",length = 45)
//    public String getDepartment() { return department_id; }
//    public void setDepartment(String department_id) { this.department_id = department_id; }

    @Column(name = "hydd",length = 45)
    public String getHydd() { return hydd; }
    public void setHydd(String hydd) { this.hydd = hydd; }



    @Column(name = "capacity",length = 45)
    public String getCapacity() { return capacity; }
    public void setCapacity(String capacity) { this.capacity = capacity; }

    @Column(name = "location",length = 45)
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

}
