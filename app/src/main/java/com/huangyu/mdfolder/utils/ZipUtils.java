package com.huangyu.mdfolder.utils;

import com.huangyu.mdfolder.bean.FileItem;
import com.hzy.lib7z.Un7Zip;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import net.lingala.zip4j.util.Zip4jUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;

/**
 * Created by huangyu on 2017/6/28.
 */

public class ZipUtils {

    private ZipUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 压缩文件
     */
    public static boolean zipFile(ArrayList<File> fileList, String toPath) {
        try {
            ZipFile zipFile = new ZipFile(toPath);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            zipFile.addFiles(fileList, parameters);
        } catch (ZipException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 解压文件
     *
     * @param zipFilePath 压缩文件路径
     * @param toPath      解压路径
     */
    public static boolean unZipFile(String zipFilePath, String toPath) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            zipFile.extractAll(toPath);
        } catch (ZipException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 解压文件
     *
     * @param zipFilePath 压缩文件路径
     * @param toPath      解压路径
     */
    public static boolean unZipFile(String zipFilePath, String toPath, String password) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(toPath);
        } catch (ZipException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 解压文件
     *
     * @param zipFilePath 压缩文件路径
     * @param toPath      解压路径
     */
    public static boolean unRarFile(String zipFilePath, String toPath) {
        try {
            unRar(new File(zipFilePath), toPath);
        } catch (RarException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void unRar(File rarFilePathFile, String toPath) throws RarException, IOException {
        FileOutputStream fileOut;
        File file;
        Archive rarFile = new Archive(rarFilePathFile);
        de.innosystec.unrar.rarfile.FileHeader entry = rarFile.nextFileHeader();
        while (entry != null) {
            String entryPath;
            if (entry.isUnicode()) {
                entryPath = entry.getFileNameW().trim();
            } else {
                entryPath = entry.getFileNameString().trim();
            }
            entryPath = entryPath.replaceAll("\\\\", "/");
            file = new File(toPath + "/" + entryPath);
            if (entry.isDirectory()) {
                file.mkdirs();
            } else {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                fileOut = new FileOutputStream(file);
                rarFile.extractFile(entry, fileOut);
                fileOut.close();
            }
            entry = rarFile.nextFileHeader();
        }
        rarFile.close();
    }

    /**
     * 解压文件
     *
     * @param zipFilePath 压缩文件路径
     * @param toPath      解压路径
     */
    public static boolean un7zipFile(String zipFilePath, String toPath) {
        return Un7Zip.extract7z(zipFilePath, toPath);
    }

    /**
     * zip文件是否加密
     *
     * @param zipFilePath 压缩文件路径
     * @return
     */
    public static boolean isEncrypted(String zipFilePath) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            return zipFile.isEncrypted();
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 显示zip文件内容
     *
     * @param zipFilePath 压缩文件路径
     * @return
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<FileItem> listFiles(String zipFilePath) {
        ArrayList<FileItem> fileItemList = new ArrayList<>();
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            List<FileHeader> fileHeaderList = zipFile.getFileHeaders();
            FileItem fileItem;
            for (FileHeader fileHeader : fileHeaderList) {
                fileItem = new FileItem();
                fileItem.setName(fileHeader.getFileName());
                fileItem.setDate(String.valueOf(Zip4jUtil.dosToJavaTme(fileHeader.getLastModFileTime())));
                fileItem.setSize(String.valueOf(fileHeader.getUncompressedSize()));
                fileItemList.add(fileItem);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return fileItemList;
    }

}