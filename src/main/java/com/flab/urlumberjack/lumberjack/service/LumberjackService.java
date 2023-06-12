package com.flab.urlumberjack.lumberjack.service;

import com.flab.urlumberjack.lumberjack.domain.UrlInfo;
import com.flab.urlumberjack.lumberjack.dto.request.LumberjackRequest;
import com.flab.urlumberjack.lumberjack.dto.response.LumberjackResponse;
import com.flab.urlumberjack.lumberjack.exception.DuplicatedCustomUrlException;
import com.flab.urlumberjack.lumberjack.exception.NonMemberLumberjackLimitExceededException;
import com.flab.urlumberjack.lumberjack.exception.RecursiveCallExceedException;
import com.flab.urlumberjack.lumberjack.mapper.LumberjackMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import static com.flab.urlumberjack.global.constants.GlobalConstants.*;

@Service
public class LumberjackService {

	LumberjackMapper lumberjackMapper;

	public LumberjackService(LumberjackMapper lumberjackMapper) {
		this.lumberjackMapper = lumberjackMapper;
	}

	public UrlInfo selectUrlInfo(UrlInfo urlInfo){
		return lumberjackMapper.selectUrlInfo(urlInfo);
	}

	public List<UrlInfo> getLumberjackUrlList(UrlInfo urlInfo) {
		return lumberjackMapper.getLumberjackUrlList(urlInfo);
	}

	public LumberjackResponse lumberjackUrl(LumberjackRequest dto, String createUser) {
		isNonMemberLimitExceeded(createUser);

		String originUrl = dto.getOriginUrl();
		String shortenUrl = getShortUrlRecursive(1);

		UrlInfo urlInfo = UrlInfo.builder()
				.originUrl(originUrl)
				.shortUrl(shortenUrl)
				.qrCode(getQrCode(shortenUrl))
				.useYn("Y")
				.createBy(createUser)
				.build();

		lumberjackMapper.lumberjackUrl(urlInfo);

		UrlInfo savedUrlInfo = lumberjackMapper.selectUrlInfo(urlInfo);

		return LumberjackResponse.of(savedUrlInfo);
	}

	public LumberjackResponse enterCustomUrl(LumberjackRequest dto, String createUser){
		isNonMemberLimitExceeded(createUser);

		UrlInfo urlInfo = UrlInfo.builder()
				.originUrl(dto.getOriginUrl())
				.shortUrl(dto.getCustomUrl())
				.qrCode(getQrCode((dto.getCustomUrl())))
				.useYn("Y")
				.createBy(createUser)
				.build();

		if (selectUrlInfo(urlInfo) != null) {
			throw new DuplicatedCustomUrlException();
		}

		lumberjackMapper.lumberjackUrl(urlInfo);

		UrlInfo savedUrlInfo = lumberjackMapper.selectUrlInfo(urlInfo);

		return LumberjackResponse.of(savedUrlInfo);
	}


	/**
	 * UUID를 SHA-256으로 해싱한 후 앞에서 5자리를 가져와 Base62로 인코딩한다.
	 * 이미 등록되어있는지 확인하며, 이미 등록되었다면 재귀호출한다. 재귀호출 한도는 10회이다.
	 * @param recursionCount 재귀호출 횟수로, 10회를 넘어가면 재귀호출을 중단한다.
	 * @return String : UUID를 SHA-256으로 해싱해 Base62로 인코딩한 5자리 문자열
	 */
	private String getShortUrlRecursive(int recursionCount){
		if(recursionCount > RECURSIVE_CALL_EXCEEDED){
			throw new RecursiveCallExceedException();
		}

		//UUID 생성
		UUID uuid = UUID.randomUUID();
		String hashedUUID = hashWithSHA256(uuid.toString());
		String encodedResult = encodeWithBase62(hashedUUID);

		//이미 등록되어있는 URL인지 확인하며, 이미 등록되었다면 재귀호출한다
		UrlInfo urlInfo = UrlInfo.builder()
				.shortUrl(encodedResult)
				.useYn("Y")
				.build();
		if (selectUrlInfo(urlInfo) != null) {
			return getShortUrlRecursive(recursionCount+1);
		}

		return encodedResult;
	}


	/***
	 * 문자열을 SHA-256으로 해싱해 반환한다
	 * @param input [String] 해싱할 문자열
	 * @return [String] 해싱된 UUID
	 */
	public static String hashWithSHA256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

			StringBuilder hexString = new StringBuilder();
			for (byte hashByte : hashBytes) {
				hexString.append(String.format("%02x", hashByte));
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	/***
	 * Base62로 인코딩된 7자리 문자열을 반환한다
	 * @param input 인코딩할 문자열
	 * @return String : Base62로 인코딩된 문자열
	 */
	public static String encodeWithBase62(String input) {
		String target = input.substring(0, 5);
		long decimalValue = Long.parseLong(target, 16);

		StringBuilder encodedString = new StringBuilder();
		while (decimalValue > 0) {
			encodedString.append(BASE_62_CHAR.charAt((int) (decimalValue % 62)));
			decimalValue /= 62;
		}

		String truncatedString = encodedString.substring(0, Math.min(encodedString.length(), 7));
		String formattedString = String.format("%-7s", truncatedString);
		formattedString = formattedString.replace(' ', 'A');
		return formattedString;
	}

	/***
	 * QR코드를 생성한다
	 * @param shortUrl
	 * @return String : QR코드 URL
	 */
	private String getQrCode(String shortUrl) {
		return "https://chart.googleapis.com/chart?cht=qr&chs=300x300&chl=" + shortUrl;
	}


	/***
	 * 비회원이 생성한 URL이 10개를 초과했는지 확인한다
	 * @param currentUser 현재 접속한 사용자
	 */
	private void isNonMemberLimitExceeded(String currentUser){
		if(currentUser.contains("@")){
			return;
		}

		UrlInfo urlInfo = UrlInfo.builder()
				.createBy(currentUser)
				.useYn("Y")
				.build();

		List<UrlInfo> urlInfos = getLumberjackUrlList(urlInfo);

		if (CollectionUtils.isEmpty(urlInfos)) {
			return;
		}

		if (urlInfos.size() > NON_MEMBER_LUMBERJACK_LIMIT) {
			throw new NonMemberLumberjackLimitExceededException();
		}

	}

}
