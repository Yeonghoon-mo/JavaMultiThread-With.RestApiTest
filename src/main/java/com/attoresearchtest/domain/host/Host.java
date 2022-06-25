package com.attoresearchtest.domain.host;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "tb_host")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Host {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                                                 // 호스트 번호(PK)
    private String name;                                                // 호스트 이름
    private String ipAddress;                                           // IP 주소
    private String status;                                              // alive 상태
    private String deleteYn;                                            // 삭제 여부
    private final LocalDateTime createdDate = LocalDateTime.now();      // 등록 시간
    private LocalDateTime modifiedDate;                                 // 수정 시간
    private LocalDateTime statusAliveTime;                              // 마지막 연결 시간

    @Builder
    public Host(String name, String ipAddress, String status, String deleteYn) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.status = status;
        this.deleteYn = deleteYn;
    }

    // Host 수정
    public void update(HostRequest params) {
        this.name = params.getName();
        this.ipAddress = params.getIpAddress();
        this.modifiedDate = LocalDateTime.now();
    }

    // Host 삭제
    public void delete() {
        this.deleteYn = "Y";
    }

    // Host Status Alive Update
    public void statusUpdate(String status) {
        this.status = status;
    }

    // Host Status Alive Time Update
    public void statusTimeUpdate() {
        this.statusAliveTime = LocalDateTime.now();
    }



}
