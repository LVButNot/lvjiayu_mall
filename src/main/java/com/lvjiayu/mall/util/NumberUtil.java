package com.lvjiayu.mall.util;


public class NumberUtil {
    /**
     * 生成指定长度的随机数
     * @param length 指定长度
     * @return 随机数
     */
    public static int genRandomNum(int length) {
        int num = 1;
        double random = Math.random();
        if(random < 0.1){
            random += 0.1;
        }
        for(int i = 0; i < length; i++){
            num = num * 10;
        }
        return (int) (num * random);
    }
}
