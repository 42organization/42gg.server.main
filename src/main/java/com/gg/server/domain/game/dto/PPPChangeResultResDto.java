package com.gg.server.domain.game.dto;

import com.gg.server.domain.coin.dto.UserGameCoinResultDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PPPChangeResultResDto extends ExpChangeResultResDto {

    public PPPChangeResultResDto(Integer beforeExp, Integer currentExp, Integer beforePpp, Integer afterPpp, UserGameCoinResultDto userGameCoinResultDto) {
        super(beforeExp, currentExp, userGameCoinResultDto);
        this.changedPpp = afterPpp - beforePpp;
        this.beforePpp = beforePpp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof PPPChangeResultResDto)) {
            return false;
        } else {
            PPPChangeResultResDto other = (PPPChangeResultResDto) obj;
            return this.changedPpp.equals(other.getChangedPpp())
                    && this.beforePpp.equals(other.getBeforePpp());
        }
    }
}
