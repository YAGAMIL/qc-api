package com.quantumtime.qc.common.constant;

/**
 * Description: 异常码枚举 Created on 2019/10/14 10:46
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public interface ErrorCodeConstant {

  /** --------------- global --------------------- */
  String GLOBAL_EXIST = "global_exist",
      GLOBAL_GENERATE_QRCODE_ERROR = "global_generate_qrcode_error",
      GLOBAL_PERMISSION_ERROR = "global_permission_error",
      GLOBAL_UNKNOWN_EXCEPTION = "global_unknown_exception",
      GLOBAL_NOT_EXIST = "global_not_exist",

      /** --------------- cache --------------------- */
      CACHE_NOT_EXIST = "cache_not_exist",

      /** --------------- verify--------------------- */
      VERIFY_NOT_NULL = "verify_not_null",
      VERIFY_PHONE_FORMAT_ERROR = "verify_phone_format_error",
      NOT_FOLLOW = "not_follow",

      /** --------------- sms --------------------- */
      SMS_OVER_SEND = "sms_over_send",
      SMS_ACTION_ERROR = "sms_action_error",
      SMS_VERIFICATION_MISS = "sms_verification_miss",
      SMS_VERIFICATION_ERROR = "sms_verification_error",
      SMS_CONTROL = "sms_control",
      SMS_SEND_ERROR = "sms_send_error",

      /** --------------- account --------------------- */
      ACCOUNT_PHONE_EXIST_REGISTER = "account_phone_exist_register",
      ACCOUNT_PHONE_NOT_EXIST_REGISTER = "account_phone_not_exist_register",
      ACCOUNT_STATUS_ERROR = "account_status_error",
      ACCOUNT_DISABLE = "account_disable",
      ACCOUNT_WECHAT_NOT_UNVERIFIED = "account_wechat_not_unverified",
      ACCOUNT_NOT_EXIST = "account_not_exist",
      ACCOUNT_NOT_AUTH = "account_not_auth",
      ACCOUNT_TOKEN_ERROR = "account_token_error",
      ACCOUNT_USERNAMEORPASSWD_ERROR = "account_usernameorpasswd_error",
      ACCOUNT_WECHAT_LOGIN_ERROR = "account_wechat_login_error",
      PHONE_BIND_WECHAT = "phone_bind_wechat",
      WECHAT_NOT_EXIST = "wechat_not_exist",

      /** ---------------- information ---------------------- */
      INFORMATION_DELETE = "information_delete",
      INFORMATION_LIKE_DELETE = "information_like_delete",
      INFORMATION_COMMIT_DELETE = "information_commit_delete",
      INFORMATION_THEME_JOIN_ERROR = "information_theme_join_error",
      INFORMATION_LIKE_EXIST = "information_like_exist",
      INFORMATION_LIKE_NOT_EXIST = "information_like_not_exist",
      INFORMATION_SELF_FORWRD_ERROR = "information_self_forward_error",
      INFORMATION_THEME_FORWARD_PUBLIC_ERROR = "information_theme_forward_public_error",
      INFORMATION_FORWARD_ERROR = "information_forward_error",
      DATE_TYPE_ERROR = "date_type_error",

      /** ---------------- mq ---------------------- */
      MQ_REQUEST_FAIL = "mq_request_fail",

      /** ---------------- address ---------------------- */
      ADDRESS_NULL = "mq_request_fail";
}
