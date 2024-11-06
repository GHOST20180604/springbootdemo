package com.hanyc.demo;


import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author ：hanyc
 * @date ：2022/10/10 17:57
 * @description： 验证码
 */
@RestController
@Slf4j
public class DemoController {

    /**
     * 获取验证码  访问这个 controller
     *
     * @return
     */
    @PostMapping("/getCaptcha")
    public Object getCaptcha() {
//        算数类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
//        几位数运算,默认两位
        captcha.setLen(2);
//        获取运算的公式: 3+2 = ?
        captcha.getArithmeticString();
        //获取运算结果
        String text = captcha.text();
        String prefix = UUID.randomUUID().toString().replace("-", "");
//        redisTemplate.opsForValue().set(RedisKeyConsts.LOGIN_CAPTCHA + prefix, text, 10, TimeUnit.MINUTES);
        Map<String, Object> map = new HashMap<>(5);
        map.put("key", prefix);
        map.put("captcha", captcha.toBase64());
        log.debug("获取验证码:{}:{}", prefix, text);
        map.put("code", text);
        return map;
    }

}
