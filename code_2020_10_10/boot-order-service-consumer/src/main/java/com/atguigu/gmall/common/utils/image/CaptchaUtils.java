package com.atguigu.gmall.common.utils.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtils {
    private static final String RANDOM_STRS = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";
    private static final String FONT_NAME = "Times New Roman";//字体
    private static final int FONT_SIZE = 24;//字体大小
    private int width = 85;// 图片宽
    private int height = 25;// 图片高
    private int lineNum = 155;// 干扰线数量
    private int strNum = 4;// 随机产生字符数量

    private Random random = new Random();

    /**
     * 生成随机图片
     *
     * @param randomCode
     * @return
     */
    public BufferedImage genRandomCodeImage(StringBuffer randomCode) {
        // BufferedImage类是具有缓冲区的Image类
        BufferedImage image = new BufferedImage(this.width, this.height,
                BufferedImage.TYPE_INT_RGB);
        // 获取Graphics对象,便于对图像进行各种绘制操作
        Graphics g = image.getGraphics();
        g.setColor(CaptchaUtils.getRandColor(200, 250));
        g.fillRect(0, 0, this.width, this.height);
        //g.setColor(new Color());
        //g.drawRect(0,0,width-1,height-1);
        g.setColor(CaptchaUtils.getRandColor(160, 200));

        // 绘制干扰线
        for (int i = 0; i <= this.lineNum; i++) {
            this.drowLine(g);
        }
        // 绘制随机字符
        g.setFont(new Font(CaptchaUtils.FONT_NAME, Font.PLAIN,
                CaptchaUtils.FONT_SIZE));

        for (int i = 0; i < this.strNum; i++) {
            randomCode.append(this.drowString(g, i));
        }
        g.dispose();
        return image;
    }

    /**
     * 给定范围获得随机颜色
     */
    private static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 绘制字符串
     */
    private String drowString(Graphics g, int i) {
        g.setColor(new Color(20 + this.random.nextInt(110), 20 + this.random
                .nextInt(110), 20 + this.random.nextInt(110)));
        String rand = String.valueOf(CaptchaUtils.getRandomString(this.random
                .nextInt(CaptchaUtils.RANDOM_STRS.length())));
        //  g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(rand, 20 * i + 4, 19);
        return rand;
    }

    /**
     * 绘制干扰线
     */
    private void drowLine(Graphics g) {
        int x = this.random.nextInt(this.width);
        int y = this.random.nextInt(this.height);
        int x0 = this.random.nextInt(12);
        int y0 = this.random.nextInt(12);
        g.drawLine(x, y, x + x0, y + y0);
    }

    /**
     * 获取随机的字符
     */
    private static String getRandomString(int num) {
        return String.valueOf(CaptchaUtils.RANDOM_STRS.charAt(num));
    }
}
