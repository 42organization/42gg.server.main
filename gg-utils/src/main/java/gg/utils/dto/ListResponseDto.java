package gg.utils.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ListResponseDto<T> {
	private List<T> content;

	@Builder
	private ListResponseDto(List<T> content) {
		this.content = content;
	}

	public static <T> ListResponseDto<T> toDto(List<T> content) {
		return ListResponseDto.<T>builder().content(content).build();
	}
}
