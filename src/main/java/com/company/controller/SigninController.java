package com.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class SigninController {

    private static String authorizationRequestBaseUri = "oauth2/authorization";

    private Map<String, String> oauth2AuthenticationUrls = new LinkedHashMap<>();

    private final ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public SigninController(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/signin")
    public String signin(Model model, HttpServletRequest request) {
        System.out.println("REQUEST: " + request.getRequestURI());
        System.out.println("SCHEME: " + request.getScheme() + " - " + request.getHeader("x-forwarded-proto"));
        System.out.println("PORT: " + request.getServerPort() + " - " + request.getHeader("x-forwarded-port"));
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);
        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }
        if (clientRegistrations != null) {
            clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        }
        model.addAttribute("urls", oauth2AuthenticationUrls);
        return "signin/index";
    }

}
