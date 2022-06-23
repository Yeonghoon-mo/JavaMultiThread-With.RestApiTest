package com.attoresearchtest.domain.host;

import com.attoresearchtest.exception.CustomException;
import com.attoresearchtest.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HostService {

    private final HostRepository hostRepository;

    // Host 생성
    @Transactional
    public Integer saveHost(final HostRequest params) {
        checkDuplicate(params); // NAME, IP 중복체크
        hostCount(); // Host 총 개수 확인 (삭제된 Host 제외)
        Host host = hostRepository.save(params.toEntity());
        return host.getId();
    }

    // Host 수정
    @Transactional
    public Integer updateHost(final Integer id, final HostRequest params) {
        Host host = hostRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        host.update(params);
        return host.getId();
    }

    // Host 삭제
    @Transactional
    public Integer deleteHost(final Integer id) {
        Host host = hostRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        host.delete();
        return id;
    }

    // 특정 호스트의 현재 Alive 상태 조회
    public HostResponse findById(final Integer id) {
        Host host = hostRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));

        InetAddress aliveState;
        String aliveStatus = null;
        try {
            aliveState = InetAddress.getByName(host.getIpAddress());
        } catch (UnknownHostException e) {
            throw new CustomException(ErrorCode.IPADDRESS_NOT_FOUND);
        }

        try {
            if(aliveState.isReachable(1000)) { // 1초
                aliveStatus = "Reachable"; // 응답이 된 경우
            } else {
                aliveStatus = "Unreachable"; // 응답이 안된 경우
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HostResponse(host, aliveStatus);
    }

    // 호스트들의 Alive 모니터링 결과 조회
    public List<HostResponse> findAll() {

    }

    // Name, IpAddress 중복체크
    public void checkDuplicate(final HostRequest params) {
        // true = 중복, false = 미중복
        if (hostRepository.existsByName(params.getName()))  {
            throw new CustomException(ErrorCode.DUPLICATE_NAME_ERROR);
        }

        // ipAddress 중복체크
        if (hostRepository.existsByIpAddress(params.getIpAddress())) {
            throw new CustomException(ErrorCode.DUPLICATE_IPADDRESS_ERROR);
        }
    }

    // Host 개수 확인 (삭제되지 않은 호스트가 100개 이상 등록되어 있을 시 등록 불가)
    public void hostCount() {
        if(hostRepository.hostCount() > 100) {
            throw new CustomException(ErrorCode.HOST_MAX_COUNT_ERROR);
        }
    }

}
