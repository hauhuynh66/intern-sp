package com.intern.security;

import com.intern.model.Admin;
import com.intern.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private LoginAttempService loginAttempService;
    @Autowired
    private HttpServletRequest request;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        String IP = getClientIP();
        if(loginAttempService.isBlocked(IP)){
            throw new RuntimeException("Block");
        }else {
            Admin admin = adminRepository.findByMail(s);
            if (admin == null) {
                throw new UsernameNotFoundException("Admin not found");
            }
            return new CustomUserDetails(admin);
        }
    }
    private String getClientIP(){
        String xfHeader = request.getHeader("X-Forward-For");
        if(xfHeader==null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
