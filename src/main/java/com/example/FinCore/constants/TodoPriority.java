package com.example.FinCore.constants;

import com.example.FinCore.annotation.TODO;

/**
 * 列舉 TODO annotation 的優先級參數。
 * @see TODO
 * @author 羊羊
 */
public enum TodoPriority 
{
	
	/**
	 * 表示該代辦事項極度重要，最高優先級
	 */
	IMPORTANT,
	
	/**
	 * 表示該代辦事項有一定程度重要性，高優先級
	 */
	HIGH,
	
	/**
	 * 表示該代辦事項需要做、但沒有急迫性，中優先級
	 */
	MEDIUM,
	
	/**
	 * 表示該代辦事項對於程式運作幾乎不影響，低優先級
	 */
	LOW,
	
	/**
	 * 表示該代辦事項完全不影響程式，甚至為非必須，最低優先級
	 */
	NOT_REQUIRED
	
}
