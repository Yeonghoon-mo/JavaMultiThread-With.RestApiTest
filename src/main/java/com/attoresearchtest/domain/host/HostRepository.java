package com.attoresearchtest.domain.host;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host, Integer> , HostRepositoryCustom {

    // Name 중복체크
    boolean existsByName(String name);

    // IP 주소 중복체크
    boolean existsByIpAddress(String ipAddress);

}
