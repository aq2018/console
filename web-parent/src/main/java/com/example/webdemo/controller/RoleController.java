package com.example.webdemo.controller;

import com.example.webdemo.beans.Role;
import com.example.webdemo.common.vo.BaseVo;
import com.example.webdemo.service.PermissionService;
import com.example.webdemo.service.RolePermissionRelationService;
import com.example.webdemo.service.RoleService;
import com.example.webdemo.common.vo.PageVo;
import com.example.webdemo.vo.request.RoleRequest;
import com.example.webdemo.vo.request.UpdateRoleRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionRelationService rolePermissionRelationService;

    @RequestMapping("/save")
    public BaseVo saveRole(@RequestBody Role role) {
        return new BaseVo(roleService.save(role));
    }

    @RequestMapping("/update")
    public BaseVo update(@RequestBody @Valid UpdateRoleRequest req) {
        Role role = new Role();
        BeanUtils.copyProperties(req,role);
        return new BaseVo(roleService.update(role));
    }


    @RequestMapping("/query")
    public PageVo query(@RequestBody RoleRequest req) {
        return roleService.query(req);
    }

    @RequestMapping("/queryAll")
    public List<Role> queryAll() {
        return roleService.queryAll();
    }

}
