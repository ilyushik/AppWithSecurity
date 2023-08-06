package com.example.secondapp.Config;

import com.example.secondapp.Services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // loginPage - страница входа
        // loginProcessingUrl - куда придут данные о входе
        // defaultSuccessUrl - куда отправляемся после успешного входа(ещё добавляем true чтобы всегда перенаправлял на нужный адрес)
        // failureUrl - в случае неудачного входа(адрес страницы входа с параметром error)
        // authorizeRequests - ысе запросы проходят через него
        // antMatchers - чтобы смотреть какой запрос к нам пришёл
        // permitAll - чтобы пускать всех на перечисленные стрнаницы
        // anyRequest - означает другие страницы, не перечисленные в antMatchers
        // authenticated - для этих запросов пользователь должен войти
        // and - чтобы перейти от одного действия к другому
        // csrf().disable - отключаем чтобы не использовать эту защиту
        // logoutUrl - адрес разлогирования
        // logoutSuccessUrl - куда перейдем в случае удачного разлогирования
        // hasRole - чтобы указать одну роль
        // hasAnyRole - чтобы указать список ролей
        // hasAuthority и has AnyAuthority - тоже самое что и с role только используется для действий а не для ролей
        http.authorizeRequests()
                //.antMatchers("/hello/admin").hasRole("ADMIN")
                .antMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
                //.anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/hello", true)
                .failureUrl("/auth/login?error")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login");
    }

    // authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
