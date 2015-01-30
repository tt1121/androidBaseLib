/*
 * 版    权： 深圳市爱猫新媒体数据科技有限公司
 * 创建人: 李理
 * 创建时间: 2014年8月28日
 */
package com.imove.base.utils.filetype;


/**
 * [图片类型]
 * 
 * @author 李理
 */
public enum ImageType {
	JPG("jpg"),
	JPEG("jpeg"), 
	BMP("bmp"), 
	PNG("png"),
	GIF("gif");

	private String value;

	ImageType(String v) {
		this.value = v;
	}

	@Override
	public String toString() {
		return this.value;
	}
	
	public static boolean isContainsType(String v) {
		ImageType[] types = ImageType.values();
		for (ImageType t : types) {
			if (t.value.equals(v.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
