package com.gg.server.domain.tournament.data;

import com.gg.server.domain.tournament.type.TournamentStatus;
import com.gg.server.domain.tournament.type.TournamentType;
import com.gg.server.domain.user.data.User;
import com.gg.server.global.utils.BaseTimeEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@ToString
public class Tournament extends BaseTimeEntity {
    // 토너먼트 참가자 수 => 현재는 8강 고정
    public static final int ALLOWED_JOINED_NUMBER = 8;
    // 토너먼트 최소 시작 날짜 (n일 후)
    public static final int ALLOWED_MINIMAL_START_DAYS = 2;
    // 토너먼트 최소 진행 시간 (n시간)
    public static final int MINIMUM_TOURNAMENT_DURATION = 2;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "contents")
    private String contents;

    @NotNull
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TournamentType type;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TournamentStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    private User winner;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<TournamentGame> tournamentGames = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<TournamentUser> tournamentUsers = new ArrayList<>();

    /**
     * winner는 생성시점에 존재하지 않음.
     */
    @Builder
    public Tournament(String title, String contents, LocalDateTime startTime, LocalDateTime endTime,
        TournamentType type, TournamentStatus status) {
        this.title = title;
        this.contents = contents;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.status = status;
    }

    public void update(String title, String contents, LocalDateTime startTime,
        LocalDateTime endTime, TournamentType type, TournamentStatus status) {
        this.title = title;
        this.contents = contents;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.status = status;
    }

    public void addTournamentGame(TournamentGame tournamentGame) {
        this.tournamentGames.add(tournamentGame);
    }

    public void addTournamentUser(@NotNull TournamentUser tournamentUser) {
        this.tournamentUsers.add(tournamentUser);
    }

    public void deleteTournamentUser(@NotNull TournamentUser tournamentUser) {
        this.tournamentUsers.remove(tournamentUser);
    }

    public void updateWinner(User winner) {
        this.winner = winner;
    }

    public void updateStatus(TournamentStatus status) {
        this.status = status;
    }
}
