package com.csnt.deploy.util;

import com.jfinal.json.Jackson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件处理工具类
 * Created by duwanjiang on 2017/12/22.
 */
public class FilesUtil {

    private static Logger logger = LoggerFactory.getLogger(FilesUtil.class);

    /**
     * json文件后缀名
     */
    public static final String JSON_SUFFIX = ".json";
    /**
     * zip文件后缀名
     */
    public static final String ZIP_SUFFIX = ".zip";
    /**
     * temp文件后缀名
     */
    public static final String ZIP_TEMP_SUFFIX = ".temp";

    /**
     * 缓存区大小
     */
    private static final int bufferSize = 1024;

    private static Object lock = new Object();

    /**
     * 根据文件路径获取文件目录
     *
     * @param filePath 文件路径
     * @return 返回文件目录
     */
    public static String getFileDir(String filePath) {
        Path parentPath = Paths.get(filePath).getParent();
        return parentPath.toString();
    }

    /**
     * 根据文件路径获取文件名
     *
     * @param filePath 文件路径
     * @return 返回文件名
     */
    public static String getFileName(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return null;
        }
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 根据文件路径获取文件名
     *
     * @param filePath 文件路径
     * @return 返回文件名
     */
    public static String getFileName(Path filePath) {
        return filePath.getFileName().toString();
    }

    /**
     * 根据文件路径获取无后缀文件名
     *
     * @param filePath 文件路径
     * @return 返回无后缀文件名
     */
    public static String getFileNameWithoutSuffix(String filePath) {
        String fileName = getFileName(filePath);
        int suffixIdx = fileName.lastIndexOf(".") > -1 ? fileName.lastIndexOf(".") : fileName.length();
        return fileName.substring(0, suffixIdx);
    }

    /**
     * 根据文件路径获取文件后缀名
     *
     * @param filePath 文件路径
     * @return 返回文件后缀名
     */
    public static String getFileSuffixName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("."));
    }

    /**
     * 创建json文件
     *
     * @param jsonList json对象列表
     * @param destDir  存储目录
     * @param destDir  存储文件名
     * @return 返回文件路径
     */
    public static String createJsonFileByJsonLst(List<Object> jsonList, String destDir, String destFileName) {
        String fileName = null;
        Path path = Paths.get(destDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                logger.error("创建目录失败[{}]", e.toString());

            }
        }
        fileName = destDir + File.separator + destFileName; //生存文件路径
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(fileName));) {
            bw.write(Jackson.getJson().toJson(jsonList));
            bw.flush();
        } catch (Exception e) {
            logger.error("生成json文件失败[{}]", e.toString());
        }
        return fileName;
    }

    /**
     * 创建json文件
     *
     * @param jsonMap json对象
     * @param destDir 存储目录
     * @param destDir 存储文件名
     * @return 返回文件路径
     */
    public static String createJsonFileByJsonMap(Map jsonMap, String destDir, String destFileName) {
        Path fileName = createJsonFileByJsonMap(jsonMap, Paths.get(destDir), destFileName);
        if (fileName == null) {
            return null;
        }
        return fileName.toString();
    }

    /**
     * 创建json文件
     *
     * @param jsonMap json对象
     * @param destDir 存储目录
     * @param destDir 存储文件名
     * @return 返回文件路径
     */
    public static Path createJsonFileByJsonMap(Map jsonMap, Path destDir, String destFileName) {
        Path fileName;
        Path path = destDir;
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                logger.error("创建目录失败[{}]", e.toString());

            }
        }
        //生存文件路径
        fileName = Paths.get(destDir.toString(), destFileName);
        try (BufferedWriter bw = Files.newBufferedWriter(fileName)) {
            bw.write(Jackson.getJson().toJson(jsonMap));
            bw.flush();
        } catch (Exception e) {
            logger.error("生成json文件失败[{}]", e.toString());
            return null;
        }
        return fileName;
    }

    /**
     * 创建json文件
     *
     * @param obj
     * @param destDir
     * @param destFileName
     * @return
     */
    public static String createJsonFileByJsonObj(Object obj, String destDir, String destFileName) {
        String fileName = null;
        Path path = Paths.get(destDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                logger.error("创建目录失败[{}]", e.toString());
            }
        }
        fileName = destDir + File.separator + destFileName; //生存文件路径
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(fileName));) {
            bw.write(Jackson.getJson().toJson(obj));
            bw.flush();
        } catch (Exception e) {
            logger.error("生成json文件失败[{}]", e.toString());
            return null;
        }
        return fileName;
    }

    public static boolean createFile(String fileName, FileInputStream inputStream, String outPath) {
        try {
            //判断文件是否存在
            if (!Files.exists(Paths.get(outPath))) {
                Files.createDirectories(Paths.get(outPath));
            }
            String fileOutPath = outPath + File.separator + fileName;
            FileOutputStream fos = new FileOutputStream(fileOutPath);
            byte[] buf1 = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buf1)) > 0) {
                fos.write(buf1, 0, len);
            }
            inputStream.close();
            fos.close();
        } catch (IOException e) {
            logger.error("创建文件失败[{}]", e.toString());
            return false;
        }
        return true;
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @param data
     * @return
     */
    public static boolean createFile(Path filePath, byte[] data) {
        try {
            Files.write(filePath, data);
        } catch (IOException e) {
            logger.error("创建文件失败[{}]", e.toString());
            return false;
        }
        return true;
    }

    /**
     * 追加文件
     *
     * @param filePath
     * @param data
     * @return
     */
    public static boolean appendFile(Path filePath, byte[] data) {
        try {
            //判断文件是否存在
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath);
            }
            Files.write(filePath, data, StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("追加文件失败[{}]", e.toString());
            return false;
        }
        return true;
    }

    /**
     * 递归解压
     *
     * @param zipFile
     * @param descDir
     * @return 解压后的根路径
     * @throws IOException
     */
    public static String iteratorUnZip(File zipFile, String descDir) throws IOException {
        //解压后的根目录
        String unZipRootPath = zipFile.getPath().substring(0, zipFile.getPath().lastIndexOf(ZIP_SUFFIX)) + File.separator; //获取解压后的根路径
        List<Path> resultLst = new ArrayList();
        //解压文件
        descDir = unZipFiles(zipFile, descDir);
        logger.info("递归解压的目录[{}]", descDir);
        //查找解压后文件中的zip文件，继续解压
        findFiles(descDir, "*.zip", "", resultLst);
        for (Path filePath : resultLst) {
            String filePathStr = filePath.toString();
            if (filePathStr.lastIndexOf(ZIP_SUFFIX) > -1) {
                String tempDir = getFileDir(filePathStr);
                iteratorUnZip(filePath.toFile(), tempDir);
                Files.delete(filePath);
            }
        }
        return unZipRootPath;
    }

    /**
     * 递归解压
     *
     * @param zipFile
     * @param descDir
     * @return 解压后的根路径
     * @throws IOException
     */
    public static String iteratorUnZip(String zipFile, String descDir) throws IOException {
        //解压后的根目录
        String unZipRootPath = getFileDir(zipFile); //获取解压后的根路径
        List<Path> resultLst = new ArrayList();
        //解压文件
        descDir = unZipFiles(zipFile, descDir);
        //查找解压后文件中的zip文件，继续解压
        findFiles(descDir, "*.zip", "", resultLst);
        for (Path filePath : resultLst) {
            String filePathStr = filePath.toString();
            if (filePathStr.lastIndexOf(ZIP_SUFFIX) > -1) {
                String tempDir = getFileDir(filePathStr);
                iteratorUnZip(filePathStr, tempDir);
            }
        }
        return unZipRootPath;
    }

    /**
     * 解压文件到指定目录
     * 解压后的文件名，和之前一致
     *
     * @param zipFile 待解压的zip文件
     * @param descDir 指定目录
     */
    @SuppressWarnings("rawtypes")
    public static String unZipFiles(String zipFile, String descDir) throws IOException {
        return unZipFiles(zipFile, descDir, true);
    }

    /**
     * 解压文件到指定目录
     * 解压后的文件名，和之前一致
     *
     * @param zipFile 待解压的zip文件
     * @param descDir 指定目录
     */
    @SuppressWarnings("rawtypes")
    public static String unZipFiles(File zipFile, String descDir) throws IOException {
        return unZipFiles(zipFile, descDir, true);
    }

    /**
     * 解压文件到指定目录
     * 解压后的文件名，和之前一致
     *
     * @param zipFile             待解压的zip文件
     * @param descDir             指定目录
     * @param isCreateFileNameDir 是否创建文件名目录
     * @return 返回解压文件的目录
     */
    @SuppressWarnings("rawtypes")
    public static String unZipFiles(File zipFile, String descDir, boolean isCreateFileNameDir) throws IOException {
        String tempSuff = ZIP_TEMP_SUFFIX; //临时目录后缀
        ZipFile zip = new ZipFile(zipFile, Charset.forName("UTF-8"));//解决中文文件夹乱码
        //获取zip文件无后缀名
        String name = getFileNameWithoutSuffix(zip.getName());
        String filePathName = isCreateFileNameDir ? descDir + File.separator + name : descDir;

        final String realFilePathName = filePathName; //保存正式文件夹名
        filePathName += tempSuff;
        if (!Files.exists(Paths.get(filePathName))) {
            Files.createDirectories(Paths.get(filePathName));
        }

        for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
//            String outPath = (filePathName + File.separator + getFileName(zipEntryName)).replaceAll("\\*", File.separator);

            Path zipPath = Paths.get(filePathName, zipEntryName);
            //判断是否是目录
            if (entry.isDirectory()) {
                // 判断路径是否存在,不存在则创建文件路径
                if (Files.notExists(zipPath)) {
                    Files.createDirectories(zipPath);
                }
            } else {
                //创建文件
                InputStream in = zip.getInputStream(entry);
                FileOutputStream out = new FileOutputStream(zipPath.toString());
                try {
                    byte[] buf1 = new byte[bufferSize];
                    int len;
                    while ((len = in.read(buf1)) > 0) {
                        out.write(buf1, 0, len);
                    }
                } finally {
                    //关闭文件流
                    in.close();
                    out.close();
                }
            }

//            File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//
//            File outFile = new File(outPath);
//            //如果entry是目录则创建文件夹
//            if (entry.isDirectory()) {
//                outFile.mkdirs();
//            }

//            // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
//            if (outFile.isDirectory()) {
//                continue;
//            }

        }
        zip.close();

        Path filePath = Paths.get(filePathName);

        //将临时文件目录移回正式目录
        Files.walkFileTree(filePath, new SimpleFileVisitor<Path>() {

            //正式目录根节点NameCount
            private int rootCount;

            {
                rootCount = filePath.getNameCount();
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                int fileCount = file.getNameCount();
                StringBuilder dest = new StringBuilder(realFilePathName);
                for (int start = rootCount; start < fileCount - 1; start++) {
                    dest.append(File.separator).append(file.getName(start).toString());
                }

                moveFile(file, dest.toString());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return FileVisitResult.CONTINUE;
            }
        });
        return realFilePathName;
    }

    /**
     * 解压文件到指定目录
     * 解压后的文件名，和之前一致
     *
     * @param zipFile             待解压的zip文件
     * @param descDir             指定目录
     * @param isCreateFileNameDir 是否创建文件名目录
     * @return 返回解压结果
     */
    public static String unZipFiles(String zipFile, String descDir, boolean isCreateFileNameDir) {
        String tempSuff = ZIP_TEMP_SUFFIX; //临时目录后缀
        String filePathName = "";
        String realFilePathName = "";
        try (
                ZipFile zip = new ZipFile(zipFile, Charset.forName("UTF-8"));
        ) {
            //获取zip文件无后缀名
            String name = getFileNameWithoutSuffix(zip.getName());
            filePathName = isCreateFileNameDir ? descDir + File.separator + name : descDir;

            final String finalRealFilePathName = filePathName; //保存正式文件夹名
            realFilePathName = finalRealFilePathName;

            filePathName += tempSuff;
            Path filePath = Paths.get(filePathName);
            //创建解压文件目录
            if (!Files.exists(filePath)) {
                Files.createDirectory(filePath);
            }

            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                String outPath = (filePathName + File.separator + getFileName(zipEntryName)).replaceAll("\\*", File.separator);

                Path outPathDir = Paths.get(getFileDir(outPath)); //文件目录
                // 判断路径是否存在,不存在则创建文件路径
                if (!Files.exists(outPathDir)) {
                    Files.createDirectory(outPathDir);
                }
                // 判断文件全路径是否为文件夹,如果是上面已经存在,不需要解压
                if (Files.isDirectory(Paths.get(outPath))) {
                    continue;
                }

                //执行解压文件操作
                try (
                        InputStream in = zip.getInputStream(entry);
                        FileOutputStream out = new FileOutputStream(outPath);
                ) {
                    byte[] buf1 = new byte[bufferSize];
                    int len;
                    while ((len = in.read(buf1)) > 0) {
                        out.write(buf1, 0, len);
                    }
                }
            }

            //将临时文件目录移回正式目录
            Files.walkFileTree(Paths.get(filePathName), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    moveFile(file, finalRealFilePathName);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.deleteIfExists(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            logger.error("解压文件异常[{}]", e.toString());
        }

        return realFilePathName;
    }

    /**
     * 压缩指定文件集合到一个目标zipPath中
     *
     * @param files
     * @param zipFileName
     * @param zipPath
     * @return
     */
    public Path zipFiles(File[] files, String zipFileName, Path zipPath) {
        if (files == null || files.length == 0) {
            return null;
        }
        Path zipFilePath = Paths.get(zipPath.toString(), zipFileName + ZIP_SUFFIX);
        try (FileOutputStream fos = new FileOutputStream(zipFilePath.toString());
             ZipOutputStream zos = new ZipOutputStream(fos);) {
            for (File file : files) {
                zos.putNextEntry(new ZipEntry(file.getName()));
                zos.write(Files.readAllBytes(file.toPath()));
                zos.closeEntry();
            }
        } catch (IOException e) {
            logger.error("压缩文件异常:[" + Arrays.toString(files) + "] ", e);
            try {
                Files.deleteIfExists(zipFilePath);
            } catch (Exception e1) {
                //ignore
            }
            return null;
        }
        return zipFilePath;
    }

    /**
     * 将文件打包为zip文件
     *
     * @param fileName 文件名
     * @param outPath  输出路径
     * @return 返回打包结果
     */
    public static boolean zipFile(String fileName, String outPath) {
        return zipFile(fileName, Paths.get(fileName), outPath);
    }

    /**
     * 将文件打包为zip文件
     *
     * @param fileName 文件名
     * @param outPath  输出路径
     * @return 返回打包结果
     */
    public static boolean zipFile(String fileName, Path outPath) {
        return zipFile(fileName, Paths.get(fileName), outPath);
    }

    /**
     * 将文件打包为zip文件
     *
     * @param fileName 文件名
     * @param sourPath 源文件
     * @param outPath  输出路径
     * @return 返回打包结果
     */
    public static boolean zipFile(String fileName, Path sourPath, String outPath) {
        return zipFile(fileName, sourPath, Paths.get(outPath));
    }

    /**
     * 将文件打包为zip文件
     *
     * @param fileName 文件名
     * @param sourPath 源文件
     * @param outPath  输出路径
     * @return 返回打包结果
     */
    public static boolean zipFile(String fileName, Path sourPath, Path outPath) {
        if (zipFileForPath(fileName, sourPath, outPath) == null) {
            return false;
        }
        return true;
    }

    /**
     * 将文件打包为zip文件,并返回压缩文件路径
     *
     * @param fileName 文件名
     * @param sourPath 源文件路径
     * @param outPath  输出目录路径
     * @return 返回压缩文件路径
     */
    public static Path zipFileForPath(String fileName, Path sourPath, Path outPath) {
        String zipSuffix = ZIP_SUFFIX;
        String tempSuffix = ZIP_TEMP_SUFFIX;
        String fileRealName = "";
        Path zipFileName, tempZipFileName;
        if (Files.isDirectory(sourPath)) {
            logger.error("请检查目标文件，它不应该是一个目录");
            return null;
        }
        fileRealName = getFileName(fileName);
        tempZipFileName = Paths.get(outPath.toString(), getFileNameWithoutSuffix(fileRealName).concat(tempSuffix));
        zipFileName = Paths.get(tempZipFileName.toString().replace(tempSuffix, zipSuffix));
        //判断压缩输出目录是否存在
        if (!Files.exists(outPath)) {
            try {
                Files.createDirectories(outPath);
            } catch (IOException e) {
                logger.error("创建压缩输出目录失败[{}]", e.toString());
                return null;
            }
        }
        try (
                FileOutputStream fos = new FileOutputStream(tempZipFileName.toString());
                ZipOutputStream zos = new ZipOutputStream(fos);
                FileInputStream fis = new FileInputStream(sourPath.toFile())
        ) {
            zos.putNextEntry(new ZipEntry(fileRealName));
            byte[] bytes = new byte[bufferSize];
            int len = 0;
            while ((len = fis.read(bytes)) > 0) {
                zos.write(bytes, 0, len);
            }
        } catch (FileNotFoundException ex) {
            logger.error("压缩文件时，文件[{}]不存在", sourPath.getFileName());
            return null;
        } catch (IOException ex) {
            logger.error("I/O error: [{}]", ex);
            return null;
        }

        //压缩成功后将文件改名为.zip
        try {
            Files.move(tempZipFileName, zipFileName, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("修改压缩文件名异常:[{}] ", e);
            return null;
        }
        return zipFileName;
    }


    /**
     * 对二进制流进行Zip压缩
     *
     * @param data
     * @param fileName
     * @return
     */
    public static byte[] zip(byte[] data, String fileName) {
        byte[] b = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ZipOutputStream zip = new ZipOutputStream(bos)
        ) {
            ZipEntry entry = new ZipEntry(fileName);
            entry.setSize(data.length);
            zip.putNextEntry(entry);
            zip.write(data);
            zip.closeEntry();
            zip.close();
            b = bos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
    }

    /**
     * 压缩二进制文件
     *
     * @param data
     * @return
     */
    public static byte[] gZip(byte[] data) {
        byte[] b = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data);
            gzip.finish();
            gzip.close();
            b = bos.toByteArray();
            bos.close();
        } catch (Exception ex) {
            logger.error("压缩二进制文件失败");
        }
        return b;
    }


    /**
     * 将目录打包为zip文件
     *
     * @param pathName 目标路径（可以是文件或目录）
     * @param outPath  输出路径
     * @return 返回打包结果
     */
    public static boolean zipPath(String pathName, String outPath) {
        String zipSuffix = ZIP_SUFFIX;
        String tempSuffix = ZIP_TEMP_SUFFIX;
        Path path = Paths.get(pathName);
        String fileRealName = "", zipFileName = "";
        if (!Files.isDirectory(path)) {
            //如果是文件就直接压缩
            return zipFile(pathName, outPath);
        }
        fileRealName = getFileName(path.toString());
        zipFileName = outPath + File.separator + fileRealName.concat(tempSuffix);
        try (
                FileOutputStream fos = new FileOutputStream(zipFileName);
                ZipOutputStream zos = new ZipOutputStream(fos);
        ) {
            Files.walkFileTree(path, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    //将文件压缩到zip文件中
                    FileInputStream fis = new FileInputStream(file.toFile());
                    //设置压缩文件中文件名
                    String entryName = file.toFile().getName();
                    zos.putNextEntry(new ZipEntry(entryName));
                    byte[] bytes = new byte[bufferSize];
                    int len = 0;
                    while ((len = fis.read(bytes)) > 0) {
                        zos.write(bytes, 0, len);
                    }
                    fis.close();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });

        } catch (IOException ex) {
            logger.error("I/O error:[{}]", ex);
            return false;
        }

        //压缩成功后将文件改名为.zip
        try {
            Files.move(Paths.get(zipFileName), Paths.get(zipFileName.replace(tempSuffix, zipSuffix)),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("修改压缩文件名异常:[{}] ", e);
            return false;
        }
        return true;
    }

    /**
     * 将目录打包为zip文件
     *
     * @param pathName 目标路径（可以是文件或目录）
     * @param outPath  输出路径
     * @return 返回打包结果
     */
    public static boolean zipPathWithoutTemp(String pathName, String outPath) {
        String zipSuffix = ZIP_SUFFIX;
        Path path = Paths.get(pathName);
        String fileRealName = "", zipFileName = "";
        if (!Files.isDirectory(path)) {
            //如果是文件就直接压缩
            return zipFile(pathName, outPath);
        }
        fileRealName = getFileName(path.toString());
        zipFileName = outPath + File.separator + fileRealName.concat(zipSuffix);
        try (
                FileOutputStream fos = new FileOutputStream(zipFileName);
                ZipOutputStream zos = new ZipOutputStream(fos);
        ) {
            Files.walkFileTree(path, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    //将文件压缩到zip文件中
                    FileInputStream fis = new FileInputStream(file.toFile());
                    //设置压缩文件中文件名
                    String entryName = file.toString().replace(pathName + File.separator, "");
                    zos.putNextEntry(new ZipEntry(entryName));
                    byte[] bytes = new byte[bufferSize];
                    int len = 0;
                    while ((len = fis.read(bytes)) > 0) {
                        zos.write(bytes, 0, len);
                    }
                    fis.close();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });

        } catch (IOException ex) {
            logger.error("I/O error:[{}]", ex);
            return false;
        }
        return true;
    }

    /**
     * 递归查找文件
     *
     * @param baseDirName    目标目录
     * @param targetFileName 目标文件名
     * @param fileList<File> 返回文件对象列表
     */
    public static void findSubFiles(String baseDirName, String targetFileName, List<File> fileList) {
        File baseDir = Paths.get(baseDirName).toFile();       // 创建一个File对象
        if (!baseDir.exists() || !baseDir.isDirectory()) {  // 判断目录是否存在
            logger.error("文件查找失败：" + baseDirName + "不是一个目录！");
        }
        String tempName = null;
        //判断目录是否存在
        File tempFile;
        File[] files = baseDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            tempFile = files[i];
            if (tempFile.isDirectory()) {
                findSubFiles(tempFile.getAbsolutePath(), targetFileName, fileList);
            } else if (tempFile.isFile()) {
                tempName = tempFile.getName();
                if (wildcardMatch(targetFileName, tempName)) {
                    // 匹配成功，将文件名添加到结果集
                    fileList.add(tempFile.getAbsoluteFile());
                }
            }
        }
    }

    /**
     * NIO方式查找文件
     *
     * @param baseDirName    目标目录
     * @param targetFileName 目标文件名
     * @param fileList<Path> 返回文件路径列表
     */
    public static void findFiles(String baseDirName, String targetFileName, List<Path> fileList) throws IOException {
        Files.walkFileTree(Paths.get(baseDirName), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("正在访问：[" + dir + "]目录");
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String tempName = file.getFileName().toString();
                if (wildcardMatch(targetFileName, tempName)) {
                    // 匹配成功，将文件名添加到结果集
                    fileList.add(file.toAbsolutePath());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * 递归查找文件
     *
     * @param baseDirName     查找的文件夹路径
     * @param targetFileName  需要查找的文件名
     * @param excludeFileName 需要排除的文件名，为空则不排除
     * @param fileList<Path>  查找到的文件集合
     */
    public static void findFiles(String baseDirName, String targetFileName, String excludeFileName, List<Path> fileList) throws IOException {
        Files.walkFileTree(Paths.get(baseDirName), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String tempName = file.getFileName().toString();
                if (StringUtil.isEmpty(excludeFileName)) {
                    if (wildcardMatch(targetFileName, tempName)) {
                        // 匹配成功，将文件名添加到结果集
                        fileList.add(file);
                    }
                } else {
                    if (!wildcardMatch(excludeFileName, tempName) && wildcardMatch(targetFileName, tempName)) {
                        // 匹配成功，将文件名添加到结果集
                        fileList.add(file);
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * 通配符匹配
     *
     * @param pattern 通配符模式
     * @param str     待匹配的字符串
     * @return 匹配成功则返回true，否则返回false
     */
    private static boolean wildcardMatch(String pattern, String str) {
        int patternLength = pattern.length();
        int strLength = str.length();
        int strIndex = 0;
        char ch;
        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
            ch = pattern.charAt(patternIndex);
            if (ch == '*') {
                //通配符星号*表示可以匹配任意多个字符
                while (strIndex < strLength) {
                    if (wildcardMatch(pattern.substring(patternIndex + 1),
                            str.substring(strIndex))) {
                        return true;
                    }
                    strIndex++;
                }
            } else if (ch == '?') {
                //通配符问号?表示匹配任意一个字符
                strIndex++;
                if (strIndex > strLength) {
                    //表示str中已经没有字符匹配?了。
                    return false;
                }
            } else {
                if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
                    return false;
                }
                strIndex++;
            }
        }
        return (strIndex == strLength);
    }

    /**
     * 递归删除文件
     *
     * @param dir 文件目录
     */
    public static void delDir(File dir) {
        // 判断是否是一个目录, 不是的话跳过, 直接删除; 如果是一个目录, 先将其内容清空.
        if (dir.isDirectory()) {
            // 获取子文件/目录
            File[] subFiles = dir.listFiles();
            // 遍历该目录
            for (File subFile : subFiles) {
                // 递归调用删除该文件: 如果这是一个空目录或文件, 一次递归就可删除. 如果这是一个非空目录, 多次
                // 递归清空其内容后再删除
                delDir(subFile);
            }
        }
        // 删除空目录或文件
        dir.delete();
    }

    /**
     * 递归删除文件
     *
     * @param dir 文件目录
     */
    public static boolean delDir(String dir) throws IOException {
        Files.walkFileTree(Paths.get(dir), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                //删除文件
                Files.deleteIfExists(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    throw exc;
                }
            }
        });
        return true;
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return 返回是否删除成功
     */
    public static boolean delFile(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            logger.error("删除文件失败[{}]", e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return 返回是否删除成功
     */
    public static boolean delFile(Path fileName) {
        try {
            Files.deleteIfExists(fileName);
        } catch (IOException e) {
            logger.error("删除文件[{}]失败[{}]", fileName.toString(), e.toString(), e);
            return false;
        }
        return true;
    }

    /**
     * 移动文件
     *
     * @param fileName 原文件
     * @param destDir  目标目录
     * @return 返回是否移动成功
     */
    public static boolean moveFile(String fileName, String destDir) throws IOException {
        return moveFile(Paths.get(fileName), destDir);
    }

    /**
     * 移动文件
     *
     * @param fileNamePath 原文件
     * @param destDir      目标目录
     * @return 返回是否移动成功
     */
    public static boolean moveFile(Path fileNamePath, String destDir) throws IOException {
        return moveFile(fileNamePath, Paths.get(destDir));
    }

    /**
     * 移动文件
     *
     * @param fileNamePath 原文件
     * @param destDir      目标目录
     * @return 返回是否移动成功
     */
    public static boolean moveFile(Path fileNamePath, Path destDir) throws IOException {
        //判断目标路径是否有目录
        Path dest = destDir;
        if (!Files.exists(dest)) {
            Files.createDirectories(dest);
        }
        if (Files.isDirectory(dest)) {
            Path destDirPath = Paths.get(destDir + File.separator + getFileName(fileNamePath.toString()));
            //如果源文件为目录，则删除目标目录的文件
            if (Files.isDirectory(fileNamePath)) {
                if (Files.exists(destDirPath)) {
                    delDir(destDirPath.toFile());
                }
            }
            Files.move(fileNamePath, destDirPath,
                    StandardCopyOption.REPLACE_EXISTING);
        } else {
            logger.error("移动文件失败，目标目录不能是文件");
            return false;
        }
        return true;
    }

    /**
     * 移动文件
     *
     * @param fileNamePath 原文件
     * @param fileName     移动后的文件名
     * @param destDir      目标目录
     * @return
     * @throws IOException
     */
    public static boolean moveFile(Path fileNamePath, String fileName, String destDir) throws IOException {
        //判断目标路径是否有目录
        Path dest = Paths.get(destDir);
        if (!Files.exists(dest)) {
            Files.createDirectories(dest);
        }
        if (Files.isDirectory(dest)) {
            Path destDirPath = Paths.get(destDir, fileName);
            //如果源文件为目录，则删除目标目录的文件
            if (Files.isDirectory(fileNamePath)) {
                if (Files.exists(destDirPath)) {
                    delDir(destDirPath.toFile());
                }
            }
            Files.move(fileNamePath, destDirPath,
                    StandardCopyOption.REPLACE_EXISTING);
        } else {
            logger.error("移动文件失败，目标目录不能是文件");
            return false;
        }
        return true;
    }

    public static boolean moveFile(File srcFile, String dest) throws IOException {
        Path destDir = Paths.get(getFileDir(dest));

        if (!Files.exists(destDir)) {
            Files.createDirectories(destDir);
        }
        srcFile.renameTo(Paths.get(dest).toFile());
        return true;
    }


    /**
     * 创建新文件，文件已经存在，返回true，打印日志
     *
     * @param path
     * @return
     */
    public static boolean createNewFile(Path path) {
        boolean result = true;
        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            logger.warn("ERROR: writeNewFile " + path.getFileName() + " error, FileAlreadyExists!!");
            result = true;
        } catch (IOException e) {
            result = false;
            logger.error("ERROR: createNewFile {}:{}", path.getFileName(), e.getMessage());
//            XAssert.afterExceptionContinue(e, "ERROR: createNewFile " + path.getName());
        }
        return result;
    }

    /**
     * 将文件复制至指定目录，若目录不存在 ，则自动创建；若文件已经存在则进行替换；
     *
     * @param file
     * @param targetDir
     * @throws IOException
     */
    public static void copyFileToTargetDir(String description, Path file, Path targetDir) throws IOException {
        if (Files.notExists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        Path target = Paths.get(targetDir.toString(), file.getFileName().toString());
        Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 判断目录是否为空
     * 这里的空目录包括：目录中包含的空目录，但是是没有文件的
     *
     * @param dir
     * @return
     */
    public static boolean isEmptyDir(Path dir) throws IOException {
        if (Files.isDirectory(dir)) {
            File[] files = dir.toFile().listFiles();
            if (files.length == 0) {
                return true;
            } else {
                for (File file : files) {
                    if (file.isDirectory()) {
                        return isEmptyDir(file.toPath());
                    } else {
                        return false;
                    }
                }
            }
        } else {
            throw new RuntimeException("当前文件不是一个目录");
        }
        return false;
    }

    /**
     * 在不解压的情况下读取zip文件中的数据
     *
     * @param file
     * @return
     */
    public static Map<String, String> readFromZip(Path file) {
        Map<String, String> readMap = new HashMap<>(16);
        try (ZipFile zip = new ZipFile(file.toFile())) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry), Charset.forName("utf-8")))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                        readMap.put(entry.getName(), builder.toString());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return readMap;
    }

    /**
     * 创建目录
     *
     * @param dir
     */
    public static void mkdirs(String dir) {
        File localFile = new File(dir);
        if ((!localFile.exists()) || (!localFile.isDirectory())) {
            synchronized (lock) {
                if ((localFile.exists()) && (localFile.isDirectory())) {
                    return;
                }
                boolean bool = localFile.mkdirs();
                if (!bool) {
                    throw new RuntimeException("创建文件夹失败,请查看application server权限设置path = " + dir);
                }
            }
        }
    }

    /**
     * 检查目录
     * 1 判断目录是否存在
     * 2 不存在则创建，存在则跳过
     */
    public static boolean checkDir(Path... paths) {
        if (paths == null || paths.length == 0) {
            return true;
        }
        for (Path p : paths) {
            try {
                if (!Files.exists(p) || Files.isRegularFile(p)) {
                    Path directories = Files.createDirectories(p);
                    logger.info("创建目录[{}]成功", directories);
                }
            } catch (IOException e) {
                logger.error("创建目录[" + p.toString() + "]异常", e);
                return false;
            }
        }
        return true;
    }
}
