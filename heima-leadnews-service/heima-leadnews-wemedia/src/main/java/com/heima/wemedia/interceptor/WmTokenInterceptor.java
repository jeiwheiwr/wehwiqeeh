package com.heima.wemedia.interceptor;

import com.heima.model.wemedia.pojos.WmUser;
import com.heima.utils.thread.WmThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
public class WmTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //得到header的信息
        String userId = request.getHeader("userId");
        Optional<String> token = Optional.ofNullable(request.getHeader("token"));
        if (token.isPresent()) {
            //把用户id存入threadlocal
            WmUser wmUser = new WmUser();
            wmUser.setId(Integer.valueOf(userId));
            WmThreadLoc alUtil.setUser(wmUser);
            log.info("wmTokenFilter设置用户信息到threadlocal中");

        }
        return true;
    }
}
