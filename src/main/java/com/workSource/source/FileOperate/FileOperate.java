package com.workSource.source.FileOperate;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author jiahuixi
 * @date 2019/3/7 19:30
 */
@RestController
@RequestMapping("/api")
public class FileOperate {
    /**
     * 不生成直接下载文件
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/download")
    public void download (HttpServletResponse response) throws IOException {
        System.out.println("-----------------");
        String title = "_图集信息";//定义文件名,之前代码省略,都是处理文件内容的代码
        String fileName = title + ".xml";//定义文件格式
        int bufferSize = 65000;
        String  xmlContent = "ceshishishishsi测试测试";
//xmlContent就是文件的内容,反正是个字符串,你们自行处理
        byte[] bytes = xmlContent.getBytes ("utf-8");
        ByteArrayInputStream inputstream = new ByteArrayInputStream (bytes);
        byte abyte0[] = new byte[bufferSize];
        response.setContentType ("application/octet-stream; charset=utf-8");
        response.setContentLength ((int) bytes.length);
        response.setHeader ("Content-Disposition", "attachment;filename=" + new String (fileName.getBytes ("utf-8"), "ISO8859-1"));
        ServletOutputStream out = response.getOutputStream (); response.setCharacterEncoding ("utf-8");
        int sum = 0; int k = 0;
        while ((k = inputstream.read (abyte0, 0, bufferSize)) > -1)
        {
            out.write (abyte0, 0, k);
            sum += k;
        }
        inputstream.close ();
        out.flush ();
        out.close ();
    }
    //普通java文件下载方法，适用于所有框架
    @GetMapping("/download")
    public String downloadFiles(HttpServletRequest request, HttpServletResponse res) {
        try {
            String fileName ="";
            String filePath ="";
           //方法1：IO流实现下载的功能
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                fileName = URLEncoder.encode(fileName+".zip", "UTF-8");
            } else {
                fileName = new String((fileName+".zip").getBytes("UTF-8"), "ISO8859-1");
            }
            res.setContentType("text/html; charset=UTF-8"); //设置编码字符
            res.setContentType("application/octet-stream"); //设置内容类型为下载类型
            // res.setHeader("Content-disposition", "attachment;filename="+srcPathName1);//设置下载的文件名称
            OutputStream out = res.getOutputStream();   //创建页面返回方式为输出流，会自动弹出下载框
            res.setHeader("Content-disposition", "attachment;filename="+fileName);//设置下载的压缩文件名称
            //将打包后的文件写到客户端，输出的方法同上，使用缓冲流输出
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
            byte[] buff = new byte[bis.available()];
            bis.read(buff);
            bis.close();
            out.write(buff);//输出数据文件
            out.flush();//释放缓存
            out.close();//关闭输出流
            return null;
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }
    /**
     * 上传文件并进行读取
     * @param file
     * @param request
     * @throws IOException
     */
    @PostMapping("/uploadFile")
    @ResponseBody
    public  void upload(@RequestParam MultipartFile file, HttpServletRequest request) throws IOException {
        String oo = convertStreamToString(file.getInputStream());
        String ss =  toString(file.getInputStream());
        System.out.println(ss);
        System.out.println(oo);
    }

    public static String toString(InputStream is) {
        try {
            ByteArrayOutputStream boa=new ByteArrayOutputStream();
            int len=0;
            byte[] buffer=new byte[1024];
            while((len=is.read(buffer))!=-1){
                boa.write(buffer,0,len);
            }
            is.close();
            boa.close();
            byte[] result=boa.toByteArray();
            String temp=new String(result);
            if(temp.contains("utf-8")){
                  return boa.toString("utf-8");
            }else if(temp.contains("gb2312")){
                return boa.toString("gb2312");
//                return new String(result,"gb2312");
            }else{
                return boa.toString("utf-8");
//                return new String(result,"utf-8");
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
        public static String convertStreamToString(InputStream inputStream){
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "/n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return sb.toString();
        }

    @RequestMapping(value = "/writeFile")
    public void in (HttpServletResponse response) throws IOException {
            File file = new File("C:\\test.txt");
        try {
            writeTxtFile("ssssss",file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入文件
     * @param content
     * @param file
     * @return
     * @throws Exception
     */
    public static boolean writeTxtFile(String content,File  file)throws Exception{
        if(!file.exists()) {
            //先得到文件的上级目录，并创建上级目录，在创建文件
            file.getParentFile().mkdir();
            try {
                //创建文件
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RandomAccessFile mm=null;
        boolean flag=false;
        FileOutputStream o=null;
        try {
            o = new FileOutputStream(file);
            o.write(content.getBytes("UTF-8"));
            o.close();
            flag=true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            if(mm!=null){
                mm.close();
            }
        }
        return flag;
    }
}
