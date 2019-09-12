package com.intern.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    @Autowired
    private LoginAttempService loginAttempService;
    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent authenticationFailureBadCredentialsEvent) {
        WebAuthenticationDetails authenticationDetails =
                (WebAuthenticationDetails)authenticationFailureBadCredentialsEvent.getAuthentication().getDetails();
        loginAttempService.loginFailed(authenticationDetails.getRemoteAddress());
    }
}
