package com.example.webdemo.mybatis.record;

import com.example.webdemo.mybatis.provider.UpdateRecordProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 *
 * @author admin
 * @since 2021/1/16
 */
@RegisterMapper
public interface UpdateByRecordSelectiveMapper<T> {
    /**
     * 批量更新，record null值会被更新
     *
     * @param record 修改set的参数
     * @param example where条件
     * @return
     */
    @UpdateProvider(type = UpdateRecordProvider.class, method = "dynamicSQL")
    int updateByRecordSelective(@Param("record") T record, @Param("example") T example);
}
