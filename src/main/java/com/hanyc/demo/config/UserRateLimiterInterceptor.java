package com.hanyc.demo.config;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 用户请求限流拦截器
 * @Author hanyc
 * @Date 2024/12/12
 **/
@Component
@Slf4j
public class UserRateLimiterInterceptor implements HandlerInterceptor {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 假设这个方法能够从请求中解析出用户ID
        String userIdStr = "";
        String userIp = ServletUtil.getClientIP(request);
        // 假设用户ID 为 1
        String userId = "1";
        if (userId == null) {
            // 所有未登录账号 不需要验证的请求,都使用userId=10000
            userIdStr = userIp;
        } else {
            userIdStr = userId.toString();
        }
        // 为每个用户生成唯一的限流键
        String rateLimiterKey = "USER_RATE_LIMITER" + userIdStr;
        // 删除 Redis 中存储的限流器状态（清除缓存）
        // RRateLimiter 在内部将配置保存在 Redis 中，并且这些配置是持久化的。删除该键后，Redis 会丢失之前存储的限流配置，从而确保新的配置能够被应用。
        redissonClient.getBucket(rateLimiterKey).delete();
        // 获取分布式的 RRateLimiter 实例
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(rateLimiterKey);
        // 初始化限流器，限制每两秒最多 10 次请求
        // 参数说明:
        // RateType.OVERALL 全局
        // rate 时间限制内可以请求多少次
        // rateInterval 多少时间内限制
        // 时间单位 可以秒/分钟/小时等
        rateLimiter.trySetRate(RateType.OVERALL, 10, 2, RateIntervalUnit.SECONDS);
        log.info("当前路由为:{} userIdStr:{} userIp:{}", request.getRequestURI(), userIdStr, userIp);
        // 尝试获取令牌，如果获取不到说明超过请求限制
        if (rateLimiter.tryAcquire()) {
            // 允许继续处理请求
            return true;
        } else {
            // 如果获取不到令牌，则说明请求超过了限制，可以在这里抛出异常或者返回错误信息
            log.warn("当前异常的路由为:{} userIdStr:{} userIp :{}", request.getRequestURI(), userIdStr, userIp);
            return false;
        }
    }


}