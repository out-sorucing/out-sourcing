# 11조 Out Sourcing 프로젝트
## 팀원
- 이하승 : 메뉴
- 심윤호 : 주문
- 김지혜 : 회원가입+리뷰
- 문정원 : 가게

## ERD
![erd](https://github.com/user-attachments/assets/288ff388-107b-4375-9fc1-f47958f50253)

## 와이어프레임
[https://app.visily.ai/projects/96e7e630-b7af-491c-a53a-5fa8aba4899c/boards/1387034](https://app.visily.ai/projects/96e7e630-b7af-491c-a53a-5fa8aba4899c/boards/1387034)
## API명세서
https://www.notion.so/teamsparta/12a2dc3ef5148103a07dd4f9fc67b608?v=12a2dc3ef51481f785a8000cb1148244&pvs=4
## 기능 요구 사항

### **1. 회원가입/로그인**

- 회원가입
    - 사용자 아이디
        - 사용자 아이디는 이메일 형식이어야 합니다.
    - 비밀번호
        - `Bcrypt`로 인코딩합니다.
            - 암호화를 위한 `PasswordEncoder`를 직접 만들어 사용합니다.
        - 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함합니다.
        - 비밀번호는 최소 8글자 이상이어야 합니다.
    - 권한
        - 회원가입시 일반 유저(USER) 또는 사장님(OWNER)으로 가입할 수 있습니다.
        - 권한에 따라 사용할 수 있는 기능이 나뉘어집니다.
    
    <aside>
    
    **⚠️ 예외처리**
    
    - 중복된 `사용자 아이디`로 가입하는 경우
    - `사용자 아이디` 이메일과 비밀번호 형식이 올바르지 않은 경우
    </aside>
    
- 회원탈퇴
    
    회원탈퇴 방식을 어떻게 처리할지 고민해보세요.
    
    - 조건
        - 탈퇴 처리 시 `비밀번호`를 확인한 후 일치할 때 탈퇴 처리합니다.
        - 탈퇴한 사용자의 아이디는 재사용할 수 없고, 복구할 수 없습니다.
    
    <aside>
    
    **⚠️ 예외처리**
    
    - `사용자 아이디`와 `비밀번호`가 일치하지 않는 경우
    - 이미 탈퇴한 `사용자 아이디`인 경우
    </aside>
    
- 로그인
    - 가입한 아이디와 비밀번호로 로그인 합니다.

### **2. 가게**

- 가게 생성/수정
    - 가게는 오픈 및 마감 시간이 있습니다.
    - 가게는 최소 주문 금액이 있습니다.
    
    <aside>
    
    **⚠️ 예외처리**
    
    - 사장님 권한을 가진 유저만 가게를  만들 수 있습니다.
    - 사장님은 가게를 최대 3개까지만 운영할 수 있습니다.
    </aside>
    
- 가게 조회
    - 고객은 가게명으로 가게를 여러 건 찾아볼 수 있습니다.
        - 가게 다건 조회시에는 메뉴 목록을 함께 볼 수 없습니다.
    - 가게 단건 조회 시 등록된 메뉴 목록도 함께 볼 수 있습니다.
- 가게 폐업
    - 폐업시, 가게의 상태만 폐업 상태로 변경됩니다.
        - 가게 조회 시 나타나지 않습니다.
        - 사장님은 가게를 추가로 등록할 수 있게 됩니다.

### **3.  메뉴**

- 메뉴 생성/수정
    - 메뉴 생성, 수정은 사장님만 할 수 있습니다.
    - 사장님은 본인 가게에만 메뉴를 등록할 수 있습니다.
- 메뉴를 단독으로 조회할 수는 없으며, 가게 조회 시 함께 조회됩니다.
- 메뉴 삭제
    - 본인 가게의 메뉴만 삭제할 수 있습니다.
    - 삭제 시, 메뉴의 상태만 삭제 상태로 변경됩니다.
        - 가게 메뉴 조회 시 삭제된 메뉴는 나타나지 않습니다.
        - 주문 내역 조회 시에는 삭제된 메뉴의 정보도 나타납니다.

### **4.  주문**

- 고객은 메뉴를 주문할 수 있습니다.
    - 각 주문에는 하나의 메뉴만 주문할 수 있습니다.
        
        
- 사장님은 주문을 수락할 수 있으며, 배달이 완료될 때까지의 모든 상태를 순서대로 변경 합니다.
- 주문 요청 및 상태 변경
    - 새로운 주문이거나 주문의 상태가 변경될 때는 AOP에 의해 로그를 남겨야합니다.
        - 로그에는 `요청 시각`, `가게 id`, `주문 id`가 필수로 포함되어야합니다.
    
    <aside>
    
    **⚠️ 예외처리**
    
    - 가게에서 설정한 최소 주문 금액을 만족해야 주문이 가능합니다.
    - 가게의 오픈/마감 시간이 지나면 주문할 수 없습니다.
    </aside>
    

### **5.  리뷰**

- 리뷰 생성
    - 고객은 주문 건에 대해 리뷰를 작성할 수 있습니다.
    - 리뷰는 별점을 부여합니다.(1~5점)
    
    <aside>
    
    **⚠️ 예외처리**
    
    - `배달 완료` 되지 않은 주문은 리뷰를 작성할 수 없습니다.
    - 자신의 주문이 아닌 주문에 리뷰를 작성할 수 없습니다.
    - 한 주문에 한 리뷰만 작성할 수 있습니다.
    </aside>
    
- 리뷰 조회
    - 리뷰는 단건 조회할 수 없습니다.
    - 리뷰는 가게 정보를 기준으로 다건 조회 가능하며, 최신순으로 정렬합니다.
    - 리뷰를 별점 범위에 따라 조회할 수 있습니다.
        - ex) 3~5점
