package com.workSource.source.FileOperate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
    //复制方法
    public static boolean copyDir(String src, String des)  {
        try {
        //初始化文件复制
        File file1=new File(src);
        if(!file1.exists()){
            return false;
        }else{
        //把文件里面内容放进数组
        File[] fs=file1.listFiles();
        //初始化文件粘贴
        File file2=new File(des);
        //判断是否有这个文件有不管没有创建
        if(!file2.exists()){
            file2.mkdirs();
        }
        //遍历文件及文件夹
        for (File f : fs) {
            if(f.isFile()){
                //文件
                fileCopy(f.getPath(),des+File.separator+f.getName()); //调用文件拷贝的方法
            }else if(f.isDirectory()){
                //文件夹
                copyDir(f.getPath(),des+File.separator+f.getName());//继续调用复制方法      递归的地方,自己调用自己的方法,就可以复制文件夹的文件夹了
            }
        }
            return true;
        }
        }catch (Exception e){
            e.getStackTrace();
            return false;
        }
    }

    /**
     * 文件复制的具体方法
     */
    private static void fileCopy(String src, String des) throws Exception {
        //io流固定格式
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des));
        int i = -1;//记录获取长度
        byte[] bt = new byte[2014];//缓冲区
        while ((i = bis.read(bt))!=-1) {
            bos.write(bt, 0, i);
        }
        bis.close();
        bos.close();
        //关闭流
    }
    public static String getFileName(String fName){
        String fileName = null;
        File tempFile =new File( fName.trim());
        if(tempFile.exists()){
            File[] array = tempFile.listFiles();
            for(int i = 0 ; i<array.length ; i++){
                String houzhui = array[i].toString().substring(array[i].toString().lastIndexOf(".") + 1);
                if(houzhui.equals("iw")||houzhui.equals(".zip")){
                    fileName = array[i].getName();
                }
            }
        }
        return fileName;
    }
    /**
     * 删除空目录
     * @param dir 将要删除的目录路径
     */
    private static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            LOGGER.info("Successfully deleted empty directory: " + dir);
        } else {
            LOGGER.info("Failed to delete empty directory: " + dir);
        }
    }
    public static void main(String[] args) throws Exception {
        String srcPathName = "E:/workSpace/apistore/apistore-api/target/upload"+File.separator+"temp"+File.separator+"123";
        String content = "{\"id\":10,\"versionId\":11,\"name\":\"金融基金\",\"onlineTime\":0,\"version\":\"v1.0\"}";
        writeTxtFile(content,new File(srcPathName));
        getDirName(srcPathName);
                String sourcePath = "C:/Users/jiahuixi/Desktop/upload/temp/apiId";
        String path = "C:/Users/jiahuixi/Desktop/upload/real/apiId";
        doDeleteEmptyDir("new_dir1");
        String newDir2 = "new_dir2";
        boolean success = deleteDir(new File("C:/Users/jiahuixi/Desktop/upload/"));
        if (success) {
            System.out.println("Successfully deleted populated directory: " + newDir2);
        } else {
            System.out.println("Failed to delete populated directory: " + newDir2);
        }

        copyDir(sourcePath, path);
    }

    /**
     * 写入文件
     * @param
     */
    public static boolean writeTxtFile(String content,File  fileName)throws Exception{
        RandomAccessFile mm=null;
        boolean flag=false;
        FileOutputStream o=null;
        try {
            o = new FileOutputStream(fileName);
            o.write(content.getBytes("UTF-8"));
            o.close();
//   mm=new RandomAccessFile(fileName,"rw");
//   mm.writeBytes(content);
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
    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    public static Set<String> getDirName(String fName){
        Set<String> set = new HashSet<>();
        String fileName = null;
        File tempFile =new File( fName.trim());
        if(tempFile.exists()){
            File[] array = tempFile.listFiles();
            for(int i = 0 ; i<array.length ; i++){
                set.add(array[i].getName().toString());
            }
        }
        System.out.println(set);
        return set;
    }
}
