package pl.lodz.p.it.boorger.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.lodz.p.it.boorger.security.jwt.AuthEntryPointJwt;
import pl.lodz.p.it.boorger.security.jwt.JWTAuthenticationFilter;
import pl.lodz.p.it.boorger.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String[] CLIENT_PATHS = new String[]{"/tmp"};
    private final String[] MANAGER_PATHS = new String[]{"/tmp"};
    private final String[] ADMIN_PATHS = new String[]{"/boorger/accounts"};

    private AuthEntryPointJwt authEntryPointJwt;
    private UserDetailsServiceImpl userDetailsService;
    private Environment env;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(CLIENT_PATHS).hasAuthority(env.getProperty("boorger.roleClient"))
                .antMatchers(MANAGER_PATHS).hasAuthority(env.getProperty("boorger.roleManager"))
                .antMatchers(ADMIN_PATHS).hasAuthority(env.getProperty("boorger.roleAdmin")).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) .and()
                .exceptionHandling().authenticationEntryPoint(authEntryPointJwt).accessDeniedPage("/accessDenied");
//                .and().formLogin().loginPage("/login").permitAll();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.requiresChannel()
            .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
            .requiresSecure();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
