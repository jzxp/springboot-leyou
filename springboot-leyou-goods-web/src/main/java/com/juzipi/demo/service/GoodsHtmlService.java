package com.juzipi.demo.service;

import com.juzipi.demo.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Field;

@Service
public class GoodsHtmlService {

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private GoodsService goodsService;


    /**
     * 创建html页面
     * @param spuId
     */
    public void createHtml(Long spuId) {
        //初始化运行上下文
        Context context = new Context();
        //设置数据模型
        context.setVariables(this.goodsService.loadData(spuId));

        PrintWriter printWriter = null;
        try {
            //把静态文件生成到服务器本地
            File file = new File("D:\\Utilities\\nginx-1.18.0\\nginx-1.18.0\\html\\item\\"+spuId+".html");
            printWriter = new PrintWriter(file);
            this.templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (StringUtils.isEmpty(printWriter)){
                printWriter.close();
            }
        }
    }


    /**
     * 新建线程处理页面静态化
     * @param spuId
     */
    public void asyncExcute(Long spuId){
        ThreadUtils.execute(()->createHtml(spuId));
    }

    public void deleteHtml(Long id) {
        File file = new File("D:\\Utilities\\nginx-1.18.0\\nginx-1.18.0\\html\\item\\" + id + ".html");
        file.deleteOnExit();
    }
}
