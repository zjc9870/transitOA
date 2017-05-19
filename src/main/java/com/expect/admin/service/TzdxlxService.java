package com.expect.admin.service;

import com.expect.admin.data.dao.TzdxlxRepository;
import com.expect.admin.data.dataobject.Tzdxlx;
import com.expect.admin.service.vo.TzdxlxVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qifeng on 17/5/4.
 */
@Service
public class TzdxlxService {
    @Autowired
    private TzdxlxRepository tzdxlxRepository;
    public List<TzdxlxVo> getTzdxlx(){
        List<Tzdxlx> tzdxlxList=tzdxlxRepository.findAll();
        List<TzdxlxVo> tzdxlxVoList=new ArrayList<>();
        if (tzdxlxList!=null){
            for (Tzdxlx tzdxlx: tzdxlxList){
                TzdxlxVo tzdxlxVo=new TzdxlxVo(tzdxlx);
                tzdxlxVoList.add(tzdxlxVo);
            }
        }
        return tzdxlxVoList;
    }

}
