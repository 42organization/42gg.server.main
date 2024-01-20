package com.gg.server.admin.coin.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CoinPolicyAdminListResponseDto {
	private List<CoinPolicyAdminResponseDto> coinPolicyList;
	private int totalPage;
}
