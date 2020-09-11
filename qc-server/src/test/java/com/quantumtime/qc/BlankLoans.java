package com.quantumtime.qc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * .Description:银行贷款 Program:qc-api.Created on 2019-11-20 14:50
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */
public class BlankLoans {
  public static void main(String[] args) {

    LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        System.err.println(today_start);
    System.out.println(today_start.toInstant(ZoneOffset.of("+8")).toEpochMilli());
//    String s = "";
//    String q = "5";
//    Long m = null;
//    Optional.ofNullable(s).filter(StringUtils::isNotBlank).ifPresent(System.err::println);
//    Video video = new Video();
//    video.setTitle("");
//    Optional.ofNullable(m).ifPresent(a -> video.setActivityId(m));
//    Optional.ofNullable(q).ifPresent(a -> video.setActivityName(q));
//    Video video1 = new Video();
//
//    Optional.ofNullable(q).ifPresent(a -> video.setTitle(q));
//    Optional.ofNullable(s)
//        .filter(StringUtils::isNotBlank)
//        .map(activityName -> video.setActivityName(activityName).getActivityName())
//        .ifPresent(video1::setActivityName);
//    Optional.ofNullable(s)
//            .filter(StringUtils::isNotBlank)
//            .map(activityName -> video.setActivityName(activityName).getActivityName())
//            .ifPresent(video1::setActivityName);
//    System.err.println(video.getActivityName());
//    System.err.println(video1.getActivityName());
//
//    BaseInfo baseInfo = new BaseInfo();
//    baseInfo.setUser(null);
//    User user = Optional.ofNullable(baseInfo)
//            .map(BaseInfo::getUser)
//            .orElse(new User().setFanSum(80));
//    System.err.println(user);
  }

  public double gainProfit(List<LoanRecord> records, List<RiskProbability> probability) {

    Map<String, RiskProbability> riskMap =
        probability.stream()
            .collect(
                Collectors.toMap(
                    RiskProbability::getCode, risk -> risk, (a, b) -> b, HashMap::new));
    long capitalCount = records.stream().mapToDouble(LoanRecord::getCapital).count();
    long count =
        records.stream()
            .mapToDouble(
                record ->
                    record.getCapital()
                        * (1 + record.getInterest() / 100)
                        * riskMap.get(record.getRiskLevel()).getProbability())
            .count();
    return count - capitalCount;
  }
}
