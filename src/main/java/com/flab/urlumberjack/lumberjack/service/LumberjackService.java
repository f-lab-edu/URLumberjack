package com.flab.urlumberjack.lumberjack.service;

import static com.flab.urlumberjack.global.constants.GlobalConstants.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.flab.urlumberjack.lumberjack.domain.UrlInfo;
import com.flab.urlumberjack.lumberjack.dto.request.LumberjackRequest;
import com.flab.urlumberjack.lumberjack.dto.response.LumberjackResponse;
import com.flab.urlumberjack.lumberjack.exception.DuplicatedCustomUrlException;
import com.flab.urlumberjack.lumberjack.exception.NonMemberLumberjackLimitExceededException;
import com.flab.urlumberjack.lumberjack.exception.TooManyRepeatLumberjack;
import com.flab.urlumberjack.lumberjack.mapper.LumberjackMapper;

@Service
public class LumberjackService {

	private final LumberjackMapper lumberjackMapper;

	public LumberjackService(LumberjackMapper lumberjackMapper) {
		this.lumberjackMapper = lumberjackMapper;
	}

	public UrlInfo selectUrlInfo(UrlInfo urlInfo) {
		return lumberjackMapper.selectUrlInfo(urlInfo);
	}

	public List<UrlInfo> getLumberjackUrlList(UrlInfo urlInfo) {
		return lumberjackMapper.getLumberjackUrlList(urlInfo);
	}

	public LumberjackResponse lumberjackUrl(LumberjackRequest dto, String createUser) {
		isNonMemberLimitExceeded(createUser);

		String originUrl = dto.getOriginUrl();
		String shortenUrl = getShortUrl();

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

	public LumberjackResponse enterCustomUrl(LumberjackRequest dto, String createUser) {
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

	/***
	 * 생성한 Short URL이 이미 등록되어있는지 확인하고, 10회이상 반복시 에러를 출력한다.
	 * @return String : 중복되지않은 7자리로 구성된 short URL
	 */
	String getShortUrl() {
		int cnt = 1;
		String shortUrl;
		while (cnt <= 10) {
			shortUrl = generateShortUrl();

			UrlInfo urlInfo = UrlInfo.builder()
				.shortUrl(shortUrl)
				.useYn("Y")
				.build();
			if (selectUrlInfo(urlInfo) == null) {
				return shortUrl;
			}

			cnt++;
		}

		throw new TooManyRepeatLumberjack();
	}

	/**
	 * UUID를 생성해 SHA-256으로 해싱한 후 앞에서 5자리를 가져와 Base62로 인코딩한다.
	 * 해당 결과가 DB에 등록되었는지 확인하며, 등록될경우 10회 반복하고 10회 이상 반복시 TooManyRepeatLumberjack를 반환한다.
	 * @return String : 7자리로 구성된 short URL
	 */
	public String generateShortUrl() {
		String uuid = UUID.randomUUID().toString();
		String hashedUuid = hashWithSha256(uuid);
		String shortUrl = encodeWithBase62(hashedUuid.substring(0, 5));
		return shortUrl;
	}

	/***
	 * 문자열을 SHA-256으로 해싱해 반환한다
	 * @param input [String] 해싱할 문자열
	 * @return [String] 해싱된 UUID
	 */
	private String hashWithSha256(String input) {
		StringBuilder hexString = new StringBuilder();

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

			for (byte hashByte : hashBytes) {
				hexString.append(String.format("%02x", hashByte));
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return hexString.toString();
	}

	/***
	 * Base62로 인코딩된 7자리 문자열을 반환한다
	 * @param input 인코딩할 문자열
	 * @return String : Base62로 인코딩된 문자열
	 */
	private String encodeWithBase62(String input) {
		long decimalValue = Long.parseLong(input, 16);

		StringBuilder encodedString = new StringBuilder();
		while (decimalValue > 0) {
			encodedString.append(BASE_62_CHAR.charAt((int)(decimalValue % 62)));
			decimalValue /= 62;
		}

		// 7자리가 아니라면, 뒤에 A를 붙여준다
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
	private void isNonMemberLimitExceeded(String currentUser) {
		if (currentUser.contains("@")) {
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
