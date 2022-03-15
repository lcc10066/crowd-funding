package com.crowd.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.crowd.aliUtils.HttpUtils;
import com.crowd.constant.CrowdConstant;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrowdUtil {

/**
 * 可在阿里云OSS服务器使用SDK中查看使用方法
 */



    /**
     * 专门负责上传文件到OSS服务器的工具方法
     *
     * @param endpoint
     *            OSS参数
     * @param accessKeyId
     *            OSS参数
     * @param accessKeySecret
     *            OSS参数
     * @param inputStream
     *            要上传的文件的输入流
     * @param bucketName
     *            OSS参数
     * @param bucketDomain
     *            OSS参数
     * @param originalName
     *            要上传的文件的原始文件名
     * @return 包含上传结果以及上传的文件在OSS上的访问路径
     */
    public static ResultEntity<String> uploadFileToOss(String endpoint, String accessKeyId, String accessKeySecret,
                                                       InputStream inputStream, String bucketName, String bucketDomain, String originalName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());

        // 生成上传文件在OSS服务器上保存时的文件名
        // 原始文件名：beautfulgirl.jpg
        // 生成文件名：wer234234efwer235346457dfswet346235.jpg
        // 使用UUID生成文件主体名称
        String fileMainName = UUID.randomUUID().toString().replace("-", "");

        // 从原始文件名中获取文件扩展名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));

        // 使用目录、文件主体名称、文件扩展名称拼接得到对象名称
        String objectName = folderName + "/" + fileMainName + extensionName;

        try {
            // 调用OSS客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, inputStream);

            // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();

            // 根据响应状态码判断请求是否成功
            if (responseMessage == null) {

                // 拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = bucketDomain + "/" + objectName;

                // 当前方法返回成功
                return ResultEntity.successWithData(ossFileAccessPath);
            } else {
                // 获取响应状态码
                int statusCode = responseMessage.getStatusCode();

                // 如果请求没有成功，获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();

                // 当前方法返回失败
                return ResultEntity.failed("当前响应状态码=" + statusCode + " 错误消息=" + errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();

            // 当前方法返回失败
            return ResultEntity.failed(e.getMessage());
        } finally {

            if (ossClient != null) {

                // 关闭OSSClient。
                ossClient.shutdown();
            }
        }

    }


    /**
     *
     * @param host
     * @param path
     * @param method
     * @param phoneNum
     * @param appCode
     * @param sign      使用了不同的验证，这个字段暂时未使用
     * @param skin
     * @return
     */
    public static ResultEntity<String> sendCodeByShortMessage(
            String host, String path, String method, String phoneNum,
            String appCode, String sign, String skin){


//        String host = "https://dfsns.market.alicloudapi.com";
//        String path = "/data/send_sms";
//        String method = "POST";
//        String appCode = "24dd7d967f1d4d91bd018492aab11fbb";

//      生成验证码
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int random = (int) (Math.random() * 10);
            builder.append(random);
        }
        String code = builder.toString();

        Map<String, String> headers = new HashMap<String, String>();

        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);

        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:"+code);
        bodys.put("phone_number", phoneNum);
        bodys.put("template_id", skin);
//        bodys.put("template_id", "TPL_0000");

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));

            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            String reasonPhrase = statusLine.getReasonPhrase();
            if(statusCode == 200)
//                成功则返回验证码
                return ResultEntity.successWithData(code);
            return ResultEntity.failed(reasonPhrase);

        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }







//请求头示例：
//    Accept: text/plain, */*; q=0.01
//    Accept-Encoding: gzip, deflate, br
//    Accept-Language: zh-CN,zh;q=0.9
//    Connection: keep-alive
//    Content-Length: 227
//    Content-Type: application/json;charset=UTF-8
//    Cookie: JSESSIONID=F62B9CED02C9F864DC9F8A3B0EA01138; Idea-55747ddc=0bf64eb2-388c-45fd-888a-b4cd7b688ef2
//    Host: localhost:8080
//    Origin: http://localhost:8080
//    Referer: http://localhost:8080/crowd/
//    sec-ch-ua: " Not A;Brand";v="99", "Chromium";v="96", "Google Chrome";v="96"
//    sec-ch-ua-mobile: ?0
//    sec-ch-ua-platform: "Windows"
//    Sec-Fetch-Dest: empty
//    Sec-Fetch-Mode: cors
//    Sec-Fetch-Site: same-origin
//    User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36
//    X-Requested-With: XMLHttpRequest


    /**
     * 根据请求头所包含的信息来判断为普通请求还是Ajax请求：
     *      请求头中 只有第一个和最后一个键值信息可用于判断
     *         Accept ：text/plain..........
     *      X-Requested-With :XMLHttpRequest
     * @param request
     * @return      true  为ajax
     *
     */

    public static boolean judgeRequestType(HttpServletRequest request){

        String accept = request.getHeader("Accept");
        String xRequestedWith = request.getHeader("X-Requested-With");

        return (accept!=null && accept.contains("(application)/json")) || (xRequestedWith!=null && xRequestedWith.equals("XMLHttpRequest"));

    }

    /**
     * 对明文字符串进行MD5加密
     *
     * @param source
     *            传入的明文字符串
     * @return 加密结果
     */
    public static String md5(String source) {

        // 1.判断source是否有效
        if (source == null || source.length() == 0) {

            // 2.如果不是有效的字符串抛出异常
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        try {
            // 3.获取MessageDigest对象
            String algorithm = "md5";

            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            // 4.获取明文字符串对应的字节数组
            byte[] input = source.getBytes();

            // 5.执行加密
            byte[] output = messageDigest.digest(input);

            // 6.创建BigInteger对象
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, output);

            // 7.按照16进制将bigInteger的值转换为字符串
            int radix = 16;
            String encoded = bigInteger.toString(radix).toUpperCase();

            return encoded;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }


}
