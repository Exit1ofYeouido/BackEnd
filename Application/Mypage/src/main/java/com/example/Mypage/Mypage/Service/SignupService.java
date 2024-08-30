package com.example.Mypage.Mypage.Service;

import com.example.Mypage.Common.Entity.Member;
import com.example.Mypage.Common.Repository.MemberRepository;
import com.example.Mypage.Mypage.Dto.in.MemberSignupRequestDTO;
import com.example.Mypage.Mypage.Exception.DuplicateIdException;
import com.example.Mypage.Mypage.Kafka.Dto.AuthProduceDTO;
import com.example.Mypage.Mypage.Kafka.ouput.AuthMessageProducer;
import jakarta.persistence.PessimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignupService {

    private final MemberRepository memberRepository;
    private final AuthMessageProducer authMessageProducer;

    @Transactional
    public boolean saveMember(MemberSignupRequestDTO memberSignupRequestDTO) {
        String newMemberName = memberSignupRequestDTO.getMemberName();

        if (memberRepository.existsByMemberName(newMemberName)) {
            log.error("중복된 아이디로 회원가입 요청발생 {}", memberSignupRequestDTO.getMemberName());
            throw new DuplicateIdException(newMemberName + "는 이미 사용 중인 아이디입니다.");
        }

        try {
            Member newMember = memberSignupRequestDTO.toEntity();
            Member joinedMember = memberRepository.save(newMember);

            AuthProduceDTO authProduceDTO = getAuthProduceDTO(memberSignupRequestDTO,
                    joinedMember);

            authMessageProducer.sendMessage("test-auth", authProduceDTO);

        } catch (PessimisticLockException e) {
            log.error("Lock Transaction 얻지 못 함 {} ", e.getMessage());
            throw new PessimisticLockException(e.getMessage());
        } catch (Exception e) {
            log.error("회원가입 과정에서 예외 발생 {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    private AuthProduceDTO getAuthProduceDTO(MemberSignupRequestDTO memberSignupRequestDTO, Member joinedMember) {
        AuthProduceDTO authProduceDTO = AuthProduceDTO.builder()
                .memberId(joinedMember.getId())
                .memberName(joinedMember.getMemberName())
                .memberPassword(memberSignupRequestDTO.getMemberPassword())
                .phoneNumber(joinedMember.getPhoneNumber())
                .role(joinedMember.getRole())
                .build();
        return authProduceDTO;
    }

}
