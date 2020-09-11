package com.quantumtime.qc.controller;

import com.quantumtime.qc.common.constant.ErrorCodeConstant;
import com.quantumtime.qc.common.controller.BaseController;
import com.quantumtime.qc.common.exception.BizVerifyException;
import com.quantumtime.qc.entity.poi.Address;
import com.quantumtime.qc.help.AccountHelp;
import com.quantumtime.qc.service.IAddressService;
import com.quantumtime.qc.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Description: 地址信息控制器 Created on 2019/09/16 20:56
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@Api(tags = "地址管理")
@RestController
@RequestMapping("/address")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddressController extends BaseController<Address, Long, IAddressService> {

  private final AccountHelp accountHelp;

  @ApiIgnore
  @ApiOperation(value = "根据上级id查找地址", notes = "root节点id为0")
  @GetMapping("/findByParentId")
  public Result findByParentId(String parentId) {
    if (StringUtils.isEmpty(parentId)) {
      throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "parentId");
    }
    return Result.success(baseService.findByParentId(Long.parseLong(parentId)));
  }

  @ApiOperation(value = "根据当前id查找完整地址", notes = "返回地址列表")
  @GetMapping("/findAddressById")
  public Result findAddressById(String id) {
    if (StringUtils.isEmpty(id)) {
      throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "parentId");
    }
    return Result.success(baseService.findAddressById(Long.parseLong(id)));
  }

  @ApiOperation(value = "根据当前PoiId查找完整地址", notes = "返回地址列表")
  @GetMapping(value = "/{poiId}}")
  public Result getAddrByPoiId(@PathVariable("poiId") String poiId) {
    if (StringUtils.isEmpty(poiId)) {
      throw new BizVerifyException(ErrorCodeConstant.VERIFY_NOT_NULL, new Throwable(), "parentId");
    }
    return Result.success(baseService.findByPoiId(poiId));
  }

  @ApiIgnore
  @ApiOperation(value = "查找当前用户详细地址", notes = "返回地址列表")
  @GetMapping("/findCurrentUserAddress")
  public Result findCurrentUserAddress() {
    return Result.success(baseService.findAddressById(accountHelp.getCurrentUser().getAddressId()));
  }
}
