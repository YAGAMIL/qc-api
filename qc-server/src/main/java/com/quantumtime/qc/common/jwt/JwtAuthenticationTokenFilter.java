//package com.quantumtime.qc.common.jwt;
//
//import com.quantumtime.qc.common.constant.ErrorCodeConstant;
//import com.quantumtime.qc.common.constant.SmsConstant;
//import com.quantumtime.qc.common.exception.BizException;
//import com.quantumtime.qc.service.IRedisService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Map;
//
//
//@Slf4j
//@Component
//public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    @Lazy
//    private IRedisService redisService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
//        String authHeader = request.getHeader(this.jwtTokenUtil.getTokenHeader());
//        if (authHeader != null) {
//            String authToken = null;
//            if(authHeader.startsWith(this.jwtTokenUtil.getTokenHead())) {
//                authToken = authHeader.substring(this.jwtTokenUtil.getTokenHead().length());
//            }else if(authHeader.startsWith(this.jwtTokenUtil.getWechatTokenHead())){
//                authToken = authHeader.substring(this.jwtTokenUtil.getWechatTokenHead().length());
//            }else{
//                chain.doFilter(request, response);
//            }
//            // 获取账号
//            String phone = jwtTokenUtil.getUsernameFromToken(authToken);
//            log.info("JwtAuthenticationTokenFilter[doFilterInternal] checking authentication " + phone);
//            //token校验通过
//            if (phone != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                // 根据account去数据库中查询user数据，足够信任token的情况下，可以省略这一步
//                Map map = redisService.getMap(SmsConstant.TOKEN_PREFIX + phone);
//                if(map != null && map.containsKey(authToken)){
////                    redisService.setExpireTime(SmsConstant.TOKEN_PREFIX + phone, jwtTokenUtil.getExpiration()/1000);
//                    JwtUser userDetails = (JwtUser) map.get(authToken);
//                    if (jwtTokenUtil.validateToken(authToken, userDetails)) {
//                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                                userDetails, null, userDetails.getAuthorities());
//                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        log.info("JwtAuthenticationTokenFilter[doFilterInternal]  authenticated user " + phone + ", setting security context");
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                    }
//                }else{
//                    throw new BizException(ErrorCodeConstant.ACCOUNT_TOKEN_ERROR, new Throwable());
//                }
//            }
//        }
//        chain.doFilter(request, response);
//    }
//}
