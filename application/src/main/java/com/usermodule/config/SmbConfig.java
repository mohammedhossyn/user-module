package com.usermodule.config;

import com.usermodule.service.system.OptionService;
import jcifs.CIFSContext;
import jcifs.Credentials;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class SmbConfig {

    @Value("${smbUsername}")
    private String smbUsername;
    @Value("${smbPassword}")
    private String smbPassword;
    private final OptionService optionService;

    @Bean
    public CIFSContext cifsContext() {
        String username = optionService.getStringValueByCode(smbUsername);
        String password = optionService.getStringValueByCode(smbPassword);
        String domain = "";
        SingletonContext baseContext = SingletonContext.getInstance();
        Credentials credentials = new NtlmPasswordAuthenticator(domain, username, password);
        return baseContext.withCredentials(credentials);
    }
}
