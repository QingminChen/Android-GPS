package com.certusnet.activity;

import android.util.Base64;

public class test {

	public static void main(String[] args) {
		try {

			System.out.println("Basic " + Base64.encodeToString("10201:86f2629ae23038de27da5629181f1793".getBytes(), 2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
