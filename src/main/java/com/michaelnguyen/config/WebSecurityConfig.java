package com.michaelnguyen.config;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.michaelnguyen.dto.request.UserCreationRequest;
import com.michaelnguyen.entity.User;
import com.michaelnguyen.repository.IUserRepository;
import com.michaelnguyen.service.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

	private String[] PUBLIC_ENDPOINT = { "/api/auth/**" };

//	private String[] PUBLIC_ENDPOINT = { "/api/auth/**", "/api/permissions/**", "/api/roles/**", "/api/product/**",
//			"/api/gallery/**" };
	@Autowired
	private IUserRepository iUserRepository;

	@Autowired
	private UserService userService;

	@Value("${jwt.signerKey}")
	private String SIGNER_KEY;

	@Bean
	static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

	@Bean
	JwtDecoder JwtDecoder() {
		SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
		return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();

	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(authorize -> {
			authorize.requestMatchers(PUBLIC_ENDPOINT).permitAll().anyRequest().authenticated();
		})
//		.oauth2Login(oauth2Customize -> oauth2Customize.loginPage("/oauth2/authorization/google")).oauth2Client(withDefaults());
				.oauth2Login(oauth2Login -> oauth2Login
						.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.oidcUserService(this.oidcUserService())
								.userService(this.oauth2UserService()))
						.defaultSuccessUrl("/home-page", true).failureUrl("/login?error")
						.successHandler(this.successHandler()))

				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/logoutSuccess")
						.invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID"));

		http.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwtConfigurer -> jwtConfigurer.decoder(JwtDecoder())
						.jwtAuthenticationConverter(jwtAuthenticationConverter()))
				.authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

		http.csrf(csrf -> csrf.disable());
		return http.build();
	}

	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

		return jwtAuthenticationConverter;
	}

	@Bean
	OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
		final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
		return request -> {
			OAuth2User oauth2User = delegate.loadUser(request);
			Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
			attributes.put("customAttribute", "customValue"); // Add custom attributes if needed
			return new DefaultOAuth2User(oauth2User.getAuthorities(), attributes, "sub"); // Use the appropriate key for
																							// the user's unique
																							// identifier
		};
	}

	@Bean
	OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
		final OidcUserService delegate = new OidcUserService();
		return request -> {
			OidcUser oidcUser = delegate.loadUser(request);
			Map<String, Object> attributes = new HashMap<>(oidcUser.getAttributes());
			attributes.put("customAttribute", "customValue"); // Add custom attributes if needed
			return oidcUser;
		};
	}

	@Bean
	AuthenticationSuccessHandler successHandler() {

		return (request, response, authentication) -> {
			OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
			String googleId = oauth2User.getAttribute("sub");
			String email = oauth2User.getAttribute("email");
			String name = oauth2User.getAttribute("name");

			User user = iUserRepository.findByGoogleId(googleId);
			if (user == null) {

				UserCreationRequest userRequest = new UserCreationRequest();
				userRequest.setEmail(email);
				userRequest.setGoogleId(googleId);

				userService.createUser(userRequest);

				user = iUserRepository.findByGoogleId(googleId);
				// Tạo và gửi token xác thực
				userService.createVerificationToken(user);
			} else {
				System.out.println("Hi: " + name);
				response.sendRedirect("/home-page");

			}

		};
	}

}