package com.example.FinCore.vo;

import java.util.List;

/**
 * 每個月一次統計
 */
public record StatisticsVO(
		int year,
		int month,
		List<StatisticsInfoVO> infoList
		) {

}
