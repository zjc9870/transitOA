package com.expect.admin.service.vo;

import com.expect.admin.data.dataobject.Tzdxlx;

/**
 * Created by qifeng on 17/5/4.
 */
public class TzdxlxVo {
    private  String id;
    private  String tzdxlx;

    public TzdxlxVo(){

    }
    public TzdxlxVo(Tzdxlx tzdxlx) {
        this.id = tzdxlx.getId();
        this.tzdxlx = tzdxlx.getTzdxlx();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTzdxlx() {
        return tzdxlx;
    }

    public void setTzdxlx(String tzdxlx) {
        this.tzdxlx = tzdxlx;
    }
}
