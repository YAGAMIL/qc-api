package com.quantumtime.qc.service;

import com.quantumtime.qc.vo.tls.TlsModifyAccount;
import com.quantumtime.qc.vo.tls.TlsRegistAccount;
import com.tls.tls_sigature.tls_sigature;

import java.util.zip.DataFormatException;

public interface ITlsService {

    /**
     * 签名
     * @return
     */
    tls_sigature.GenTLSSignatureResult genTLSSignatureEx();

    /**
     * 根据账户进行签名
     * @param identifier
     * @return
     */
    tls_sigature.GenTLSSignatureResult genTLSSignatureEx(String identifier);

    /**
     * 验签
     * @param urlSig
     * @return
     * @throws DataFormatException
     */
    tls_sigature.CheckTLSSignatureResult checkTLSSignatureEx(String urlSig) throws DataFormatException;


    /**
     * 注册腾讯IM账户
     * @param account
     * @return
     */
    boolean registTlsUser(TlsRegistAccount account);

    /**
     * 修改腾讯IM账户
     * @param account
     * @return
     */
    boolean modifyTlsUser(TlsModifyAccount account);
}
