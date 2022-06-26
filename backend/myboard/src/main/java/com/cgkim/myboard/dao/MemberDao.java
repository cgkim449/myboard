package com.cgkim.myboard.dao;

import com.cgkim.myboard.vo.member.MemberVo;
import com.cgkim.myboard.vo.member.SignUpRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 회원 DAO
 */
@Repository
@Mapper
public interface MemberDao {

    /**
     * 회원 가입
     *
     * @param signUpRequest
     */
    void insert(SignUpRequest signUpRequest);

    /**
     * username 으로 회원 조회
     *
     * @param username
     * @return MemberVo
     */
    MemberVo selectByUsername(String username);

    /**
     * username 중복 검사
     *
     * @param username
     * @return int
     */
    int selectCountByUsername(String username);

    /**
     * nickname 중복 검사
     *
     * @param nickname
     * @return int
     */
    int selectCountByNickname(String nickname);

    /**
     * memberId 조회
     *
     * @param username
     * @return Long
     */
    Long selectMemberIdByUsername(String username);
}
