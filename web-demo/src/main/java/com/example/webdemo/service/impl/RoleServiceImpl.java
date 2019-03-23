package com.example.webdemo.service.impl;

import com.example.webdemo.beans.Role;
import com.example.webdemo.beans.RoleExample;
import com.example.webdemo.dao.RoleMapper;
import com.example.webdemo.enums.SysCodeEnum;
import com.example.webdemo.exception.ServiceException;
import com.example.webdemo.service.RoleService;
import com.example.webdemo.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    @Transactional
    public boolean save(Role role) {
        try {
            return roleMapper.insertSelective(role) == 1;
        } catch (Exception e) {
            throw new ServiceException(SysCodeEnum.DB_ERR.getCode(),SysCodeEnum.DB_ERR.getDesc(),e);
        }
    }

    @Override
    public PageVo query(Role role) {
        PageVo vo = new PageVo<Role>(true);
        RoleExample example = new RoleExample();

        RoleExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(role.getId())) {
            criteria.andIdEqualTo(role.getId());
        }
        if (!StringUtils.isEmpty(role.getRoleName())) {
            criteria.andRoleNameEqualTo(role.getRoleName());
        }
        vo.setList(roleMapper.selectByExample(example));
        vo.setTotal(roleMapper.countByExample(example));
        return vo;
    }

    @Override
    public List<Role> queryAll() {
        return roleMapper.selectAll();
    }
}
