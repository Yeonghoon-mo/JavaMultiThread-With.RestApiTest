package com.attoresearchtest.domain.host;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HostRequest {

    private String name;                      // 호스트 이름
    private String ipAddress;                 // IP 주소
    private String status;                    // alive 상태
    private String deleteYn;                  // 삭제 여부

    public Host toEntity() {
        return Host.builder()
                .name(name)
                .ipAddress(ipAddress)
                .status(status)
                .deleteYn(deleteYn)
                .build();
    }

}
