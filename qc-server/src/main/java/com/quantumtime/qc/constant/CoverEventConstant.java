package com.quantumtime.qc.constant;

/**
 * .Description:截图封面响应常量字段 Program:qc-api.Created on 2019-10-18 14:35
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface CoverEventConstant extends VodEventConstant {

    String COVER_URL = "CoverUrl";

    String SNAPSHOTS = "Snapshots";

    String ERROR_CODE = "ErrorCode";

    String ERROR_MESSAGE = "ErrorMessage";

    int MY_CHECK_PASS = 1,
            MY_CHECK_BLOCK = 2,
            MY_CHECK_REVIEW = 3,
            HAVE_NOT = 0,
            UPLOAD_NOT = 0,
            UPLOAD_IN = 1,
            UPLOAD_OVER = 2,
            UPLOAD_FAIL = 3;
}
