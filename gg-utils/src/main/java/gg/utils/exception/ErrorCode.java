package gg.utils.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	//common
	NULL_POINT(500, "G100", "NULL POINT EXCEPTION"),

	//user
	USER_NOT_FOUND(404, "UR100", "USER NOT FOUND"),
	USER_IMAGE_NOT_FOUND(404, "UR200", "USER IMAGE NOT FOUND"),
	USER_IMAGE_TOO_LARGE(413, "UR401", "USER IMAGE IS TOO LARGE"),
	USER_IMAGE_WRONG_TYPE(415, "UR402", "USER IMAGE TYPE IS WRONG"),
	KAKAO_OAUTH2_NOT_FOUND(404, "UR101", "KAKAO OAUTH2 NOT FOUND"),
	KAKAO_OAUTH2_DUPLICATE(409, "UR300", "KAKAO OAUTH2 ALREADY EXIST"),
	USER_TEXT_COLOR_WRONG_TYPE(401, "UR403", "USER TEXT COLOR CODE IS WRONG"),
	USER_ALREADY_ATTENDANCE(409, "UR301", "USER ALREADY ATTENDANCE"),

	//announcement
	ANNOUNCE_NOT_FOUND(404, "AN100", "ANNOUNCEMENT NOT FOUND"),
	ANNOUNCE_DUPLICATE(409, "AN300", "ANNOUNCEMENT DUPLICATION"),

	//coinPolicy
	CoinPolicy_NOT_FOUND(404, "CP100", "COINPOLICY NOT FOUND"),

	//coinHistory
	COIN_HISTORY_NOT_FOUND(404, "CH100", "COIN HISTORY NOT FOUND"),

	//season
	SEASON_NOT_FOUND(404, "SE100", "SEASON NOT FOUND"),
	SEASON_FORBIDDEN(400, "SE500", "SEASON FORBIDDEN ERROR"),
	SEASON_TIME_BEFORE(400, "SE501", "SEASON TIME BEFORE"),

	//slotmanagement
	SLOTMANAGEMENT_NOT_FOUND(404, "SM100", "SLOTMANAGEMENT NOT FOUND"),
	SLOTMANAGEMENT_FORBIDDEN(400, "SM500", "SLOTMANAGEMENT FORBIDDEN"),

	//rank
	RANK_NOT_FOUND(404, "RK100", "RANK NOT FOUND"),
	REDIS_RANK_NOT_FOUND(404, "RK101", "REDIS RANK NOT FOUND"),
	RANK_UPDATE_FAIL(400, "RK200", "RANK UPDATE FAIL"),

	//tier
	TIER_NOT_FOUND(404, "TR100", "TIER NOT FOUND"),

	//item
	ITEM_NOT_FOUND(404, "IT100", "ITEM NOT FOUND"),
	ITEM_TYPE_NOT_MATCHED(400, "IT200", "ITEM TYPE NOT MATCHED"),
	ITEM_NOT_PURCHASABLE(400, "IT201", "ITEM NOT PURCHASABLE"),
	INSUFFICIENT_GGCOIN(400, "IT202", "INSUFFICIENT GGCOIN"),
	GUEST_ROLE_PURCHASE_FORBIDDEN(403, "IT203", "GUEST ROLE USERS CANNOT PURCHASE ITEMS."),
	GUEST_ROLE_GIFT_FORBIDDEN(403, "IT204", "GUEST ROLE USERS CANNOT GIFT ITEMS."),
	ITEM_NOT_AVAILABLE(400, "IT205", "ITEM NOT AVAILABLE"),
	ITEM_IMAGE_TOO_LARGE(413, "IT401", "ITEM IMAGE IS TOO LARGE"),
	ITEM_IMAGE_WRONG_TYPE(415, "IT402", "ITEM IMAGE TYPE IS WRONG"),

	//receipt
	RECEIPT_NOT_FOUND(404, "RC100", "RECEIPT NOT FOUND"),
	RECEIPT_NOT_OWNER(403, "RC500", "RECEIPT NOT OWNER"),
	RECEIPT_STATUS_NOT_MATCHED(400, "RC200", "RECEIPT STATUS NOT MATCHED"),

	//megaphone
	MEGAPHONE_NOT_FOUND(404, "ME100", "MEGAPHONE NOT FOUND"),
	MEGAPHONE_TIME(400, "ME200", "MEGAPHONE TIME"),
	MEGAPHONE_CONTENT(400, "ME201", "MEGAPHONE CONTENT IS EMPTY"),

	/** Penalty **/
	PENALTY_NOT_FOUND(404, "PE100", "PENALTY NOT FOUND"),
	REDIS_PENALTY_USER_NOT_FOUND(404, "PE101", "REDIS PENALTY USER NOT FOUND"),
	PENALTY_EXPIRED(400, "PE200", "PENALTY EXPIRED"),

	/** team **/
	TEAM_ID_NOT_MATCH(400, "TM201", "TEAM id 가 일치하지 않습니다."),
	TEAM_DUPLICATION(409, "TM202", "중복된 Team 이 한 Game 에 존재할 수 없습니다."),
	TEAM_SIZE_EXCEED(500, "TM203", "게임 최대 Team 의 수(2)를 초과하였습니다."),
	TEAM_NOT_FOUND(404, "TM204", "TEAM이 존재하지 않습니다."),
	WINNING_TEAM_NOT_FOUND(404, "TM205", "WINNING TEAM이 존재하지 않습니다."),
	LOSING_TEAM_NOT_FOUND(404, "TM206", "LOSING TEAM이 존재하지 않습니다."),

	/**
	 * team_user
	 */
	TEAM_USER_ALREADY_EXIST(409, "TU201", "중복된 TEAM_USER"),
	TEAM_USER_EXCEED(500, "TU202", "TeamUser 최대 인원의 수(2)를 초과하였습니다."),
	TEAM_USER_NOT_FOUND(404, "TU203", "TeamUser가 없습니다."),

	/** game **/
	GAME_DB_NOT_VALID(500, "GM201", "GAME DB NOT CONSISTENCY"),
	SCORE_NOT_MATCHED(400, "GM202", "score 입력이 기존과 다릅니다."),
	GAME_NOT_FOUND(404, "GM101", "GAME 이 존재하지 않습니다."),
	GAME_NOT_RECENTLY(400, "GM203", "가장 최근 게임이 아닙니다."),
	GAME_DUPLICATION_EXCEPTION(409, "GM204", "GAME ALREADY EXISTS"),
	GAME_STATUS_NOT_MATCHED(400, "GM205", "게임 상태 오류입니다."),
	SCORE_ALREADY_ENTERED(400, "GM206", "점수가 이미 입력되었습니다."),
	SCORE_NOT_INVALID(400, "GM205", "score 입력이 유효하지 않습니다."),
	GAME_NOT_TOURNAMENT(400, "GM206", "토너먼트 게임이 아닙니다."),

	/** match **/
	SLOT_ENROLLED(400, "MA300", "SLOT ALREADY ENROLLED"),
	SLOT_COUNT_EXCEEDED(400, "MA301", "SLOT COUNT MORE THAN THREE"),
	SLOT_NOT_FOUND(404, "MA100", "SLOT NOT FOUND"),
	PENALTY_USER_ENROLLED(400, "MA302", "PENALTY USER ENROLLED"),
	SLOT_PAST(400, "MA303", "PAST SLOT ENROLLED"),
	MODE_INVALID(400, "MA200", "MODE INVALID"),

	/** Common **/
	INTERNAL_SERVER_ERR(500, "CM001", "INTERNAL SERVER ERROR"),
	NOT_FOUND(404, "CM002", "NOT FOUND"),
	BAD_REQUEST(400, "CM003", "BAD REQUEST"),
	UNAUTHORIZED(401, "CM004", "UNAUTHORIZED"),
	METHOD_NOT_ALLOWED(405, "CM005", "METHOD NOT ALLOWED"),
	PAGE_NOT_FOUND(404, "CM006", "PAGE NOT FOUND"),
	VALID_FAILED(400, "CM007", "Valid Test Failed."),
	BAD_ARGU(400, "ARGUMENT-ERR-400", "잘못된 argument 입니다."),
	UNREADABLE_HTTP_MESSAGE(400, "CM008", "유효하지 않은 HTTP 메시지입니다."),

	//Feedback
	FEEDBACK_NOT_FOUND(404, "FB100", "FB NOT FOUND"),

	/**
	 * PChange
	 **/
	PCHANGE_NOT_FOUND(404, "PC100", "PChange 가 존재하지 않습니다."),

	AWS_S3_ERR(500, "CL001", "AWS S3 Error"),
	AWS_SERVER_ERR(500, "CL002", "AWS Error"),

	// SENDER
	SLACK_USER_NOT_FOUND(404, "SL001", "fail to get slack user info"),
	SLACK_CH_NOT_FOUND(404, "SL002", "fail to get user dm channel id"),
	SLACK_JSON_PARSE_ERR(400, "SL002", "json parse error"),
	SLACK_SEND_FAIL(400, "SL003", "fail to send notification"),

	// Tournament
	TOURNAMENT_INVALID_TIME(400, "TN001", "유효한 토너먼트 기간이 아닙니다."),
	TOURNAMENT_CAN_NOT_UPDATE(403, "TN002", "토너먼트를 업데이트 할 수 없는 기간입니다."),
	TOURNAMENT_INVALID_SCORE(403, "TN003", "스코어를 업데이트 할 수 없습니다."),
	TOURNAMENT_NOT_BEFORE(403, "TN004", "tournament status is not before"),
	TOURNAMENT_NOT_LIVE(403, "TN005", "tournament status is not live"),
	TOURNAMENT_GAME_CAN_NOT_CANCELED(403, "TN006", "진행중인 토너먼트의 게임은 취소할 수 없습니다."),
	TOURNAMENT_NOT_FOUND(404, "TN007", "target tournament not found"),
	TOURNAMENT_GAME_NOT_FOUND(404, "TN008", "tournament game not found"),
	TOURNAMENT_NOT_PARTICIPANT(404, "TN009", "토너먼트의 신청자가 아닙니다."),
	TOURNAMENT_USER_NOT_FOUND(404, "TN010", "target tournament user not found"),
	TOURNAMENT_CONFLICT(409, "TN011", "tournament conflicted"),
	TOURNAMENT_ALREADY_PARTICIPANT(409, "TN012", "이미 토너먼트의 신청자 입니다."),
	TOURNAMENT_CONFLICT_GAME(409, "TN013", "토너먼트 기간 내 대기중인 게임이 존재합니다."),
	TOURNAMENT_GAME_DUPLICATION(409, "TN014", "중복된 토너먼트 게임입니다!"),
	TOURNAMENT_USER_DUPLICATION(409, "TN015", "중복된 토너먼트 유저입니다!"),
	TOURNAMENT_GAME_EXCEED(500, "TN016", "토너먼트 게임 최대 사이즈를 초과하였습니다!"),
	TOURNAMENT_IS_BEFORE(403, "TN017", "before인 토너먼트에서 점수 수정할 수 없습니다."),

	// Party
	CATEGORY_NOT_FOUND(404, "PT101", "유효하지 않은 카테고리 입니다."),
	ROOM_NOT_FOUND(404, "PT102", "존재하지 않는 방 입니다."),
	ROOM_REPORTED_ERROR(404, "PT103", "신고 상태로 접근이 불가능합니다."),
	COMMENT_NOT_FOUND(404, "PT104", "존재하지 않는 댓글입니다."),
	ROOMSTAT_NOT_FOUND(404, "PT105", "존재하지 않는 방 status입니다."),
	TEMPLATE_NOT_FOUND(404, "PT106", "존재하지 않는 템플릿 입니다."),
	ROOM_NOT_ENOUGH_PEOPLE(400, "PT201", "시작할 수 있는 인원이 아닙니다."),
	DEFAULT_CATEGORY_NEED(400, "PT202", "기본 카테고리가 존재해야 합니다."),
	USER_NOT_HOST(400, "PT203", "방장이 아닙니다"),
	ROOM_SAME_STATUS(400, "PT204", "이미 처리된 방 입니다."),
	ROOM_NOT_OPEN(400, "PT205", "모집중인 방이 아닙니다."),
	ROOM_NOT_PARTICIPANT(400, "PT206", "참여하지 않은 방 입니다."),
	CATEGORY_DUPLICATE(400, "PT207", "중복된 카테고리 입니다."),
	USER_ALREADY_IN_ROOM(409, "PT301", "이미 참여한 방 입니다."),
	ALREADY_REPORTED(409, "PT302", "이미 신고한 요청입니다."),
	SELF_REPORT(409, "PT303", "자신을 신고할 수 없습니다.");

	private final int status;
	private final String errCode;
	private String message;

	public void setMessage(String msg) {
		this.message = msg;
	}
}
