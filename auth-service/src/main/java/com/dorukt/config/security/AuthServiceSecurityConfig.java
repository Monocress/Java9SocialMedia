package com.dorukt.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthServiceSecurityConfig {
    private static final String[] WHITELIST ={"/swagger-ui/**","/v3/api-docs/**","/api/v1/auth/login","/api/v1/auth/registerwithrabbit","/api/v1/auth/register","/api/v1/auth/activation"};

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    JwtTokenFilter getJwtTokenFilter(){
        return new JwtTokenFilter();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable();
        httpSecurity.authorizeRequests().antMatchers(WHITELIST).permitAll().anyRequest().authenticated();//swagger hariç tüm istekler kapalıdır.
        //httpSecurity.authorizeRequests().antMatchers("/api/v1/auth/findall").authenticated().anyRequest().permitAll();//findall hariç tüm istekler açıktır.

        //httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  //Oturum oluşturmadan/açmadan her istekler cevaplanır araştırılabilir.//Her istekten sonra contexti sıfırlar.

        httpSecurity.addFilterBefore(getJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
