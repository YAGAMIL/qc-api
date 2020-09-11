package com.quantumtime.qc.common.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import java.time.LocalDateTime;

/**
 * Description:SQL.log Program:qc-api Created on 2019-09-20 10:40
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public class P6SpyLogger implements MessageFormattingStrategy {

  @Override
  public String formatMessage(
      int connectionId,
      String now,
      long elapsed,
      String category,
      String prepared,
      String sql,
      String datasource) {
    return !"".equals(sql.trim())
        ? "[ "
            + LocalDateTime.now()
            + " ] --- | took "
            + elapsed
            + "ms | "
            + category
            + " | connection "
            + connectionId
            + "\n "
            + "datasource "
            + datasource
            + "\n "
            + sql
            + ";"
        : "";
  }
}
