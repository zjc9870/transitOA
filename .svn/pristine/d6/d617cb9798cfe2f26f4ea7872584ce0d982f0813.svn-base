/**
 * 鐎电懓鍙曟导妤�閽╅崣鏉垮絺闁胶绮伴崗顑跨船鐠愶箑褰块惃鍕Х閹垰濮炵憴锝呯槕缁�杞扮伐娴狅絿鐖�.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package me.aes;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * SHA1 class
 *
 * 鐠侊紕鐣婚崗顑跨船楠炲啿褰撮惃鍕Х閹垳顒烽崥宥嗗复閸欙拷.
 */
class SHA1 {

	/**
	 * 閻⑩娍HA1缁犳纭堕悽鐔稿灇鐎瑰鍙忕粵鎯ф倳
	 * @param token 缁併劍宓�
	 * @param timestamp 閺冨爼妫块幋锟�
	 * @param nonce 闂呭繑婧�鐎涙顑佹稉锟�
	 * @param encrypt 鐎靛棙鏋�
	 * @return 鐎瑰鍙忕粵鎯ф倳
	 * @throws AesException 
	 */
	public static String getSHA1(String token, String timestamp, String nonce, String encrypt) throws AesException
			  {
		try {
			String[] array = new String[] { token, timestamp, nonce, encrypt };
			StringBuffer sb = new StringBuffer();
			// 鐎涙顑佹稉鍙夊笓鎼达拷
			Arrays.sort(array);
			for (int i = 0; i < 4; i++) {
				sb.append(array[i]);
			}
			String str = sb.toString();
			// SHA1缁涙儳鎮曢悽鐔稿灇
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());
			byte[] digest = md.digest();

			StringBuffer hexstr = new StringBuffer();
			String shaHex = "";
			for (int i = 0; i < digest.length; i++) {
				shaHex = Integer.toHexString(digest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexstr.append(0);
				}
				hexstr.append(shaHex);
			}
			return hexstr.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.ComputeSignatureError);
		}
	}
}
