/**
 * 鐎电懓鍙曟导妤�閽╅崣鏉垮絺闁胶绮伴崗顑跨船鐠愶箑褰块惃鍕Х閹垰濮炵憴锝呯槕缁�杞扮伐娴狅絿鐖�.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package me.aes;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * XMLParse class
 *
 * 閹绘劒绶甸幓鎰絿濞戝牊浼呴弽鐓庣础娑擃厾娈戠�靛棙鏋冮崣濠勬晸閹存劕娲栨径宥嗙Х閹垱鐗稿蹇曟畱閹恒儱褰�.
 */
class XMLParse {

	/**
	 * 閹绘劕褰囬崙绨抦l閺佺増宓侀崠鍛厬閻ㄥ嫬濮炵�靛棙绉烽幁锟�
	 * @param xmltext 瀵板懏褰侀崣鏍畱xml鐎涙顑佹稉锟�
	 * @return 閹绘劕褰囬崙铏规畱閸旂姴鐦戝☉鍫熶紖鐎涙顑佹稉锟�
	 * @throws AesException 
	 */
	public static Object[] extract(String xmltext) throws AesException     {
		Object[] result = new Object[3];
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(xmltext);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);

			Element root = document.getDocumentElement();
			NodeList nodelist1 = root.getElementsByTagName("Encrypt");
			NodeList nodelist2 = root.getElementsByTagName("ToUserName");
			result[0] = 0;
			result[1] = nodelist1.item(0).getTextContent();
			result[2] = nodelist2.item(0).getTextContent();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.ParseXmlError);
		}
	}

	/**
	 * 閻㈢喐鍨歺ml濞戝牊浼�
	 * @param encrypt 閸旂姴鐦戦崥搴ｆ畱濞戝牊浼呯�靛棙鏋�
	 * @param signature 鐎瑰鍙忕粵鎯ф倳
	 * @param timestamp 閺冨爼妫块幋锟�
	 * @param nonce 闂呭繑婧�鐎涙顑佹稉锟�
	 * @return 閻㈢喐鍨氶惃鍓巑l鐎涙顑佹稉锟�
	 */
	public static String generate(String encrypt, String signature, String timestamp, String nonce) {

		String format = "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
				+ "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n"
				+ "<TimeStamp>%3$s</TimeStamp>\n" + "<Nonce><![CDATA[%4$s]]></Nonce>\n" + "</xml>";
		return String.format(format, encrypt, signature, timestamp, nonce);

	}
}
