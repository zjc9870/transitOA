package com.expect.admin.data.dataobject;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by qifeng on 17/5/4.
 */@Entity
@Table(name = "c_tzdxlx")
public class Tzdxlx {
    private String id;
    private String tzdxlx;

    public Tzdxlx(){

    }

    public Tzdxlx(String id, String tzdxlx) {
        this.id = id;
        this.tzdxlx = tzdxlx;
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
    public String getTzdxlx() {
        return tzdxlx;
    }

    public void setTzdxlx(String tzdxlx) {
        this.tzdxlx = tzdxlx;
    }
}
