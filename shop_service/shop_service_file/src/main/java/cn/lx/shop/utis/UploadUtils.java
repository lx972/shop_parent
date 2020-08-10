package cn.lx.shop.utis;

import cn.lx.shop.file.FastDfsFile;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;


/**
 * cn.lx.shop.utis
 *
 * @Author Administrator
 * @date 16:05
 */
public class UploadUtils {

    static {
        //读取配置文件
        String path = new ClassPathResource("fdfs_client.conf").getPath();
        try {
            ClientGlobal.init(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到操作storage的对象
     *
     * @return
     * @throws IOException
     */
    public static StorageClient getStorageClient() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient;
    }

    /**
     * 上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String[] upload(FastDfsFile file) throws Exception {
        //图片的额外信息
        NameValuePair[] meta_list = new NameValuePair[1];
        meta_list[0] = new NameValuePair("name", file.getName());
        //得到操作storage的对象
        StorageClient storageClient = getStorageClient();
        String[] upload = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        return upload;
    }

    /**
     * 下载
     *
     * @param groupName      组名：group1
     * @param remoteFilename 虚拟路径名：M00/00/00/wKgrxV8vvaSAebIcAA8Mbf95A2w617.png
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream download(String groupName, String remoteFilename) throws Exception {

        //得到操作storage的对象
        StorageClient storageClient = getStorageClient();
        //得到文件的字节数组
        byte[] bytes = storageClient.download_file(groupName, remoteFilename);
        //构建文件输入流
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        return is;
    }


    /**
     * 删除
     *
     * @param groupName      组名：group1
     * @param remoteFilename 虚拟路径名：M00/00/00/wKgrxV8vvaSAebIcAA8Mbf95A2w617.png
     * @return
     * @throws Exception
     */
    public static int delete(String groupName, String remoteFilename) throws Exception {

        //得到操作storage的对象
        StorageClient storageClient = getStorageClient();
        //删除
        int i = storageClient.delete_file(groupName, remoteFilename);

        return i;
    }

    /**
     * 获取对外访问url
     * ClientGlobal.getG_tracker_http_port() http端口
     * inetSocketAddress.getPort()  tracker的端口
     *
     * @return
     * @throws Exception
     */
    public static String getUrl() throws Exception {

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        InetSocketAddress inetSocketAddress = trackerServer.getInetSocketAddress();
        String url = "http://" + inetSocketAddress.getHostString() + ":" + ClientGlobal.getG_tracker_http_port() + "/";
        return url;
    }

    public static void main(String[] args) throws Exception {
        /*ByteArrayInputStream is = download("group1", "M00/00/00/wKgrxV8vvaSAebIcAA8Mbf95A2w617.png");
        FileOutputStream os = new FileOutputStream("D://1.jpg");
        int i=0;
        byte[] bytes = new byte[1024];
        while ((i=is.read(bytes))!=-1){
            if (i<bytes.length){
                byte[] bytes1 = Arrays.copyOf(bytes, i);
                os.write(bytes1);
            }else {
                os.write(bytes);
            }
        }*/


       /* int group1 = delete("group1", "M00/00/00/wKgrxV8vvaSAebIcAA8Mbf95A2w617.png");
        System.out.println(group1);*/


        String storageInfo = getUrl();
        System.out.println(storageInfo);
    }
}
