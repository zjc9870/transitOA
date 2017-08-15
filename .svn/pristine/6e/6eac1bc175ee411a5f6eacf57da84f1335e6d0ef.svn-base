package com.expect.admin.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

/**
 * md5加密 非对称
 * @author zcz
 *
 */
public class MD5Util extends MessageDigestPasswordEncoder{
	
	public MD5Util(String algorithm) {
		super(algorithm);
	}
	
//	@Override
//	public boolean isPasswordValid(String savePass, String submitPass,  
//	        Object salt) {
//		String encodeSubmitPass = getMD5String(submitPass);
//		return StringUtil.equals(savePass, encodeSubmitPass);
//	}

	private static final Logger log =LoggerFactory.getLogger(MD5Util.class);
	
	private static char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
					'e', 'f' };
	private static final String key = "211bfd7efbdbe2d43fceb328969cb15e";
	/**
	 * 获取MD5加密后的字符串
	 * @param originalData 原始数据
	 * @return 加密后的数据
	 */
	public static String getMD5String(String originalData) {
		if(StringUtil.isBlank(originalData)) return "";
		String s = null;
		String data = originalData + key;
		try {
			byte[] source = data.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			byte[] tem = md.digest();// MD5 的计算结果是一个 128 位的长整数，用字节表示就是 16 个字节
			char[] str = new char[16 * 2];// 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tem[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 取字节中高 4 位的数字转换
				str[k++] = hexDigits[byte0 & 0xf];// 取字节中低 4 位的数字转换
			}
			s = new String(str);
		//永远不会发生
		} catch (UnsupportedEncodingException e1) {
			log.error(e1.toString());
		}
		//永远不会发生
		catch (NoSuchAlgorithmException e) {
			log.error(e.toString());
		}
		catch(Exception e2) {
			log.error("获取MD5加密结果出错" ,e2);
		}
		return s;
	}
	
//	public static void main(String[] args) {
//		String a = "1234";
//		String b = "12";
//		String c = new String(a);
//		long start = System.currentTimeMillis();
//		String aMd5 = getMD5String(a);
//		String bMd5 = getMD5String(b);
//		String cMd5 = getMD5String(c);
//		long end = System.currentTimeMillis();
//		System.out.println(end - start);
//		System.out.println(aMd5);
//		System.out.println(bMd5);
//		System.out.println(cMd5);
//		System.out.println(StringUtil.equals(aMd5, bMd5));
//		System.out.println(StringUtil.equals(aMd5, cMd5));
//	}
}
