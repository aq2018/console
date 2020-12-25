package com.example.webdemo.dao.intf;

import com.example.webdemo.beans.DemoDo;
import com.example.webdemo.mybatis.common.IBaseDao;

import java.util.List;

/**
 * @author admin
 * @since 2020/1/10 16:26
 */
public interface DemoDao extends IBaseDao<DemoDo> {
    List<DemoDo> queryBatch();
}
