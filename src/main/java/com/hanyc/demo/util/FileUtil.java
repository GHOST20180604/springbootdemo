package com.hanyc.demo.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.data.Pictures;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author ：hanyc
 * @date ：2022/8/9 17:33
 * @description： 文件工具类
 */
@Slf4j
public class FileUtil {

    /**
     * 判断文件夹是否存在，不存在则创建
     *
     * @param file
     */
    public static void createFile(File file) {
        if (!file.exists()) {
            FileUtil.log.debug("File {} not exists, create it ...", file.getPath());
            //getParentFile() 获取上级目录(包含文件名时无法直接创建目录的)
            if (!file.getParentFile().exists()) {
                //创建上级目录
                file.getParentFile().mkdirs();
            }
        }
    }

    /**
     * 删除文件或文件夹
     *
     * @param file 文件/文件夹
     * @return 删除成功返回true, 失败返回false
     */
    public static boolean deleteFileOrDirectory(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                // 是文件，调用删除文件的方法
                return FileUtil.deleteFile(file);
            } else {
                // 是文件夹，调用删除文件夹的方法
                return FileUtil.deleteDirectory(file);
            }
        } else {
            FileUtil.log.info("文件或文件夹不存在，删除失败：" + file.getAbsolutePath());
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param file 文件名
     * @return 删除成功返回true, 失败返回false
     */
    public static boolean deleteFile(File file) {
        if (file.isFile() && file.exists()) {
            file.delete();
            FileUtil.log.debug("删除文件成功：" + file.getAbsolutePath());
            return true;
        }
        return false;
    }

    /**
     * 删除文件夹
     * 删除文件夹需要把包含的文件及文件夹先删除，才能成功
     *
     * @param directoryFile 文件夹
     * @return 删除成功返回true, 失败返回false
     */
    public static boolean deleteDirectory(File directoryFile) {
        // directory不以文件分隔符（/或\）结尾时，自动添加文件分隔符，不同系统下File.separator方法会自动添加相应的分隔符
        // 判断directory对应的文件是否存在，或者是否是一个文件夹
        if (!directoryFile.exists() || !directoryFile.isDirectory()) {
            FileUtil.log.info("文件夹删除失败，文件夹不存在" + directoryFile.getAbsolutePath());
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件和文件夹
        File[] files = directoryFile.listFiles();
        // 循环删除所有的子文件及子文件夹
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                // 删除子文件
                if (files[i].isFile()) {
                    flag = FileUtil.deleteFile(files[i]);
                    if (!flag) {
                        break;
                    }
                } else {  // 删除子文件夹
                    flag = FileUtil.deleteDirectory(files[i]);
                    if (!flag) {
                        break;
                    }
                }
            }
        }
        if (!flag) {
            FileUtil.log.debug("删除失败");
            return false;
        }
        // 最后删除当前文件夹
        if (directoryFile.delete()) {
            FileUtil.log.debug("删除成功：" + directoryFile.getAbsolutePath());
            return true;
        } else {
            FileUtil.log.debug("删除失败：" + directoryFile.getAbsolutePath());
            return false;
        }
    }


    /**
     * 读取本地html文件里的html代码
     *
     * @param file html文件
     * @return
     */
    public static String toHtmlString(File file) {
        // 获取HTML文件流
        StringBuilder htmlSb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            while (br.ready()) {
                htmlSb.append(br.readLine());
            }
            byte[] bytes = htmlSb.toString().getBytes(StandardCharsets.UTF_8);
            htmlSb = new StringBuilder(new String(bytes, 3, bytes.length - 3));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert br != null;
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return htmlSb.toString();
    }



    /**
     * 将fileToZip文件夹及其子目录文件递归压缩到zip文件中
     *
     * @param fileToZip 递归当前处理对象，可能是文件夹，也可能是文件
     * @param fileName  fileToZip文件或文件夹名称
     * @param zipOut    压缩文件输出流
     * @throws IOException
     */
    public static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        //不压缩隐藏文件夹
        if (fileToZip.isHidden()) {
            return;
        }
        FileUtil.log.debug("压缩文件:{}", fileName);
        //判断压缩对象如果是一个文件夹
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith(StrUtil.SLASH)) {
                //如果文件夹是以“/”结尾，将文件夹作为压缩箱放入zipOut压缩输出流
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                //如果文件夹不是以“/”结尾，将文件夹结尾加上“/”之后作为压缩箱放入zipOut压缩输出流
                zipOut.putNextEntry(new ZipEntry(fileName + StrUtil.SLASH));
                zipOut.closeEntry();
            }
            //遍历文件夹子目录，进行递归的zipFile
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                FileUtil.zipFile(childFile, fileName + StrUtil.SLASH + childFile.getName(), zipOut);
            }
            //如果当前递归对象是文件夹，加入ZipEntry之后就返回
            return;
        }
        //如果当前的fileToZip不是一个文件夹，是一个文件，将其以字节码形式压缩到压缩包里面
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    /**
     * 解压缩zip
     *
     * @param zipFilePath 压缩文件
     * @param zipDir      压缩文件所在文件夹位置 ; 示例  D:\\del\\temp
     * @return
     */
    public static String unzipFile(String zipFilePath, String zipDir) {
        String name = "";
        BufferedOutputStream dest = null;
        BufferedInputStream is = null;
        ZipEntry entry;
        try {
            ZipFile zipfile = new ZipFile(zipFilePath, Charset.forName("GBK"));
            Enumeration dir = zipfile.entries();
            while (dir.hasMoreElements()) {
                entry = (ZipEntry) dir.nextElement();

                if (entry.isDirectory()) {
                    name = entry.getName();
                    name = name.substring(0, name.length() - 1);
                    File fileObject = new File(zipDir + File.separator + name);
                    fileObject.mkdir();
                }
            }
            Enumeration e = zipfile.entries();
            while (e.hasMoreElements()) {
                entry = (ZipEntry) e.nextElement();
                if (entry.isDirectory()) {
                    continue;
                } else {
                    is = new BufferedInputStream(zipfile.getInputStream(entry));
                    int count;
                    int buffer = 1024;
                    byte[] dataByte = new byte[buffer];
                    FileOutputStream fos = new FileOutputStream(zipDir + File.separator + entry.getName());
                    dest = new BufferedOutputStream(fos, buffer);
                    while ((count = is.read(dataByte, 0, buffer)) != -1) {
                        dest.write(dataByte, 0, count);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
        } catch (Exception e) {
            log.error("unzipFile fail :{}", e.getMessage(), e);
        } finally {
            try {
                if (dest != null) {
                    dest.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return name;
    }

    /**
     * 输入流转为文件
     *
     * @param inputStream 文件输入流
     * @param fileName    文件名
     * @return
     */
    public static File inputStream2File(InputStream inputStream, String fileName) {
        File file = new File(fileName);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            IoUtil.copy(inputStream, out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static void main(String[] args) throws FileNotFoundException {
        //图片
        String json = "{\"theme\":\"macarons\",\"tooltip\":{\"trigger\":\"item\"},\"legend\":{\"orient\":\"vertical\",\"left\":\"left\"},\"series\":[{\"name\":\"Access From\",\"type\":\"pie\",\"radius\":\"50%\",\"data\":[{\"value\":1048,\"name\":\"Search Engine\"},{\"value\":29,\"name\":\"Direct\"},{\"value\":22,\"name\":\"Email\"},{\"value\":11,\"name\":\"Union Ads\"},{\"value\":2,\"name\":\"Video Ads\"}],\"label\":{\"formatter\":\"{a}\\n{c}\\n{d}%\"},\"emphasis\":{\"itemStyle\":{\"shadowBlur\":10,\"shadowOffsetX\":0,\"shadowColor\":\"rgba(0, 0, 0, 0.5)\"}}}]}";
        JSONObject jsonObject = JSONObject.parseObject(json);

        // Save the input stream to a local file
        String saveFilePath = "D:\\del\\poi-tl\\a" + System.currentTimeMillis() + ".jpg";

        FileOutputStream outputStream = new FileOutputStream(saveFilePath);
        HttpRequest post = HttpUtil.createPost("http://172.16.8.51:3000/");
        post.body(JSON.toJSONString(jsonObject));
        // 获取发送请求后的响应对象
        HttpResponse execute = post.timeout(30000).execute();
        System.out.println(execute.body());
        cn.hutool.core.io.FileUtil.writeBytes(execute.bodyBytes(),saveFilePath);
    }
}
