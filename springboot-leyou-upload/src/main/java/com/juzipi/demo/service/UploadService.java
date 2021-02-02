package com.juzipi.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    private static final List<String> CONTENT_TYPES = Arrays.asList("image/gif","image/jpg");

    public static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

//    @Autowired
//    private FastFileStorageClient storageClient;

    public String uploadImage(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        //获取最后一个的字符串，两个参数分别是：对象（originalFilename），分割条件"."
//        StringUtils.substringAfterLast(originalFilename,".");


        //校验文件类型
        String contentType = file.getContentType();
        if (CONTENT_TYPES.contains(contentType)){
            LOGGER.info("文件类型不合法：{}",originalFilename);
            return null;
        }

        try {
            //校验文件的内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null){
                LOGGER.info("文件内容不合法：{}",originalFilename);
                return null;
            }
            //保存到文件的服务器
            file.transferTo(new File("D:\\SpringBootLearning\\image\\" + originalFilename));

//            String s = StringUtils.substringAfterLast(originalFilename, ".");
//            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), s, "null");

            //返回url，进行回显
            return "http://image.leyou.com/" + originalFilename;
//            return "http://image.leyou.com/" +storePath.getFullPath();
        } catch (IOException e) {
            LOGGER.info("服务器内部错误："+originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
