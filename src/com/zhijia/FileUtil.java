package com.zhijia;



import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 文件工具类
 * @author android su
 *
 */
public class FileUtil {
    /**
     * 获取目录名称
     * @param url
     * @return FileName
     */
    public static String getFileName(String url)
    {
        int lastIndexStart = url.lastIndexOf("/");
        if(lastIndexStart!=-1)
        {
            return url.substring(lastIndexStart+1, url.length());
        }else{
            return new Timestamp(System.currentTimeMillis()).toString();
        }
    }
    /**
     * 判断SD卡是否存在
     * @return boolean
     */
    public static boolean checkSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 保存目录目录到目录
     * @param context
     * @return  目录保存的目录
     */
    public static String setMkdir(Context context)
    {
        String filePath = null;
        if(checkSDCard())
        {
            filePath = Environment.getExternalStorageDirectory()+File.separator+"yishuabao"+File.separator+"downloads";
        }else{
            filePath = context.getCacheDir().getAbsolutePath()+File.separator+"yishuabao"+File.separator+"downloads";
        }
        File file = new File(filePath);
        if(!file.exists())
        {
            file.mkdirs();
            Log.e("file", "目录不存在   创建目录    ");
        }else{
            Log.e("file", "目录存在");
        }
        return filePath;
    }
    public static  File  getFile(Context context){
    	String filePath = null;
        if(checkSDCard())
        {
            filePath = Environment.getExternalStorageDirectory()+File.separator+"zhijia"+File.separator+"downloads";
        }else{
            filePath = context.getCacheDir().getAbsolutePath()+File.separator+"zhijia"+File.separator+"downloads";
        }
        File file = new File(filePath);
		return file;
    	
    }
    
    /**
     * 获取路径
     * @return
     * @throws IOException
     */
    public static  String getPath(Context context,String url)
    {
        String path = null;
        try {
            path = FileUtil.setMkdir(context)+File.separator+url.substring(url.lastIndexOf("/")+1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
    
    /**
     * 获取文件的大小
     * 
     * @param fileSize
     *            文件的大小
     * @return
     */
    public static String FormetFileSize(int fileSize) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }
    
}
