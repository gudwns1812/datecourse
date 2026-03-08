# Member API 명세서

이 문서는 회원 가입 및 로그인과 관련된 API 정보를 담고 있습니다.

## 1. 회원 가입 (Signup)
회원가입 요청을 처리하여 새로운 회원을 생성합니다.

### 📝 요청 (HTTP Request)
```http
POST /v1/auth/signup HTTP/1.1
Content-Type: application/json
Content-Length: 127
Host: localhost:8080

{"username":"테스터","loginId":"test","password":"test!","email":"test@test.com","gender":"M","phoneNumber":"010-1234-5678"}
```

### 📋 요청 필드 (Request Fields)
| 경로 | 타입 | 설명 |
| :--- | :--- | :--- |
| `username` | `String` | 사용자 이름 |
| `loginId` | `String` | 로그인 아이디 |
| `password` | `String` | 비밀번호 |
| `email` | `String` | 이메일 주소 |
| `gender` | `String` | 성별 (M/F) |
| `phoneNumber` | `String` | 휴대폰 번호 |

### ✅ 응답 (HTTP Response)
```http
HTTP/1.1 200 OK
Content-Type: application/json

{"result":"SUCCESS","data":null,"error":null}
```

---

## 2. 로그인 (Login)
로그인 요청을 처리하여 인증을 수행합니다. 성공 시 세션에 회원 정보를 저장합니다.

### 📝 요청 (HTTP Request)
```http
POST /v1/auth/login HTTP/1.1
Content-Type: application/json
Content-Length: 37
Host: localhost:8080

{"loginId":"test","password":"test!"}
```

### 📋 요청 필드 (Request Fields)
| 경로 | 타입 | 설명 |
| :--- | :--- | :--- |
| `loginId` | `String` | 로그인 아이디 |
| `password` | `String` | 비밀번호 |

### ✅ 응답 (HTTP Response)
```http
HTTP/1.1 200 OK
Content-Type: application/json

{"result":"SUCCESS","data":"테스터","error":null}
```
