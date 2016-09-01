package net.redcraft.genesis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by maxim on 25/8/16.
 */

@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

	@Value("${security.enabled}")
	private boolean isEnabled;

	@Value("${security.user}")
	private String user;

	@Value("${security.password}")
	private String password;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.inMemoryAuthentication()
				.withUser(user).password(password).roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if(isEnabled) {
			http
				.authorizeRequests()
				.anyRequest()
				.authenticated()
				.and()
				.httpBasic()
				.and()
				.csrf().disable();
		}
		else {
			http
				.anonymous()
				.and()
				.csrf().disable();
		}
	}
}
