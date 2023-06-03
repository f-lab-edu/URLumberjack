package com.flab.urlumberjack.global.Utils;

import java.util.Objects;

public class QueryUtil {

	/**
	 * insert가 정상적으로 수행되었는지에 대해 반환합니다.
	 * @param insertResult (int) insert 쿼리의 결과값
	 * @return insert가 정상적으로 수행되었다면 true, 아니라면 false를 반환합니다.
	 */
	public static boolean excuteInsertQuery(Integer insertResult) {
		return !Objects.equals(insertResult, 0);
	}

}
