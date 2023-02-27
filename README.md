# 프로젝트 소개
- 해당 프로젝트는 **Rest Api를** 이용하여 Host들의 상태를 체크하는 프로젝트입니다.
- Java8에서 추가된 Stream을 사용하고 있지만, **정확한 이해없이 사용하고 있는 것 같아** 진행하게 된 프로젝트 입니다.
- **Java8의 Stream을 사용**하여 Collection Data를 불러오는 방식을 채택하였습니다. (간단한 병렬처리와 코드의 양을 간결하게 줄이기 위하여)
- Host들의 Alive 상태 확인은 **InetAddress.isReachable()** 메서드를 사용하였습니다.

# Stream CRUD Test 
- 개발기간은 **2022-06-23 ~ 2022-06-26 총 4일** 입니다.
- Api Test Tool은 **Postman**을 사용하였습니다.

## 1. 프로젝트 개발 환경 및 사용 기술
------------------------------
> ### IDE
- IntelliJ Ultimate

- JavaSE 1.8
- Spring boot 2.7.0
- Gradle 7.4.1
- Lombok 1.18.22
- JPA
- QueryDSL 5.0.0

## 2. DB테이블 구조
------------------------------
- **tb_host**
  ![db_table](https://user-images.githubusercontent.com/54883318/175807554-a88fbf16-9282-4c68-8a27-5fc79e31ec36.JPG)
- 특정 호스트 조회, 호스트 리스트 조회 시 **Alive 상태와 마지막 Alive 시간**을 사용자에게 보여주기 위해 status, status_alive_time 컬럼 추가.
- **delete_yn** 컬럼은 중요한 데이터 손실을 방지하기 위하여 실제 데이터를 삭제하지 않고, **상태값이 'N'으로 지정된 데이터만 노출**.

## 3. 호스트 등록 관리 REST API
------------------------------
- **3.1 조회(특정 호스트와 호스트 List 조회)** <br>
  ![조회](https://user-images.githubusercontent.com/54883318/175805231-bfaba711-b644-49b3-b793-8051e26c08f2.JPG)
- **3.2 생성** <br>
  ![생성](https://user-images.githubusercontent.com/54883318/175805263-8ede8017-e9af-4663-a2a4-007039be5612.JPG)
- **3.3 수정** <br>
  ![수정](https://user-images.githubusercontent.com/54883318/175805264-65c304ee-0570-4faa-aa5f-19a37d598745.JPG)
- **3.4 삭제** <br>
  ![삭제](https://user-images.githubusercontent.com/54883318/175805261-783472eb-3180-4a9d-8497-9861f903d482.JPG)

- **@RestController** 어노테이션 추가로 **@ResponseBody** 어노테이션을 사용하지 않아도 **문자열과 JSON 등**을 전송할 수 있도록 하였습니다.
- **@RequestMapping("/api/host")** 어노테이션 추가로 일일히 Mapping을 주지 않아도 Mapping이 이루어질 수 있도록 설계하였습니다.
- Lombok 라이브러리의 기능중 하나로, **@RequiredArgsConstructor** 어노테이션을 사용하여 **생성자**를 편리하게 만들었습니다.
- **생성과 수정**은 @RequestBody 어노테이션을 사용하여 파라미터로 넘어온 json형태의 데이터를 받아올 수 있도록 하였습니다.
- **수정, 삭제, 특정 Host조회**는  @PathVariable 어노테이션을 사용하여 HOST PK를 받아올 수 있도록 하였습니다.

## 4. Name, Ip 중복체크
------------------------------
![Name, Ip 중복체크](https://user-images.githubusercontent.com/54883318/175805673-2515889e-6dfc-4aed-ac53-16c70f7b3689.JPG)
![전역 예외 처리 List](https://user-images.githubusercontent.com/54883318/175806160-15b29a68-40c7-49c0-80cc-03792e95e07d.JPG)
- Service(비즈니스 로직)단에서 Name과 Ip의 중복을 체크하는 Method를 Spring Data Jpa에 있는 exists 쿼리를 사용하였습니다.
- **True**면 중복, **False**면 중복이 아니며, 중복될 경우 해당 필드가 중복되었음을 출력하기 위해, **전역 예외 처리 방식**으로 예외처리를 하였습니다.

## 5. 등록, 수정시간 Select & Update
------------------------------
![등록, 수정시간](https://user-images.githubusercontent.com/54883318/175805915-71380ba8-58b2-47fe-a20c-9246e7564d20.JPG)
- 해당 사진은 **Host Entity Class** 사진입니다.
- 등록 시간은 **데이터가 생성이 될 때**에, LocalDateTime클래스에 있는 now() 메서드를 사용하여 **등록 시간을 저장**할 수 있도록 하였습니다.
- 수정 시간은 **Host가 수정이 될 때**에,**modifiedDate** 필드의 값을 now() 메서드를 사용하여 **수정 시간을 업데이트** 할 수 있도록 설계하였습니다.

## 6. 호스트 등록 개수 제한
------------------------------
![Host 등록 개수_1](https://user-images.githubusercontent.com/54883318/175806027-dfeef926-a702-44da-82f2-8cd3092d3d5c.JPG)
![Host 등록 개수_2](https://user-images.githubusercontent.com/54883318/175806029-5ef08785-0eb8-40db-8880-68967f424731.JPG)
- Host 등록 개수 제한은 Method를 만들어서 따로 빼두었습니다.
- **Host를 등록할 때**에 hostCount() Method가 수행이 됩니다.
- QueryDSL을 사용하여 **deleteYn 컬럼이 'N'값인 값**들을 출력하도록 하였으며, **100개 이상일 경우**에 예외처리를 하였습니다.

## 7. 특정 호스트의 현재 Alive 상태 조회 REST API
------------------------------
![특정 호스트 조회](https://user-images.githubusercontent.com/54883318/175806245-1b2f28bf-db0b-4bb1-8203-e69ed873cd20.JPG)
![상태 조회 메소드](https://user-images.githubusercontent.com/54883318/175806247-2ca12499-cb5b-459c-bc44-bdbfeee30db8.JPG)
- 특정 호스트 조회는 **Jpa Repository를 사용함으로써 해당 Host의 PK를 기준**으로 조회를 합니다.
- 호스트들의 리스트, 단일 호스트 조회 시 사용되는 **Status Check Method는 하나의 Method(aliveStatusCheck())로** 사용합니다.
- **aliveStatusCheck() Method**는 **매개변수로 받은 host의 IpAddress를** 받아오며, IP주소에 문제가 있을 경우 **Exception이 발생**합니다.
- Host의 상태 확인을 위해 **InetAddress.isReachable()** 를 사용하였으며, **Network Error가 발생하면 IOException이 발생**합니다.
- Host가 **1초 내에 응답이 된 경우**, status 필드에 "Reachable" 문자열을 저장하며, 현재 시간을 status_alive_time 필드에 저장합니다.
- Host가 **1초 내에 응답이 되지 않았을 경우**, status 필드에 "Unreachable" 문자열을 저장합니다.

## 8. 호스트들의 Alive 모니터링 결과 조회 REST API
------------------------------
![호스트 리스트 조회](https://user-images.githubusercontent.com/54883318/175806625-ba816c3d-b094-47b8-86e6-29e06ea33216.JPG)
- 호스트들의 리스트 조회는 **for문을 통해 차례대로 조회하면 상태 확인에서 너무 많은 시간이 소요**되어, **Java Stream의 parallelStream Method**를 사용하여 **병렬 스트림** 처리를 하였습니다.

## 9. 한계점
------------------------------
- 호스트 리스트 조회 시, 전체 상태가 "Unreachable" 상태여도 1초 내에 응답이 가능하여야 하는 요구사항을 충족시키지 못하였습니다. (소요 시간 : 8초~9초)
- 학생 시절 배웠던 **멀티 쓰레드**가 생각이 나서 찾아보았는데, 쓰레드 기본 설정이 **8개**여서 8~9초가 소요되는 것을 알았으며,
- **ForkJoinPool**이라는 Java Concurrency툴을 구글링을 통해 알게 되었고, 쓰레드의 개수를 직접 설정하여 생성할 수 있다는 것을 알았지만, Type Error에 막히게 되었고, Type을 Casting해주어도 또 다른 에러에 직면하게 되었고, 결국에는 시간안에 해결을 할 수 없었습니다.

## 10. Api Test 동영상
------------------------------
- **동영상 내용이 잘 보이지 않아서, Video Directory 안에 따로 동영상 파일도 업로드 하였습니다.**

- 호스트 저장 (URI = Post, "/api/host") <br>
![ezgif com-gif-maker](https://user-images.githubusercontent.com/54883318/175808753-d8b6e81f-c99f-46ef-b387-443c46e0ac3d.gif) 
- 호스트 수정 (URI = Patch, "/api/host/${id}") <br>
![ezgif com-gif-maker (1)](https://user-images.githubusercontent.com/54883318/175809003-d8168e8d-5cd9-4cdf-b757-17fa8ee14053.gif)
- 호스트 삭제(deleteYn 상태값 'Y'로 변경, URI = Delete, "/api/host/${id}") <br>
![ezgif com-gif-maker (2)](https://user-images.githubusercontent.com/54883318/175809201-f81eeb7e-dde6-4f10-a6b5-f240449c6fee.gif)
- 단일 호스트 조회 (응답이 된 경우, URI = Get, "/api/host/${id}") <br>
![ezgif com-gif-maker](https://user-images.githubusercontent.com/54883318/175809557-aa3d880e-7dc7-4d4d-86fd-2d219316ab9c.gif)
- 단일 호스트 조회 (응답이 안된 경우, URI = Get, "/api/host/${id}") <br>
![ezgif com-gif-maker (1)](https://user-images.githubusercontent.com/54883318/175809721-6596fc54-db3d-4d17-8ef7-a9312a707f40.gif)
- 호스트 리스트 조회 (delete_yn = 'N'이고, 100개 리스트의 상태가 전부 "Reachable" 일 경우, URI = Get, "/api/host") <br>
![ezgif com-gif-maker (1)](https://user-images.githubusercontent.com/54883318/175810253-8b969da1-0469-4401-b1db-2305e8a35e11.gif)
- 호스트 리스트 조회 (delete_yn = 'N'이고, 100개 리스트의 상태가 전부 "Unreachable" 일 경우) <br>
![ezgif com-gif-maker](https://user-images.githubusercontent.com/54883318/175810095-179dfe48-c2fe-4b0f-9d83-bba543905595.gif)

------------------------------
