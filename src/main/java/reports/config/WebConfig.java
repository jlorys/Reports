package reports.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configurable
@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	AppUserDetailsService appUserDetailsService;

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(appUserDetailsService);
		authProvider.setPasswordEncoder(encoder());
		return authProvider;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(appUserDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		        .csrf().disable()
				// starts authorizing configurations
				.authorizeRequests()
				.antMatchers("/", "/index.html", "/app/**", "/libraries/**", "/register", "/favicon.ico").permitAll()
				// secure all other urls
				.anyRequest().fullyAuthenticated().and()
				// switch on basic authentication
				.httpBasic().and()
				.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
	}

}
