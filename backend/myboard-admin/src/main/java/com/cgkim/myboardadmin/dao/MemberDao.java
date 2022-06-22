package com.cgkim.myboardadmin.dao;

import com.cgkim.myboardadmin.vo.member.MemberVo;
import com.cgkim.myboardadmin.vo.member.SignUpRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MemberDao {

    void insert(SignUpRequest signUpRequest);
    MemberVo selectByMemberId(Long memberId);
    MemberVo selectByUsername(String username);
    int selectCountByUsername(String username);
    int selectCountByNickname(String nickname);

    Long selectMemberIdByUsername(String username);
}
