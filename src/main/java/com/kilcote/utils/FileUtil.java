package com.kilcote.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.kilcote.common.ConstantAdminTemplate;
import com.kilcote.common.Global;
import com.kilcote.common.constants.StringConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Shun fu
 */
@Slf4j
public class FileUtil {

	private static final int BUFFER = 1024 * 8;

	/**
	 * Compress files or directories
	 *
	 * @param fromPath File or path to be compressed
	 * @param toPath Compressed files, such as xx.zip
	 */
	public static void compress(String fromPath, String toPath) throws IOException {
		File fromFile = new File(fromPath);
		File toFile = new File(toPath);
		if (!fromFile.exists()) {
			throw new FileNotFoundException(fromPath + "does not exist!");
		}
		try (
				FileOutputStream outputStream = new FileOutputStream(toFile);
				CheckedOutputStream checkedOutputStream = new CheckedOutputStream(outputStream, new CRC32());
				ZipOutputStream zipOutputStream = new ZipOutputStream(checkedOutputStream)
				) {
			String baseDir = "";
			compress(fromFile, zipOutputStream, baseDir);
		}
	}

	/**
	 * download file
	 *
	 * @param filePath File path to be downloaded
	 * @param fileName Download file name
	 * @param delete   Whether to delete the source file after downloading
	 * @param response HttpServletResponse
	 * @throws Exception Exception
	 */
	public static void download(String filePath, String fileName, Boolean delete, HttpServletResponse response) throws Exception {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new Exception("file not found");
		}

		String fileType = getFileType(file);
		if (!fileTypeIsValid(fileType)) {
			throw new Exception("This type of file download is not currently supported");
		}
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=" + java.net.URLEncoder.encode(fileName, StringConstant.UTF_8));
		response.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
		response.setCharacterEncoding(StringConstant.UTF_8);
		try (InputStream inputStream = new FileInputStream(file); OutputStream os = response.getOutputStream()) {
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
		} finally {
			if (delete) {
				delete(filePath);
			}
		}
	}

	/**
	 * Delete files or directories recursively
	 *
	 * @param filePath File or directory
	 */
	public static void delete(String filePath) {
		File file = new File(filePath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				Arrays.stream(files).forEach(f -> delete(f.getPath()));
			}
		}
		file.delete();
	}

	/**
	 * Get file type
	 *
	 * @param file
	 * @return file type
	 * @throws Exception Exception
	 */
	private static String getFileType(File file) throws Exception {
		//Preconditions.checkNotNull(file);
		if (file.isDirectory()) {
			throw new Exception("file is not file");
		}
		String fileName = file.getName();
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	/**
	 * Verify that the file type is the type that allows downloading
	 * @param fileType
	 * @return
	 */
	private static Boolean fileTypeIsValid(String fileType) {
		//Preconditions.checkNotNull(fileType);
		fileType = StringUtils.lowerCase(fileType);
		return ArrayUtils.contains(ConstantAdminTemplate.VALID_FILE_TYPE, fileType);
	}

	private static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
		if (file.isDirectory()) {
			compressDirectory(file, zipOut, baseDir);
		} else {
			compressFile(file, zipOut, baseDir);
		}
	}

	private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException {
		File[] files = dir.listFiles();
		if (files != null && ArrayUtils.isNotEmpty(files)) {
			for (File file : files) {
				compress(file, zipOut, baseDir + dir.getName() + File.separator);
			}
		}
	}

	private static void compressFile(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
		if (!file.exists()) {
			return;
		}
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
			ZipEntry entry = new ZipEntry(baseDir + file.getName());
			zipOut.putNextEntry(entry);
			int count;
			byte[] data = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				zipOut.write(data, 0, count);
			}
		}
	}

	public static boolean saveAvatarImage(MultipartFile file, long id) throws Exception{
		File fileAvatar = Global.getAvatarFilePath(id);
		try {
			InputStream inputStream = file.getInputStream();
			Files.copy(inputStream, Paths.get(fileAvatar.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
