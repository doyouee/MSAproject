package com.inzent.commonAPI.mapper;

import com.inzent.commonAPI.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    //사용자 조회
    List<UserVO> getUserSearchKeyword(@Param("user") UserVO userVO, @Param("page") Map<String, Object> page);

    //사용자 등록
    boolean insertUser(UserVO userVO);

    //사용자 수정
    void updateUser(UserVO userVO);

    //사용자 비밀번호 수정
    void updateUserPassword(UserVO userVO);

    //사용자 삭제
    void deleteUser(@Param("userEmail") String userEmail);

    //이메일 중복 조회
    String getSelectUserName(String userEmail,@Param("deleteYn") String deleteYn);


    UserVO getSelectUserInfo(@Param("user") UserVO userVO, @Param("page") Map<String, Object> page);


}
