package com.company.security.oauth;

import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

//    private OAuth2AuthorizationRequestResolver defaultResolver;
//
//    public CustomAuthorizationRequestResolver(ClientRegistrationRepository repo, String authorizationRequestBaseUri){
//        defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri);
//    }

//    @Override
//    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
//        OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
//        System.out.println("CUSTOM RESOLVE: " + req);
//        if (req != null) {
//            req = customizeAuthorizationRequest(req, request);
//        }
//        return req;
//    }
//
//    @Override
//    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
//        OAuth2AuthorizationRequest req = defaultResolver.resolve(request, clientRegistrationId);
//        if (req != null) {
//            req = customizeAuthorizationRequest(req, request);
//        }
//        return req;
//    }

//    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest req, HttpServletRequest request) {
//        Map<String, Object> extraParams = new HashMap<>(req.getAdditionalParameters()); //VIP note
////        extraParams.put("test", "extra");
//        System.out.println("here =====================");
//        final String protoSchemeHeader = "x-forwarded-proto";
//        if (request.getHeader(protoSchemeHeader) != null) {
//            return OAuth2AuthorizationRequest.from(req)
//                    .additionalParameters(extraParams)
//                    .redirectUri(
//                            UrlUtils.buildFullRequestUrl(
//                                request.getHeader(protoSchemeHeader),
//                                request.getServerName(),
//                                443,
//                                "/login/oauth2/code/okta", // TODO: Do not hardcode client id
//                                request.getQueryString()
//                            )
//                    )
//                    .build();
//        }
//        return OAuth2AuthorizationRequest.from(req).additionalParameters(extraParams).build();
//    }

//    private OAuth2AuthorizationRequest customizeAuthorizationRequest1(OAuth2AuthorizationRequest req) {
//        return OAuth2AuthorizationRequest.from(req).state("xyz").build();
//    }

//    private OAuth2AuthorizationRequest customizeOktaReq(OAuth2AuthorizationRequest req) {
//        Map<String, Object> extraParams = new HashMap<>(req.getAdditionalParameters());
//        extraParams.put("idp", "https://idprovider.com");
//        return OAuth2AuthorizationRequest.from(req).additionalParameters(extraParams).build();
//    }

    private static final String REGISTRATION_ID_URI_VARIABLE_NAME = "registrationId";
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final AntPathRequestMatcher authorizationRequestMatcher;
    private final StringKeyGenerator stateGenerator = new Base64StringKeyGenerator(Base64.getUrlEncoder());

    /**
     * Constructs a {@code DefaultOAuth2AuthorizationRequestResolver} using the provided parameters.
     *
     * @param clientRegistrationRepository the repository of client registrations
     * @param authorizationRequestBaseUri the base {@code URI} used for resolving authorization requests
     */
    public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository, String authorizationRequestBaseUri) {
        Assert.notNull(clientRegistrationRepository, "clientRegistrationRepository cannot be null");
        Assert.hasText(authorizationRequestBaseUri, "authorizationRequestBaseUri cannot be empty");
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizationRequestMatcher = new AntPathRequestMatcher(authorizationRequestBaseUri + "/{" + REGISTRATION_ID_URI_VARIABLE_NAME + "}");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        System.out.println(request.getRequestURI() + " - " + request.getQueryString());
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            System.out.println("header: " + headerName + " = " + request.getHeader(headerName));
//        }
        String registrationId = this.resolveRegistrationId(request);
        String redirectUriAction = getAction(request, "login");
        return resolve(request, registrationId, redirectUriAction);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String registrationId) {
        if (registrationId == null) {
            return null;
        }
        String redirectUriAction = getAction(request, "authorize");
        return resolve(request, registrationId, redirectUriAction);
    }

    private String getAction(HttpServletRequest request, String defaultAction) {
        String action = request.getParameter("action");
        if (action == null) {
            return defaultAction;
        }
        return action;
    }

    private OAuth2AuthorizationRequest resolve(HttpServletRequest request, String registrationId, String redirectUriAction) {
        if (registrationId == null) {
            return null;
        }

        ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId);
        if (clientRegistration == null) {
            throw new IllegalArgumentException("Invalid Client Registration with Id: " + registrationId);
        }

        OAuth2AuthorizationRequest.Builder builder;
        if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(clientRegistration.getAuthorizationGrantType())) {
            builder = OAuth2AuthorizationRequest.authorizationCode();
        }
        else if (AuthorizationGrantType.IMPLICIT.equals(clientRegistration.getAuthorizationGrantType())) {
            builder = OAuth2AuthorizationRequest.implicit();
        } else {
            throw new IllegalArgumentException("Invalid Authorization Grant Type ("  + clientRegistration.getAuthorizationGrantType().getValue() + ") for Client Registration with Id: " + clientRegistration.getRegistrationId());
        }

        String redirectUriStr = this.expandRedirectUri(request, clientRegistration, redirectUriAction);

        Map<String, Object> additionalParameters = new HashMap<>();
        additionalParameters.put(OAuth2ParameterNames.REGISTRATION_ID, clientRegistration.getRegistrationId());

        return builder
                .clientId(clientRegistration.getClientId())
                .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                .redirectUri(redirectUriStr)
                .scopes(clientRegistration.getScopes())
                .state(this.stateGenerator.generateKey())
                .additionalParameters(additionalParameters)
                .build();
    }

    private String resolveRegistrationId(HttpServletRequest request) {
        if (this.authorizationRequestMatcher.matches(request)) {
            return this.authorizationRequestMatcher
                    .extractUriTemplateVariables(request).get(REGISTRATION_ID_URI_VARIABLE_NAME);
        }
        return null;
    }

    private String resolveClientRequestedUrl(HttpServletRequest request) {
        String headerScheme = "x-forwarded-proto";
        String headerHost = "x-forwarded-host";
        String headerPort = "x-forwarded-port";
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String s = headerNames.nextElement();
            if (s.equalsIgnoreCase(headerHost)) {
                headerHost = s;
            }
            else if (s.equalsIgnoreCase(headerPort)) {
                headerPort = s;
            }
            else if (s.equalsIgnoreCase(headerScheme)) {
                headerScheme = s;
            }
        }
        if (request.getHeader(headerScheme) != null && request.getHeader(headerHost) != null && request.getHeader(headerPort) != null) {
            // String scheme, String serverName, int serverPort, String requestURI, String queryString
            return UrlUtils.buildFullRequestUrl(
                    request.getHeader(headerScheme),
                    request.getHeader(headerHost),
                    Integer.valueOf(request.getHeader(headerPort)),
                    request.getRequestURI(),
                    request.getQueryString()
            );
        }
        return UrlUtils.buildFullRequestUrl(request);
    }

    private String expandRedirectUri(HttpServletRequest request, ClientRegistration clientRegistration, String action) {
        // Supported URI variables -> baseUrl, action, registrationId
        // Used in -> CommonOAuth2Provider.DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}"
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("registrationId", clientRegistration.getRegistrationId());
        String baseUrl = UriComponentsBuilder.fromHttpUrl(resolveClientRequestedUrl(request))
                .replaceQuery(null)
                .replacePath(request.getContextPath())
                .build()
                .toUriString();
        uriVariables.put("baseUrl", baseUrl);
        if (action != null) {
            uriVariables.put("action", action);
        }
        return UriComponentsBuilder.fromUriString(clientRegistration.getRedirectUriTemplate())
                .buildAndExpand(uriVariables)
                .toUriString();
    }

}
