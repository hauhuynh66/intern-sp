package com.intern.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {
    @Autowired
    private LoginAttempService loginAttempService;
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        WebAuthenticationDetails authenticationDetails =
                (WebAuthenticationDetails)authenticationSuccessEvent.getAuthentication().getDetails();
        loginAttempService.loginSucceeded(authenticationDetails.getRemoteAddress());
    }
}
