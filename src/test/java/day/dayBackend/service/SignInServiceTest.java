package day.dayBackend.service;

import day.dayBackend.domain.Member;
import day.dayBackend.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class SignInServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void memberCreateTest() throws Exception {

        //given
        Member member1 = Member.builder()
                .email("member1@test.com")
                .password("1234")
                .username("member1")
                .introduction("int1")
                .build();

        Member member2 = Member.builder()
                .email("member2@etest.com")
                .password("1234")
                .username("member2")
                .introduction("int2")
                .build();

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);

        long count = memberRepository.count();

        //then
        assertThat(member1.getEmail()).isEqualTo("member1@test.com");
        assertThat(count).isEqualTo(2);
    }


}