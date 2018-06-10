package com.seckill.util;

import org.apache.commons.lang3.RandomUtils;

public class NumberUtil {

	public static String sexNumber() {
		int[] arr = new int[] {0,1,2,3,4,5,6,7,8,9};
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			sb.append(arr[RandomUtils.nextInt(0, 10)]);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		while(true) {
			System.out.println(sexNumber());
		}
	}
	
}
