package com.tsp.new_tsp_project.configuration;

import com.tsp.new_tsp_project.api.jwt.JwtAuthenticationEntryPoint;
import com.tsp.new_tsp_project.api.jwt.JwtAuthenticationFilter;
import com.tsp.new_tsp_project.api.jwt.JwtAuthorizationFilter;
import com.tsp.new_tsp_project.api.jwt.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final MyUserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs",
                "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }

    /**
     * <pre>
     * 1. MethodName : authenticationManagerBean
     * 2. ClassName  : SecurityConfig.java
     * 3. Comment    : authenticationManager Bean 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 07. 07.
     * </pre>
     *
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * <pre>
     * 1. MethodName : passwordEncoder
     * 2. ClassName  : SecurityConfig.java
     * 3. Comment    : 암호화에 필요한 passwordEncoder Bean 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 07. 07.
     * </pre>
     *
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
        // 그 외
        http.authorizeRequests()
                .antMatchers("/api/model").hasRole("ADMIN")
                .antMatchers("/api/production").hasRole("ADMIN")
                .antMatchers("/api/portfolio").hasRole("ADMIN")
                .antMatchers("/api/support").hasRole("ADMIN")
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().formLogin().disable().headers().frameOptions().disable().and().csrf().disable();
        // 로그인 인증 관련
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        // JWT 토큰 유효성 관련
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * <pre>
     * 1. MethodName : jwtAuthorizationFilter
     * 2. ClassName  : SecurityConfiguration.java
     * 3. Comment    : 로그인 인증
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 07. 07.
     * </pre>
     *
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManager());
    }

    /**
     * <pre>
     * 1. MethodName : jwtAuthenticationFilter
     * 2. ClassName  : SecurityConfiguration.java
     * 3. Comment    : jwt 인증 Filter
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 07. 07.
     * </pre>
     *
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
