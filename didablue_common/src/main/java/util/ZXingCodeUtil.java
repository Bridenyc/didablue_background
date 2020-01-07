package util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.*;

import static java.awt.Font.BOLD;

public class ZXingCodeUtil {

    private static final int BUFFER_SIZE = 1024;
    private static final String PATH_SEP = "/";

    // 默认格式
    private static final String FORMAT_NAME = "png";
    private static final int QRCOLOR = 0xFF000000; // 默认是黑色
    private static final int BGWHITE = 0xFFFFFFFF; // 默认是白色

    private static final int WIDTH = 400; // 二维码宽
    private static final int HEIGHT = 400; // 二维码高

    /**
     * 【createQr 二维码生成工具类】
     *
     *
     * @param qr logo名 / 文件名 (数组)
     *
     * @param choose 二维码生成条件：
     *        "0": logo，底标 都不输出
     *        "1": logo      输出
     *        "2": 底标       输出
     *        "3": logo，底标 都输出
     *
     * @param url 二维码跳转url
     * @param note 二维码底部标题
     * @throws Exception
     */
    public static void createQr(String[] qr, String choose, String url, String note) throws Exception {

        // 二维码保存路径
        String path = "G://二维码测试//二维码";
        // logo保存路径
        String pic = "G://二维码测试//logo";
        // 下载目标路径
        String down = "G://二维码测试//";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        // 生成二维码
        for (String s : qr) {
            String codeNo = s;
            String URL = url + "/BankTeller?id=" + s;
            ZXingCodeUtil.getImage(codeNo, 50, 50, pic);
            File logoFile = new File(pic + "//" + codeNo + ".png");
            File QrCodeFile = new File(path + "//" + codeNo + ".png");

            // 生成中间logo
            if(choose.equals("0")){     // "0": logo，底标 都不输出
                ZXingCodeUtil.drawLogoQRCode(null, QrCodeFile, URL, "");
            }else if (choose.equals("1")) {     // "1": logo      输出
                ZXingCodeUtil.drawLogoQRCode(logoFile, QrCodeFile, URL, "");
            }else if(choose.equals("2")){      // "2": 底标       输出
                ZXingCodeUtil.drawLogoQRCode(null, QrCodeFile, URL, note);
            }else if(choose.equals("3")){       // "3": logo，底标 都输出
                ZXingCodeUtil.drawLogoQRCode(logoFile, QrCodeFile, URL, note);
            }else{
                throw new Exception("二维码输入条件出错");
            }
        }

        File[] list = file.listFiles();
        // 二维码图片不止一张时
        if (list.length > 1) {
            //2 生成zip文件
            zipCompress(path, down + note + ".zip");
            //3 删除多余文件
            deleteDir(new File(path));
            deleteDir(new File(pic));
            System.out.println("多张二维码压缩包生成成功");
        }else if(list.length == 1){
            // 二维码图片只有一张时
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : qr) {
                StringBuilder append = stringBuilder.append(s);
                String s1 = append.toString();
                String str1 = path + "//" + s1 + ".png";
                String str2 = down + s1 + ".png";
                copyFile(str1, str2);
            }
            //4 删除多余文件
            deleteDir(new File(path));
            deleteDir(new File(pic));
            System.out.println("单张二维码生成成功");
        }else{
            System.out.println("二维码生成失败");
        }
    }

    /**
     * 删除图片方法
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String s : children) {
                boolean success = deleteDir(new File(dir, s));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


    /**
     * 复制图片方法
     * @param srcPath
     * @param destPath
     * @throws IOException
     */
    public static void copyFile(String srcPath, String destPath) throws IOException {
        // 打开输入流
        FileInputStream fis = new FileInputStream(srcPath);
        // 打开输出流
        FileOutputStream fos = new FileOutputStream(destPath);
        // 读取和写入信息
        int len = 0;
        while ((len = fis.read()) != -1) {
            fos.write(len);
        }
        // 关闭流  先开后关  后开先关
        fos.close(); // 后开先关
        fis.close(); // 先开后关
    }

    /**
     * 开启创建二维码
     * @param args
     * @throws IOException
     */

    // 用于设置QR二维码参数
    private static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
        private static final long serialVersionUID = 1L;

        {
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
            put(EncodeHintType.CHARACTER_SET, "utf-8");// 设置编码方式
            put(EncodeHintType.MARGIN, 0);
        }
    };

    /**
     * 创建图片
     * @param content 内容
     * @param font  字体
     * @param width 宽
     * @param height 高
     * @return
     */
    private static BufferedImage createImage(String content,Font font,Integer width,Integer height){
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, width, height);
        g2.setPaint(Color.BLACK);

        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(content, context);
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight()) / 2;
        double ascent = -bounds.getY();
        double baseY = y + ascent;

        g2.drawString(content, (int)x, (int)baseY);

        return bi;
    }

    /**
     * 获取 图片
     * @param content 内容
     * @param font  字体
     * @param width 宽
     * @param height 高
     * @return
     */
    public static BufferedImage getImage(String content,Font font,Integer width,Integer height){
        width=width==null?WIDTH:width;
        height=height==null?HEIGHT:height;
        if(null==font)
            font = new Font("Serif", BOLD, 12);
        return createImage(content, font, width, height);
    }

    /**
     * 获取 图片
     * @param content 内容
     * @param width 宽
     * @param height 高
     * @return
     */
    public static BufferedImage getImage(String content,Integer width,Integer height){

        return getImage(content, null,width, height);
    }

    /**
     * 获取图片
     * @param content 内容
     * @return
     */
    public static BufferedImage getImage(String content){

        return getImage(content, null, null);
    }

    /**
     *  获取图片
     * @param content 内容
     * @param font 字体
     * @param width 宽
     * @param height 高
     * @param destPath 输出路径
     * @throws IOException
     */
    public static void getImage(String content,Font font ,Integer width,Integer height,String destPath) throws IOException{
        mkdirs(destPath);
        String file = content +".png";
        ImageIO.write(getImage(content,font,width,height),FORMAT_NAME, new File(destPath+"/"+file));
    }

    /**
     * 获取图片
     * @param content 内容
     * @param font 字体
     * @param width 宽
     * @param height 高
     * @param output 输出流
     * @throws IOException
     */
    public static void getImage(String content,Font font,Integer width,Integer height, OutputStream output) throws IOException{
        ImageIO.write(getImage(content,font,width,height), FORMAT_NAME, output);
    }

    /**
     * 获取图片
     * @param content 内容
     * @param width 宽
     * @param height 高
     * @param destPath 输出路径
     * @throws IOException
     */
    public static void getImage(String content, Integer width, Integer height, String destPath) throws IOException{
        getImage(content, null, width, height, destPath);
    }

    /**
     * 获取图片
     * @param content 内容
     * @param width 宽
     * @param height 高
     * @param output 输出流
     * @throws IOException
     */
    public static void getImage(String content,Integer width,Integer height, OutputStream output) throws IOException {
        getImage(content, null, width, height, output);
    }


    /**
     * 创建 目录
     * @param destPath
     */
    public static void mkdirs(String destPath) {
        File file =new File(destPath);
        //当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    // 生成带logo的二维码图片
    public static void drawLogoQRCode(File logoFile, File codeFile, String qrUrl, String note) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            BitMatrix bm = multiFormatWriter.encode(qrUrl, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

            // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
                }
            }

            int width = image.getWidth();
            int height = image.getHeight();


            // 根据logo生成二维码
            if (Objects.nonNull(logoFile) && logoFile.exists()) {
                // 构建绘图对象
                Graphics2D g = image.createGraphics();
                // 读取Logo图片
                BufferedImage logo = ImageIO.read(logoFile);
                // 开始绘制logo图片
                g.drawImage(logo, width * 7 / 20, height * 7 / 20, width * 3 / 10, height * 3 / 10, null);
                g.dispose();
                logo.flush();
            }

            // 自定义文本描述
            if (StringUtils.isNotEmpty(note)) {
                // 新的图片，把带logo的二维码下面加上文字
                //BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_INT_RGB);
                BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_INT_RGB);
                Graphics2D outg = outImage.createGraphics();
                outg.fillRect(0,200,600,600);//填充整个屏幕
                outg.setColor(Color.WHITE);
                // 画二维码到新的面板
                outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                // 画文字到新的面板
                outg.setFont(new Font("宋体", Font.BOLD, 30)); // 字体、字型、字号
                outg.setColor(Color.BLACK);
                int strWidth = outg.getFontMetrics().stringWidth(note);
                if (strWidth > 399) {
                    // //长度过长就截取前面部分
                    // 长度过长就换行
                    String note1 = note.substring(0, note.length() / 2);
                    String note2 = note.substring(note.length() / 2, note.length());
                    int strWidth1 = outg.getFontMetrics().stringWidth(note1);
                    int strWidth2 = outg.getFontMetrics().stringWidth(note2);
                    outg.drawString(note1, 200 - strWidth1 / 2, height + (outImage.getHeight() - height) / 2 + 12);
                    BufferedImage outImage2 = new BufferedImage(400, 485, BufferedImage.TYPE_INT_RGB);
                    Graphics2D outg2 = outImage2.createGraphics();
                    outg2.drawImage(outImage, 0, 0, outImage.getWidth(), outImage.getHeight(), null);
                    outg2.setFont(new Font("宋体", Font.BOLD, 30)); // 字体、字型、字号
                    outg2.drawString(note2, 200 - strWidth2 / 2, outImage.getHeight() + (outImage2.getHeight() - outImage.getHeight()) / 2 + 5);
                    outg2.dispose();
                    outImage2.flush();
                    outImage = outImage2;
                } else {
                    outg.drawString(note, 200 - strWidth / 2, height + (outImage.getHeight() - height) / 2 + 12); // 画文字
                }
                outg.dispose();
                outImage.flush();
                image = outImage;
            }

            image.flush();

            ImageIO.write(image, "png", codeFile); // TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @param sourceFileName
     * @param destFileName
     * @throws IOException
     */
    public static void zipCompress(String sourceFileName, String destFileName) throws IOException {
        File sourceFile = new File(sourceFileName);
        File destFile = new File(destFileName);
        zipCompress(sourceFile, destFile);
    }

    /**
     * @param sourceFile
     * @param destFile
     * @throws IOException
     */
    public static void zipCompress(File sourceFile, File destFile) throws IOException {
        FileOutputStream fos = null;
        CheckedOutputStream cos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(destFile);
            cos = new CheckedOutputStream(fos, new CRC32());
            zos = new ZipOutputStream(cos);
            zipCompress(sourceFile, zos, "");
            zos.flush();
            zos.finish();
        } finally {
            if (zos != null)
                zos.close();
            if (cos != null)
                cos.close();
            if (fos != null)
                fos.close();
        }
    }

    /**
     * @param sourceFile
     * @param zos
     * @param basePath
     * @throws IOException
     */
    private static void zipCompress(File sourceFile, ZipOutputStream zos, String basePath) throws IOException {
        if (sourceFile.isDirectory())
            zipCompressDir(sourceFile, zos, basePath);
        else
            zipCompressFile(sourceFile, zos, basePath);
    }

    /**
     * @throws IOException
     */
    private static void zipCompressDir(File dir, ZipOutputStream zos, String basePath) throws IOException {
        File[] files = dir.listFiles();
        String newBasePath = basePath + dir.getName() + PATH_SEP;
        if (files.length <= 0) {
            ZipEntry entry = new ZipEntry(newBasePath);
            zos.putNextEntry(entry);
            zos.closeEntry();
        }
        for (File file : files) {
            zipCompress(file, zos, newBasePath);
        }
    }

    /**
     * @throws IOException
     */
    private static void zipCompressFile(File file, ZipOutputStream zos, String basePath) throws IOException {
        String entryName = basePath + file.getName();
        FileInputStream fis = new FileInputStream(file);
        zipCompress(fis, zos, entryName);
    }

    /**
     * @throws IOException
     */
    private static void zipCompress(InputStream is, ZipOutputStream zos, String entryName) throws IOException {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(is);
            ZipEntry entry = new ZipEntry(entryName);
            zos.putNextEntry(entry);
            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                count = bis.read(buffer, 0, BUFFER_SIZE);
                if (count < 0)
                    break;
                zos.write(buffer, 0, count);
            }
            zos.closeEntry();
        } finally {
            if (bis != null)
                bis.close();
        }
    }

    /**
     * @param is
     * @param destFile
     * @throws IOException
     */
    public static void zipDecompress(InputStream is, File destFile) throws IOException {
        CheckedInputStream cis = null;
        ZipInputStream zis = null;
        try {
            cis = new CheckedInputStream(is, new CRC32());
            zis = new ZipInputStream(cis);
            zipDecompress(zis, destFile);
        } finally {
            if (zis != null)
                zis.close();
            if (cis != null)
                cis.close();
        }
    }

    /**
     * @param zis
     * @param destFile
     * @throws IOException
     */
    private static void zipDecompress(ZipInputStream zis, File destFile) throws IOException {
        while (true) {
            ZipEntry entry = zis.getNextEntry();
            if (entry == null)
                break;
            String dir = destFile.getPath() + File.separator + entry.getName();
            File dirFile = new File(dir);
            ensureFolderExists(dirFile);

            if (entry.isDirectory()) {
                dirFile.mkdir();
            }
            else {
                zipDecompressFile(zis, dirFile);
            }
            zis.closeEntry();
        }
    }

    /**
     * @param zis
     * @param destFile
     * @throws IOException
     */
    private static void zipDecompressFile(ZipInputStream zis, File destFile) throws IOException {
        BufferedOutputStream bos = null;
        int count;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(destFile));
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                count = zis.read(buffer, 0, BUFFER_SIZE);
                if (count < 0)
                    break;
                bos.write(buffer, 0, count);
            }
        } finally {
            if (bos != null)
                bos.close();
        }
    }

    /**
     * 确保文件夹存在。不存在则创建
     * @param folder
     */
    public static void ensureFolderExists(File folder)
    {
        File parentFile = folder.getParentFile();
        //递归查找上级目录，没有则创建
        if(! parentFile.exists())
        {
            ensureFolderExists(parentFile);
            parentFile.mkdir();
        }
    }
}
