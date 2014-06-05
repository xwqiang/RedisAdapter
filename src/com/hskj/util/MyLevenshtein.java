package com.hskj.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @className:MyLevenshtein.java
 * @classDescription:Levenshtein Distance 算法实现
 * 可以使用的地方：DNA分析 　　拼字检查 　　语音辨识 　　抄袭侦测
 * @author:donghai.wan
 * @createTime:2012-1-12
 */
public class MyLevenshtein {

	public static void main(String[] args) {
		//要比较的两个字符串
//		String str1 = "424afarf22afafarfwr24545535arf4";
//		String str2 = "erwre424afdarf22afafarfwr24545535arf4afdsfd";
		String str1 = "您好,您本次获得消费积分[16900]分,欢迎再次光临【玖玖旅馆连锁】";
		String str2 = "您好,您本次获得消费积分[25]分,欢迎再次光临!【玖玖旅馆连锁】";
//		String str3 = "您好,您本次获得消费积分[437]分,欢迎再次光临!【玖玖旅馆连锁】";
		System.out.println(levenshtein(str1,str2));
	}

	/**
	 * 　　DNA分析 　　拼字检查 　　语音辨识 　　抄袭侦测
	 * 
	 * @createTime 2012-1-12
	 */
	public static Map<Integer,Float> levenshtein(String str1,String str2) {
		
		if(str1==null||str2==null){
			return null;
		}
		String tmpStr1 = str1.replaceAll("-&&-", " ");
		String tmpStr2 = str2.replaceAll("-&&-", " ");
		if(tmpStr1.length()>1){
			str1 = tmpStr1;
		}
		if(tmpStr2.length()>1){
			str2 = tmpStr2;
		}
		//计算两个字符串的长度。
		int len1 = str1.length();
		int len2 = str2.length();
		//建立上面说的数组，比字符长度大一个空间
		int[][] dif = new int[len1 + 1][len2 + 1];
		//赋初值，步骤B。
		for (int a = 0; a <= len1; a++) {
			dif[a][0] = a;
		}
		for (int a = 0; a <= len2; a++) {
			dif[0][a] = a;
		}
		//计算两个字符是否一样，计算左上的值
		int temp;
		for (int i = 1; i <= len1; i++) {
			for (int j = 1; j <= len2; j++) {
				if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
					temp = 0;
				} else {
					temp = 1;
				}
				//取三个值中最小的
				dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
						dif[i - 1][j] + 1);
			}
		}
		System.out.println("字符串\""+str1+"\"与\""+str2+"\"的比较");
		//取数组右下角的值，同样不同位置代表不同字符串的比较
		System.out.println("差异步骤："+dif[len1][len2]);
		//计算相似度
		float similarity =1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());
		System.out.println("相似度："+similarity);
		
		Map<Integer,Float> map = new HashMap<Integer,Float>();
		map.put(dif[len1][len2], similarity);
		return map;
	}

	//得到最小值
	private static int min(int... is) {
		int min = Integer.MAX_VALUE;
		for (int i : is) {
			if (min > i) {
				min = i;
			}
		}
		return min;
	}

}
