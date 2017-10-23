package org.runcity.mvc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final int REMEMBER_ME_SEC = 1209600;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		http.csrf().disable().authorizeRequests().antMatchers("/resources/**", "/**").permitAll().anyRequest()
				.permitAll();

		http.formLogin().loginPage("/login").loginProcessingUrl("/j_spring_security_check").failureUrl("/login?error")
				.defaultSuccessUrl("/home").usernameParameter("j_username").passwordParameter("j_password").permitAll();

		http.logout().permitAll().logoutUrl("/logout").logoutSuccessUrl("/login?logout").invalidateHttpSession(true);
		*/
		http.authorizeRequests()
			.antMatchers("/secure/**").authenticated()
			.and()
				.formLogin()
					.defaultSuccessUrl("/home")
					.loginPage("/login")
					.failureUrl("/login?error")
					.loginProcessingUrl("/j_spring_security_check")
					.usernameParameter("j_username")
					.passwordParameter("j_password")
					.permitAll()
			.and()
				.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login?logout")
			.and()
				.csrf()
			.and()
				.rememberMe()
					.tokenRepository(persistentTokenRepository())
					.tokenValiditySeconds(REMEMBER_ME_SEC);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);
		return db;
	}
}
