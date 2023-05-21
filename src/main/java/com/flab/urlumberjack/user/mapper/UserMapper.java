package com.flab.urlumberjack.user.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.flab.urlumberjack.user.domain.User;
import com.flab.urlumberjack.user.dto.request.JoinRequest;

@Mapper
public interface UserMapper {

	Optional<User> selectUser(String email);

	Integer insertUser(JoinRequest dto);

}
