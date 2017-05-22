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
    private String tzdxfl;
    private String tzdx;
    private User tzdxUser;

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
}
