package com.expect.admin.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class IOUtil {

	/**
	 * 输出文件
	 */
	public static boolean outputDataToFile(byte[] buffer, String pathDir, String fileName) {
		FileOutputStream fos = null;
		try {
			File fileDir = new File(pathDir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			File file = new File(fileDir, fileName);

			fos = new FileOutputStream(file);
			fos.write(buffer);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	// 保存数据到指定的目录
	public static void writerDataToFile(String path, String data) {
		File file = new File(path);
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			fileWriter.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 保存数据到指定的目录
	public static void writerDataToFileAppend(String path, String data) {
		File file = new File(path);
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file, true);
			fileWriter.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取保存的数据，字符流
	 */
	public static String readerDataFromFile(String path) {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			StringBuffer sb = new StringBuffer();
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 读取保存的数据，字节流
	 */
	public static byte[] inputDataFromFile(String path) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(path));
			byte[] buff = new byte[fis.available()];
			fis.read(buff);
			return buff;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static File saveFile(InputStream is, String destFileDir, String destFileName) {
		byte[] buf = new byte[2048];
		int len = 0;
		FileOutputStream fos = null;
		try {
			File dir = new File(destFileDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, destFileName);
			fos = new FileOutputStream(file);
			while ((len = is.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			fos.flush();
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
