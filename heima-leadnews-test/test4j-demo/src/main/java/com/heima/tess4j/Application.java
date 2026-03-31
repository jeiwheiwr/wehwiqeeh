package com.heima.tess4j;

import java.io.File;

public class Application {
    public static void main(String[] args){
        try{
            //获取本地图片
            File file = new File("C:\\Users\\MING\\Desktop\\Desk_Material\\Heima_Toutiao\\day4\\自媒体文章-自动审核.assets.1585141909238.png");
            //创建Tesseract对象
            ITesseract tesseract = new Tesseract();
            //设置字体库路径
            tesseract.setDatapath("C:\\Users\\MING\\Desktop\\Desk_Material\\Heima_Toutiao\\day4\\tessdata");
            //中文识别
            tesseract.setLanguage("chi_sim");
            //执行ocr识别
            String result = tesseract.doOCR(file);
            //替换回车和·tal键，使结果为一行
            result = result.replaceAll("\\n", "").replaceAll("\\t", "");
            System.out.println("识别的结果为："+result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
