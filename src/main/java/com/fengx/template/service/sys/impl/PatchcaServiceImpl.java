package com.fengx.template.service.sys.impl;

import com.fengx.template.exception.PatchaException;
import com.fengx.template.service.sys.PatchcaService;
import com.fengx.template.utils.RedisUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.patchca.color.ColorFactory;
import org.patchca.color.SingleColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.Captcha;
import org.patchca.service.CaptchaService;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PatchcaServiceImpl implements PatchcaService {
    // 生成验证码范围
    private static final String DEFAULT_CHARACTERS = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int DEFAULT_FONT_SIZE = 35;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 50;
    // 默认过期时间5分钟
    private static final Long DEFAULT_EXPIRE = 5 * 60 * 1000L;

    private final @NonNull RedisUtils<String> redisUtils;

    @Override
    public void doPatcha(String appId, HttpServletRequest request, HttpServletResponse response) {
        try {
            CaptchaService service = create();
            // 清除缓存
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0L);
            // 显示类型
            response.setContentType("image/png");
            String token = EncoderHelper.getChallangeAndWriteImage(service, "png", response.getOutputStream());
            // 放入缓存
            redisUtils.set(appId, token, DEFAULT_EXPIRE, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PatchaException("验证码生成错误");
        }
    }

    @Override
    public void validate(String codeId, String code) {
        if (!redisUtils.hasKey(codeId)) {
            throw new PatchaException("验证码过期");
        }
        String codeRe = redisUtils.get(codeId);
        // 不区分大小写
        if (!code.toLowerCase().equals(codeRe.toLowerCase())) {
            throw new PatchaException("验证码错误");
        }
    }

    public CaptchaService create(int fontSize, int wordLength, String characters, int width, int height) {
        ConfigurableCaptchaService service;
        // 字体大小设置
        RandomFontFactory ff = new RandomFontFactory();
        ff.setMinSize(fontSize);
        ff.setMaxSize(fontSize);
        // 生成的单词设置
        RandomWordFactory rwf = new RandomWordFactory();
        rwf.setCharacters(characters);
        rwf.setMinLength(wordLength);
        rwf.setMaxLength(wordLength);
        // 干扰线波纹
        CurvesRippleFilterFactory crff = new CurvesRippleFilterFactory();
        crff.setStrokeMax(0);
        crff.setStrokeMin(0);
        // 处理器
        service = new ExConfigurableCaptchaService();
        service.setFontFactory(ff);
        service.setWordFactory(rwf);
        service.setFilterFactory(crff);
        // 生成图片大小（像素）
        service.setWidth(width);
        service.setHeight(height);
        return service;
    }

    public CaptchaService create() {
        return create(DEFAULT_FONT_SIZE, DEFAULT_WORD_LENGTH, DEFAULT_CHARACTERS, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * 随机变色处理
     */
    private static class ExConfigurableCaptchaService extends ConfigurableCaptchaService {
        private static final Random RANDOM = new Random();
        private final List<SingleColorFactory> colorList = new ArrayList<>(); // 为了性能
        public ExConfigurableCaptchaService() {
            colorList.add(new SingleColorFactory(Color.blue));
            colorList.add(new SingleColorFactory(Color.black));
            colorList.add(new SingleColorFactory(Color.red));
            colorList.add(new SingleColorFactory(Color.pink));
            colorList.add(new SingleColorFactory(Color.orange));
            colorList.add(new SingleColorFactory(Color.green));
            colorList.add(new SingleColorFactory(Color.magenta));
            colorList.add(new SingleColorFactory(Color.cyan));
        }
        public ColorFactory nextColorFactory() {
            int index = RANDOM.nextInt(colorList.size());
            return colorList.get(index);
        }

        @Override
        public Captcha getCaptcha() {
            ColorFactory cf = nextColorFactory();
            CurvesRippleFilterFactory crff = (CurvesRippleFilterFactory) this.getFilterFactory();
            crff.setColorFactory(cf);
            this.setColorFactory(cf);
            return super.getCaptcha();
        }
    }
}
