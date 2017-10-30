/**
 * 鐎电懓鍙曟导妤�閽╅崣鏉垮絺闁胶绮伴崗顑跨船鐠愶箑褰块惃鍕Х閹垰濮炵憴锝呯槕缁�杞扮伐娴狅絿鐖�.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package me.aes;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 閹绘劒绶甸崺杞扮艾PKCS7缁犳纭堕惃鍕鐟欙絽鐦戦幒銉ュ經.
 */
class PKCS7Encoder {
	static Charset CHARSET = Charset.forName("utf-8");
	static int BLOCK_SIZE = 32;

	/**
	 * 閼惧嘲绶辩�佃妲戦弬鍥箻鐞涘矁藟娴ｅ秴锝為崗鍛畱鐎涙濡�.
	 * 
	 * @param count 闂囷拷鐟曚浇绻樼悰灞斤綖閸忓懓藟娴ｅ秵鎼锋担婊呮畱閺勫孩鏋冪�涙濡稉顏呮殶
	 * @return 鐞涖儵缍堥悽銊ф畱鐎涙濡弫鎵矋
	 */
	static byte[] encode(int count) {
		// 鐠侊紕鐣婚棁锟界憰浣革綖閸忓懐娈戞担宥嗘殶
		int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
		if (amountToPad == 0) {
			amountToPad = BLOCK_SIZE;
		}
		// 閼惧嘲绶辩悰銉ょ秴閹碉拷閻€劎娈戠�涙顑�
		char padChr = chr(amountToPad);
		String tmp = new String();
		for (int index = 0; index < amountToPad; index++) {
			tmp += padChr;
		}
		return tmp.getBytes(CHARSET);
	}

	/**
	 * 閸掔娀娅庣憴锝呯槕閸氬孩妲戦弬鍥╂畱鐞涖儰缍呯�涙顑�
	 * 
	 * @param decrypted 鐟欙絽鐦戦崥搴ｆ畱閺勫孩鏋�
	 * @return 閸掔娀娅庣悰銉ょ秴鐎涙顑侀崥搴ｆ畱閺勫孩鏋�
	 */
	static byte[] decode(byte[] decrypted) {
		int pad = (int) decrypted[decrypted.length - 1];
		if (pad < 1 || pad > 32) {
			pad = 0;
		}
		return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
	}

	/**
	 * 鐏忓棙鏆熺�涙娴嗛崠鏍ㄥ灇ASCII閻礁顕惔鏃傛畱鐎涙顑侀敍宀�鏁ゆ禍搴☆嚠閺勫孩鏋冩潻娑滎攽鐞涖儳鐖�
	 * 
	 * @param a 闂囷拷鐟曚浇娴嗛崠鏍畱閺佹澘鐡�
	 * @return 鏉烆剙瀵插妤�鍩岄惃鍕摟缁楋拷
	 */
	static char chr(int a) {
		byte target = (byte) (a & 0xFF);
		return (char) target;
	}

}
