package co.mr.myShopTest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
// WebSecurityConfigurerAdapter를 상속받는 클래스에 @EnableWebSecurity를 선언하면
// SpringSecurityFilterChain이 자동으로 포함된다.
// WebSecurityConfigurerAdapter에는 세부적인 보안설정과 관련된 API를 제공(커스터마이징 가능)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // http요청에 대한 보안을 설정한다.
    // 페이지 권한설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정 할 수 있다.
    //
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }

    // 비밀번호를 데이터베이스에 그대로 저장했을 경우, 데이터베이스가 해킹당했을 때
    // 고객의 회원정보가 그대로 노출되는 것을 막기 위해 BCryptPasswordEncoder의 해시함수를
    // 이용하여 비밀번호를 암호화하여 저장
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
