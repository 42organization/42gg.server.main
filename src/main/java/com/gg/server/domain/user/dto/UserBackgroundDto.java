package com.gg.server.domain.user.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserBackgroundDto {
	@NotNull
	private Long receiptId;
}
