package com.company.config;

import com.company.security.CustomAuthenticationFilter;
import com.company.security.oauth.CustomAuthorizationRequestResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//@EnableWebSecurity // do NOT enable web security - will cause circular reference
public class WebSecurityOAuth2Config extends WebSecurityConfigurerAdapter {

    private static final String CLIENT_REGISTRATION_KEY = "spring.security.oauth2.client.registration.";
    private static final String CLIENT_PROVIDER_KEY = "spring.security.oauth2.client.provider.";
    private static List<String> clients = Arrays.asList("facebook", "google", "okta");

//    @Autowired
//    private AuthenticationProvider authenticationProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Environment env;

    private CustomAuthenticationFilter authenticationFilter() {
        return new CustomAuthenticationFilter(authenticationManager);
    }

    private ClientRegistration getRegistration(String client) {
        System.out.println("ENVIRONMENT FOR " + client + ": " + env);
        String clientId = env.getProperty(CLIENT_REGISTRATION_KEY + client + ".client-id");
        System.out.println("CLIENT ID: " + clientId);
        if (clientId == null) {
            return null;
        }
        String clientSecret = env.getProperty(CLIENT_REGISTRATION_KEY + client + ".client-secret");
        if (client.equals("google")) {
            return CommonOAuth2Provider.GOOGLE.getBuilder(client).clientId(clientId).clientSecret(clientSecret).build();
        }
        if (client.equals("facebook")) {
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client).clientId(clientId).clientSecret(clientSecret).build();
        }
        if (client.equals("okta")) {
            return CommonOAuth2Provider.OKTA.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .authorizationUri(env.getProperty(CLIENT_PROVIDER_KEY + client + ".authorization-uri"))
                    .tokenUri(env.getProperty(CLIENT_PROVIDER_KEY + client + ".token-uri"))
                    .userInfoUri(env.getProperty(CLIENT_PROVIDER_KEY + client + ".user-info-uri"))
                    .jwkSetUri(env.getProperty(CLIENT_PROVIDER_KEY + client + ".jwk-set-uri"))
                    .build();
        }
        return null;
    }

    private ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(this::getRegistration)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        System.out.println("REGISTRATIONS: " + registrations.size());
        return new InMemoryClientRegistrationRepository(registrations);
    }

    private void configureOld(HttpSecurity http) throws Exception {
        http.addFilterBefore(authenticationFilter(), BasicAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/favicon.ico").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable(); // and remember to disable CSRF for post/put requests
    }

    private void configureSimple(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/signin", "/signin/failure", "/favicon.ico")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .loginPage("/signin")
                .tokenEndpoint()
                .accessTokenResponseClient(accessTokenResponseClient())
                .and()
                .defaultSuccessUrl("/")
                .failureUrl("/signin/failure");
    }

    private void configureAdvanced(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/signin", "/signin/failure", "/favicon.ico")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .loginPage("/signin")

                .clientRegistrationRepository(clientRegistrationRepository())
                .authorizedClientService(authorizedClientService())
                .authorizationEndpoint()
                //.baseUri("/oauth2/authorization")
                .authorizationRequestResolver(new CustomAuthorizationRequestResolver(clientRegistrationRepository(),"/oauth2/authorization"))
                .authorizationRequestRepository(authorizationRequestRepository())

                .and()
                .tokenEndpoint()
                .accessTokenResponseClient(accessTokenResponseClient())
                .and()

                .defaultSuccessUrl("/")
                .failureUrl("/signin/failure");
    }

    // From fpr-vertx for FB: https://www.facebook.com/dialog/oauth?client_id=178109302761409&scope=email&response_type=code%2Cgranted_scopes&redirect_uri=https%3A%2F%2Fflightpollutionradar.com%2Ffacebook-callback&state=09d1f449-7913-493c-b547-752a6d2bd080

    /**
     * Spring Security OAuth2 Default:
     * Step 1: Login button/ link for a client should hit <our-server>/oauth2/authorization/<provider-name>
     * Step 2: The above URL sends a redirect to providers OAuth2 server with a parameter called redirect_uri equal to '<our-server>/login/oauth2/code/<provider-name>
     * Step 3: The user will login at the providers server and if login successful, our server is hit (the above redirect_uri parameter) with a querystring parameter called code
     *
     * @param http
     * @throws Exception
     */
    // See https://spring.io/guides/topicals/spring-security-architecture
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        configureSimple(http);
//        configureAdvanced(http);
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new DefaultAuthorizationCodeTokenResponseClient();
    }

}
