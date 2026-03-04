package com.atguigu.gmall.common.utils.file;

import com.atguigu.gmall.common.constant.RES_STATUS;
import com.atguigu.gmall.common.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 
 * @ClassName: FileUtils
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author shirui
 * @date 2018年5月28日 下午2:44:42
 *
 */
public class FileUtil {
	protected static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 
	 * @Title: identifySuffix @Description: 判断文件尾缀是否满足条件 @param @param
	 *         filePath @param @param expectedSuffix @param @return 设定文件 @return
	 *         boolean 返回类型 @throws
	 */
	public static boolean identifySuffix(String filePath, String expectedSuffix) {
		return filePath.endsWith(expectedSuffix);
	}

	/**
	 * 
	 * @Title: clear @Description: 删除File @param @param filePath 设定文件 @return
	 *         void 返回类型 @throws
	 */
	public static void deleteFile(String file) {
		File filePath = new File(file);

		if (!filePath.exists()) {
			logger.warn("文件地址{}不存在", file);
			throw new ServiceException(RES_STATUS.NO_FILE_ON_DISK);
		}

		if (filePath.isFile()) {
			filePath.delete();
		}
	}

	/**
	 * 
	 * @Title: clear @Description: 清空file路径下所有文件和目录 @param @param filePath
	 *         设定文件 @return void 返回类型 @throws
	 */
	public static void deleteDir(String file) {
		File filePath = new File(file);

		if (!filePath.exists()) {
			logger.warn("目录地址{}不存在", file);
			throw new ServiceException(RES_STATUS.NO_FILE_ON_DISK);
		}

		// 如果是文件，直接删除
		if (filePath.isFile()) {
			filePath.delete();
		}

		// 递归删除目录下所有文件
		if (filePath.isDirectory()) {
			File[] files = filePath.listFiles();
			for (File f : files) {
				deleteDir(f.getPath());
			}
			filePath.delete();
		}
	}

	/**
	 * 
	 * @Title: copyFile @Description: 拷贝文件到指定目录下 @param @param
	 *         sourcePath @param @param destinationDir @param @throws
	 *         IOException 设定文件 @return void 返回类型 @throws
	 */
	public static void copyFile(String sourcePath, String destinationDir) throws IOException {
		File sourceFile = new File(sourcePath);
		File destFile = new File(destinationDir);

		if (!sourceFile.exists() || !destFile.exists()) {
			logger.warn("源文件{}或者目标目录{}不存在", sourcePath, destinationDir);
			throw new ServiceException(RES_STATUS.NO_FILE_ON_DISK);
		}

		logger.info("拷贝文件{}到目录{} ", sourcePath, destinationDir);
		FileInputStream ins = new FileInputStream(sourcePath);
		FileOutputStream out = new FileOutputStream(destinationDir + "/" + sourceFile.getName());
		byte[] b = new byte[1024];
		int n = 0;
		while ((n = ins.read(b)) != -1) {
			out.write(b, 0, n);
		}

		ins.close();
		out.close();
	}

	/**
	 * 
	 * @Title: copyFile @Description: 目录拷贝 @param @param
	 *         sourcePath @param @param destinationPath @param @throws
	 *         IOException 设定文件 @return void 返回类型 @throws
	 */
	public static void copyDir(String sourcePath, String destinationPath) throws IOException {
		File sourceDir = new File(sourcePath);
		File destDir = new File(destinationPath);

		if (!sourceDir.exists() || !destDir.exists()) {
			logger.warn("源目录{}或者目标目录{}不存在", sourcePath, destinationPath);
			throw new ServiceException(RES_STATUS.NO_FILE_ON_DISK);
		}

		File[] files = sourceDir.listFiles();

		for (File file : files) {
			String name = file.getName();
			String destPath = destinationPath + "/" + name;

			if (file.isFile()) {
				FileInputStream ins = new FileInputStream(file);
				FileOutputStream out = new FileOutputStream(destPath);
				byte[] b = new byte[1024];
				int n = 0;
				while ((n = ins.read(b)) != -1) {
					out.write(b, 0, n);
				}
				ins.close();
				out.close();
			} else if (file.isDirectory()) {
				new File(destPath).mkdirs();
				copyDir(file.getPath(), destPath);
			}
		}

	}

	/** 递归构建文件目录，如果父目录不存在则创建父目录，如果父目录存在则直接构建文件目录 */
	public static void createDir(String dirPath) {

		File file = new File(dirPath);

		if ("/".equals(file.getName())) {
			logger.warn("输入目录不满足系统要求");
			throw new ServiceException(RES_STATUS.NO_FILE_ON_DISK);
		}

		if (file.getParentFile().exists()) {
			file.mkdirs();
		}

		if (!file.getParentFile().exists()) {
			createDir(file.getParent());
		}
	}

	/** 把String输出到指定文件 */
	public static void writeFile(String path, String msg) {
		try {
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
			osw.write(msg);
			osw.flush();
			osw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 根据根路径,文件名,folder层次构建指定文件的全路径*/
	public static String fullPathBuilder(String base, String filename, String... folder) {
		if (!base.endsWith("/")) {
			base = base + "/";
		}
		String buildFileName = "";
		if (StringUtils.isNotBlank(filename)) {
			buildFileName = filename;
		}
		if (folder == null || folder.length == 0) {
			return base + buildFileName;
		}

		StringBuilder baseBuilder = new StringBuilder(base);
		for (String each : folder) {
			if (each.endsWith("/")) {
				baseBuilder.append(each);
			} else {
				baseBuilder.append(each).append("/");
			}
		}
		base = baseBuilder.toString();
		return base + buildFileName;
	}
}
