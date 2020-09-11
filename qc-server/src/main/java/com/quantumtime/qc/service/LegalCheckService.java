package com.quantumtime.qc.service;

/**
 * .Description:合法性校验处理 Program:qc-api.Created on 2019-10-30 18:07
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface LegalCheckService {
    String RESULTS = "results";
    String SCENE = "scene";
    String TEXT_SUGGESTION = "suggestion";
    String DATA = "data";
    String DATA_ID = "dataId";
    String TASKS = "tasks";
    String SCENES = "scenes";
    String ANTISPAM = "antispam";
    String REGION_CN_HANGZHOU = "cn-hangzhou";
    String CONTENT = "content";
    String CODE = "code";
    int HTTP_SUCCESS = 200;
    /**
     * Created on 9:40 2019/10/31 Author: Tablo.
     *
     * <p>Description:[校验文本合法]
     *
     * @param text 文本内容
     * @return java.lang.String string
     */
    String checkText(String text);
}
