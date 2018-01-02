package com.expect.admin.data.dao;

import com.expect.admin.data.dataobject.Meetingroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingroomRepository extends JpaRepository<Meetingroom, String>{
    public Meetingroom findById(String meetingroomId);

    @Query("select distinct hydd from Meetingroom ")
    public List<String> findHydd();

    @Query("select distinct hysname from Meetingroom where hydd = ?1")
    public List<String> findHysnameByHydd(String hydd);

    @Query("select m.id from Meetingroom m where hydd = ?1 and hysname = ?2")
    public String findMeetingroomId(String hydd, String hysname);

    public Meetingroom findByHyddAndHysname(String hydd, String hysname);

    @Query("select distinct hysname from Meetingroom where hydd = ?1 and location = ?2 ")
    public String findHysnameByHyddAndLocation(String hydd, String location);

}
