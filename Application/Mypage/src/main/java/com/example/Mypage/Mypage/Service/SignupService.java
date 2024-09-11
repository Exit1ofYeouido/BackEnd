package com.example.Mypage.Mypage.Service;

import com.example.Mypage.Common.Entity.Account;
import com.example.Mypage.Common.Entity.Member;
import com.example.Mypage.Common.Repository.AccountRepository;
import com.example.Mypage.Common.Repository.MemberRepository;
import com.example.Mypage.Mypage.Dto.in.MemberSignupRequestDTO;
import com.example.Mypage.Mypage.Exception.DuplicateIdException;
import com.example.Mypage.Mypage.Kafka.Dto.AttendanceDto;
import com.example.Mypage.Mypage.Kafka.Dto.AuthProduceDTO;
import com.example.Mypage.Mypage.Kafka.ouput.AttendanceMessageProducer;
import com.example.Mypage.Mypage.Kafka.ouput.AuthMessageProducer;
import jakarta.persistence.PessimisticLockException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignupService {

    private final MemberRepository memberRepository;
    private final AuthMessageProducer authMessageProducer;
    private final AttendanceMessageProducer attendanceMessageProducer;
    private final AccountRepository accountRepository;

    @Transactional
    public boolean saveMember(MemberSignupRequestDTO memberSignupRequestDTO) {
        String newMemberName = memberSignupRequestDTO.getMemberName();

        if (memberRepository.existsByMemberName(newMemberName)) {
            log.error("중복된 아이디로 회원가입 요청발생 {}", memberSignupRequestDTO.getMemberName());
            throw new DuplicateIdException(newMemberName + "는 이미 사용 중인 아이디입니다.");
        }

        try {
            Member newMember = memberSignupRequestDTO.toEntity();
            Member savedMember = memberRepository.save(newMember);

            String newAccountNumber = makeNewAccountNumber();
            Account newAccount = getNewAccount(newMember, newAccountNumber);
            accountRepository.save(newAccount);

            AuthProduceDTO authProduceDTO = getAuthProduceDTO(memberSignupRequestDTO,
                    savedMember);

            authMessageProducer.sendMessage("test-auth", authProduceDTO);

            AttendanceDto attendanceDto = getAttendanceDto(savedMember);
            attendanceMessageProducer.sendMessage("attendance", attendanceDto);

        } catch (PessimisticLockException e) {
            log.error("Lock Transaction 얻지 못 함 {} ", e.getMessage());
            throw new PessimisticLockException(e.getMessage());
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("입력하신 생년월일을 확인해주세요.", memberSignupRequestDTO.getBirth(), 0);
        } catch (Exception e) {
            log.error("회원가입 과정에서 예외 발생 {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    private static AttendanceDto getAttendanceDto(Member savedMember) {
        return AttendanceDto.builder().memberId(savedMember.getId()).build();
    }

    private static Account getNewAccount(Member newMember, String newAccountNumber) {
        return Account.builder()
                .member(newMember)
                .accountNumber(newAccountNumber)
                .point(0)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private AuthProduceDTO getAuthProduceDTO(MemberSignupRequestDTO memberSignupRequestDTO, Member joinedMember) {
        return AuthProduceDTO.builder()
                .memberId(joinedMember.getId())
                .memberName(joinedMember.getMemberName())
                .memberPassword(memberSignupRequestDTO.getMemberPassword())
                .phoneNumber(joinedMember.getPhoneNumber())
                .role(joinedMember.getRole())
                .build();
    }

    private String generateAccountNumber() {
        Random random = new Random();

        String prePart = String.format("%06d", random.nextInt(1000000));
        String inPart = String.format("%02d", random.nextInt(100));
        String postPart = String.format("%05d", random.nextInt(10000));

        return String.format("%s-%s-%s", prePart, inPart, postPart);
    }

    private String makeNewAccountNumber() {
        while (true) {
            String accountNumber = generateAccountNumber();

            if (!accountRepository.existsByAccountNumber(accountNumber)) {
                return accountNumber;
            }
        }
    }

}
