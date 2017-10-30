package com.expect.admin.data.dataobject;

import com.expect.admin.service.vo.NotifyObjectVo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by qifeng on 17/5/4.
 */
@Entity
@Table(name = "d_notify_object")
public class NotifyObject {
    private String id;
    private String tzdxfl;//通知对象分类 高管 办公室等
    private String tzdx;//用户全称 例如集团董事长(王伟)
    private User tzdxUser;
//    private Department ssgs;//所属公司

    public NotifyObject(){

    }
    public NotifyObject(NotifyObjectVo notifyObjectVo) {
        this.id = notifyObjectVo.getId();
        this.tzdxfl = notifyObjectVo.getTzdxfl();
        this.tzdx = notifyObjectVo.getTzdx();
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id", nullable = false, unique = true, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "tzdxfl",length = 30)
    public String getTzdxfl() {
        return tzdxfl;
    }
    public void setTzdxfl(String tzdxfl) {
        this.tzdxfl = tzdxfl;
    }

    @Column(name = "tzdx",length = 100)
    public String getTzdx() {
        return tzdx;
    }

    public void setTzdx(String tzdx) {
        this.tzdx = tzdx;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "tzdx_user")
    public User getUser() {
        return tzdxUser;
    }

    public void setUser(User tzdxUser) {
        this.tzdxUser = tzdxUser;
    }

//    @ManyToOne(cascade = CascadeType.REFRESH)
//    @JoinColumn(name = "notify_department")
//    public Department getSsgs() {
//        return ssgs;
//    }
//
//    public void setSsgs(Department ssgs) {
//        this.ssgs = ssgs;
//    }

}
