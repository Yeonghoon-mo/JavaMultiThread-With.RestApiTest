package com.attoresearchtest;

import com.attoresearchtest.domain.host.Host;
import com.attoresearchtest.domain.host.HostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestClass {

    @Autowired
    HostRepository hostRepository;

    @Test
    void 호스트등록() {
        for(int i=1; i<=100; i++) {
            Host params = Host.builder()
                    .name("localhost")
                    .ipAddress("192.168.75.86")
                    .deleteYn("N")
                    .build();
            hostRepository.save(params);
        }
    }
}
