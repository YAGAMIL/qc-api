package com.quantumtime.qc.common.controller;

import com.quantumtime.qc.common.entity.BaseEntity;
import com.quantumtime.qc.common.service.IBaseService;
import com.quantumtime.qc.vo.MyPage;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * .Description: & Created on 2019/08/01 15:32
 *
 * @author 宇飞
 * @version 1.0
 */
@Slf4j
public class BaseController<T extends BaseEntity, ID, S extends IBaseService<T, ID>> {
//    The Tomcat connector configured to listen on port 8090 failed to start. The port may already be in use or the connector may be misconfigured.

    @Autowired
    protected S baseService;

    @ApiOperation(value = "单个查询", notes = "根据Id查询")
    @GetMapping("/{id}")
    public Result findById(@PathVariable("id") ID id) {
        log.debug("get ID : {}", id);
        return Result.success(baseService.findById(id));
    }

    @ApiOperation(value = "查询所有", notes = "查询所有")
    @GetMapping("/all")
    @ApiIgnore
    public Result findAll() {
        return Result.success(baseService.findAll());
    }

    @ApiOperation(value = "分页", notes = "分页 查询所有")
    @GetMapping("/page")
    @ApiIgnore
    public Result findAll(MyPage page) {
        log.debug("page :  {} size : {}", page.getPage(), page.getSize());
        PageRequest rageRequest = PageRequest.of(page.getPage() - 1, page.getSize(), Sort.by(Sort.Direction.DESC, "upTime"));
        return Result.success(baseService.findAll(rageRequest));
    }

    @ApiOperation(value = "新增", notes = "不需要添加id")
    @PostMapping()
    @ApiIgnore
    public Result save(@RequestBody T entity) {
        log.debug("save :  {}", entity);
        return Result.success(baseService.save(entity));
    }

    @ApiOperation(value = "修改", notes = "修改必须要id")
    @PutMapping()
    @ApiIgnore
    public Result update(@RequestBody T entity) {
        log.debug("update:  {}", entity);
        return Result.success(baseService.update(entity));
    }

    @ApiOperation(value = "删除", notes = "只需要id即可")
    @DeleteMapping("/{id}")
    @ApiIgnore
    public Result delete(@PathVariable("id") ID id) {
        log.debug("delete:  {}", id);
        baseService.deleteById(id);
        return Result.success();
    }

}
