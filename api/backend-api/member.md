# Member API 명세서

이 문서는 현재 코드 기준의 회원 인증 관련 API를 정리합니다.

## 공통

- Base Path: `/api/v1/auth`
- 응답 래퍼: `ApiResponse<T>`
- 세션 기반 인증을 사용합니다.

## 1. 회원가입

새 회원을 생성합니다.
카카오 OAuth2 이후 추가 정보를 입력하는 경우에도 같은 엔드포인트를 사용합니다.

### 요청

```http
POST /api/v1/auth/signup HTTP/1.1
Content-Type: application/json

{
  "username": "테스터",
  "loginId": "testuser",
  "password": "test12!!!!",
  "email": "test@example.com",
  "gender": "MALE",
  "phoneNumber": "010-1234-5678",
  "birth": "1999-09-20"
}
```

### 요청 필드

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `username` | `String` | 사용자 이름 |
| `loginId` | `String` | 로그인 아이디 |
| `password` | `String` | 비밀번호 |
| `email` | `String` | 이메일 주소 |
| `gender` | `String` | 성별 (`MALE`, `FEMALE`) |
| `phoneNumber` | `String` | 휴대폰 번호 |
| `birth` | `String` | 생년월일 (`yyyy-MM-dd`) |

### 성공 응답

```json
{
  "result": "SUCCESS",
  "data": 1,
  "error": null
}
```

## 2. 로그인

일반 로그인 아이디/비밀번호로 인증합니다.
성공 시 세션이 생성됩니다.

### 요청

```http
POST /api/v1/auth/login HTTP/1.1
Content-Type: application/json

{
  "loginId": "testuser",
  "password": "test12!!!!"
}
```

### 요청 필드

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `loginId` | `String` | 로그인 아이디 |
| `password` | `String` | 비밀번호 |

### 성공 응답

```json
{
  "result": "SUCCESS",
  "data": "테스터",
  "error": null
}
```

## 3. 아이디 중복 확인

로그인 아이디 사용 가능 여부를 확인합니다.

### 요청

```http
GET /api/v1/auth/check-id?loginId=testuser HTTP/1.1
```

### 성공 응답

```json
{
  "result": "SUCCESS",
  "data": false,
  "error": null
}
```

설명:
- `true`: 이미 존재하는 아이디
- `false`: 사용 가능한 아이디

## 4. 현재 세션 사용자 확인

현재 세션이 인증된 사용자와 연결되어 있는지 확인합니다.
프론트 `AuthSync`가 이 API를 사용해 로그인 상태를 동기화합니다.

### 요청

```http
GET /api/v1/auth/me HTTP/1.1
Cookie: JSESSIONID=...
```

### 성공 응답

```json
{
  "result": "SUCCESS",
  "data": "테스터",
  "error": null
}
```

### 실패 응답 예시

```json
{
  "result": "FAIL",
  "data": null,
  "error": {
    "code": "A001",
    "message": "인증되지 않은 회원입니다.",
    "data": null
  }
}
```
