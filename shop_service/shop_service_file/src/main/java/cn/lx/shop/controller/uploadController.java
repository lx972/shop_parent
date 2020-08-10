package cn.lx.shop.controller;

import cn.lx.shop.entity.Result;
import cn.lx.shop.entity.StatusCode;
import cn.lx.shop.file.FastDfsFile;
import cn.lx.shop.utis.UploadUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * cn.lx.shop
 *
 * @Author Administrator
 * @date 16:26
 */
@RestController
@RequestMapping(value = "/file")
public class uploadController {

    /**
     * 文件上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        //封装文件信息
        String originalFilename = file.getOriginalFilename();
        byte[] content = file.getBytes();
        String ext = StringUtils.getFilenameExtension(originalFilename);
        FastDfsFile fastDfsFile = new FastDfsFile(originalFilename, content, ext);
        //调用上传方法
        String[] upload = UploadUtils.upload(fastDfsFile);
        String url = UploadUtils.getUrl() + upload[0] + "/" + upload[1];
        return new Result(true, StatusCode.OK, "文件上传成功", url);
    }
}
