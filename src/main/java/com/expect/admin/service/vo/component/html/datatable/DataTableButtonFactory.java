package com.expect.admin.service.vo.component.html.datatable;

import com.expect.admin.service.vo.component.html.ButtonVo;

/**
 * Datatable后面操作一栏的button。
 * 
 * 主要分为几类：删除(red)，修改（blue）。
 * 
 * 其他可以选择的颜色：green，yellow，purple
 */
public class DataTableButtonFactory {

	public static String setButton(boolean isButton, String text, String className, String otherAttr, int size) {
		ButtonVo.Builder builder = new ButtonVo.Builder();
		builder.setClassName(className).setText(text).setOtherAttr(otherAttr).setSize(size);
		ButtonVo buttonVo = builder.create();
		return buttonVo.getButton(isButton);
	}

	public static String getDeleteButton(String otherAttr) {
		return setButton(true, "删除", "red delete-button", otherAttr, 1);
	}

	public static String getUpdateButton(String otherAttr) {
		return setButton(true, "修改", "blue update-button", otherAttr, 1);
	}

	public static String getGreenButton(String text, String otherAttr) {
		return setButton(true, text, "green green-button", otherAttr, 1);
	}

	public static String getGreenButton(String text, String className, String otherAttr) {
		return setButton(true, text, "green green-button " + className, otherAttr, 1);
	}

	public static String getGreenButton(boolean isButton, String text, String className, String otherAttr) {
		return setButton(isButton, text, "green green-button " + className, otherAttr, 1);
	}

	public static String getGreenButton(boolean isButton, String text, String otherAttr) {
		return setButton(isButton, text, "green green-button", otherAttr, 1);
	}

	public static String getYellowButton(String text, String otherAttr) {
		return setButton(true, text, "yellow yellow-button", otherAttr, 1);
	}

	public static String getYellowButton(boolean isButton, String text, String otherAttr) {
		return setButton(isButton, text, "yellow yellow-button", otherAttr, 1);
	}

	public static String getYellowButton(String text, String className, String otherAttr) {
		return setButton(true, text, "yellow yellow-button " + className, otherAttr, 1);
	}

	public static String getYellowButton(boolean isButton, String text, String className, String otherAttr) {
		return setButton(isButton, text, "yellow yellow-button " + className, otherAttr, 1);
	}

	public static String getPurpleButton(String text, String otherAttr) {
		return setButton(true, text, "purple purple-button", otherAttr, 1);
	}

	public static String getPurpleButton(boolean isButton, String text, String otherAttr) {
		return setButton(isButton, text, "purple purple-button", otherAttr, 1);
	}

	public static String getPurpleButton(String text, String className, String otherAttr) {
		return setButton(true, text, "purple purple-button " + className, otherAttr, 1);
	}

	public static String getPurpleButton(boolean isButton, String text, String className, String otherAttr) {
		return setButton(isButton, text, "purple purple-button " + className, otherAttr, 1);
	}
	
	public static String getGreenSharpButton(String text, String otherAttr) {
		return setButton(true, text, "green-sharp green-sharp-button", otherAttr, 1);
	}

	public static String getGreenSharpButton(boolean isButton, String text, String otherAttr) {
		return setButton(isButton, text, "green-sharp green-sharp-button", otherAttr, 1);
	}

	public static String getGreenSharpButton(String text, String className, String otherAttr) {
		return setButton(true, text, "green-sharp green-sharp-button " + className, otherAttr, 1);
	}

	public static String getGreenSharpButton(boolean isButton, String text, String className, String otherAttr) {
		return setButton(isButton, text, "green-sharp green-sharp-button " + className, otherAttr, 1);
	}
}
