package com.quantumtime.qc.common.utils;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import java.util.List;

/**
 * <p>
 * Description:靓号体系类
 * Program:saas-server
 * </p>
 * Created on 2019-10-11 17:48
 *
 * @author <a href="mailto: Tablo_Jhin1996@outlook.com">Tablo</a>
 * @version 1.0
 */

class Focus2IdUtil {
    private static PatternCompiler compiler = new Perl5Compiler();

    private static PatternMatcher matcher = new Perl5Matcher();

    /**
     * 根据正则过滤条件过滤
     *
     * @param input 参数
     * @return 是否匹配
     */
    public static boolean contains(String input, String patternString) {
        try {
            Pattern pattern = compiler.compile(patternString);
            if (matcher.contains(input, pattern)) {
                return true;
            }
        } catch (MalformedPatternException e) {
            return false;
        }
        return false;
    }

    static boolean contains(String input, List<String> patternStrings) {
        return patternStrings.stream().anyMatch(patternString -> contains(input, patternString));
    }
}