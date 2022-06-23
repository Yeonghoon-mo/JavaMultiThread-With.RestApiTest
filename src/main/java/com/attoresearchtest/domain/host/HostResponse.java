package com.attoresearchtest.domain.host;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class HostResponse {

    private Integer id;                             // 호스트 번호(PK)
    private String name;                            // 호스트 이름
    private String ipAddress;                       // IP 주소
    private String deleteYn;                        // 삭제 여부
    private LocalDateTime createdDate;              // 등록 시간
    private LocalDateTime modifiedDate;             // 수정 시간

    // Entity Class 컬럼에 등록되어 있는 데이터를 받는 것이 아닌 직접 선언한 멤버변수
    private String aliveStatus;                     // Alive 상태 확인

    public HostResponse(Host entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.ipAddress = entity.getIpAddress();
        this.deleteYn = entity.getDeleteYn();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }

    /**
     * 특정 호스트 조회를 위한 생성자
     * @param host - host 정보
     * @param aliveStatus - Host Status
     */
    public HostResponse(Host host, String aliveStatus) {
        this(host);
        this.aliveStatus = aliveStatus;
    }

}
