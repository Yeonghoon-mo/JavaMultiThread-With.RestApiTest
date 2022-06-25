package com.attoresearchtest.domain.host;

import com.attoresearchtest.exception.CustomException;
import com.attoresearchtest.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HostService {

    private final HostRepository hostRepository;

    // Host 생성
    public Integer saveHost(final HostRequest params) {
        checkDuplicate(params); // NAME, IP 중복체크
        hostCount(); // Host 총 개수 확인 (삭제된 Host 제외)
        Host host = hostRepository.save(params.toEntity());
        return host.getId();
    }

    // Host 수정
    public Integer updateHost(final Integer id, final HostRequest params) {
        Host host = hostRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        host.update(params);
        return host.getId();
    }

    // Host 삭제
    public Integer deleteHost(final Integer id) {
        Host host = hostRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        host.delete();
        return id;
    }

    // 2.특정 호스트의 현재 Alive 상태 조회
    public HostResponse findById(final Integer id) {
        Host host = hostRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POSTS_NOT_FOUND));
        aliveStatusCheck(host);
        return new HostResponse(host);
    }

    // 3.호스트들의 Alive 모니터링 결과 조회
    public List<Host> findAll() {
        List<Host> list = hostRepository.findByHostList(); // 도메인 목록 조회
        list.parallelStream().map(this::parallelStream).collect(Collectors.toList()); // 스트림 병렬처리
        return list;
    }

    // 병렬 Stream Method
    public Host parallelStream(Host host) {
        aliveStatusCheck(host);
        return host;
    }

    // Status Check Method
    private void aliveStatusCheck(Host host) {
        InetAddress aliveState;
        try {
            aliveState = InetAddress.getByName(host.getIpAddress());
        } catch (UnknownHostException e) {
            throw new CustomException(ErrorCode.IPADDRESS_NOT_FOUND);
        }

        try {
            if (aliveState.isReachable(1000)) { // 1초
                host.statusUpdate("Reachable"); // 응답이 된 경우
                host.statusTimeUpdate();
            } else {
                host.statusUpdate("Unreachable"); // 응답이 안된 경우
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Name, IpAddress Duplicate Check Method
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

    // Host Count Check Method (삭제되지 않은 호스트가 100개 이상 등록되어 있을 시 등록 불가)
    public void hostCount() {
        if(hostRepository.hostCount() > 100) {
            throw new CustomException(ErrorCode.HOST_MAX_COUNT_ERROR);
        }
    }

}
