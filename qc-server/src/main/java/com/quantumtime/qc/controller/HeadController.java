package com.quantumtime.qc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * .Description:阿里云健康度接口 & Created on 2019/11/12 10:55
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
@ApiIgnore
@RestController
@RequestMapping("/head")
public class HeadController {

    /**
     * Check head string.
     *
     * @return the string
     */
    @RequestMapping(value = "/checkHead", method = RequestMethod.HEAD)
    public String checkHead() {
        return "ok";
    }

}
