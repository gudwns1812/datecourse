# Station API 명세서

이 문서는 지하철 역 정보 조회와 관련된 API 정보를 담고 있습니다.

## 1. 무작위 역 조회 (Random Station)
무작위로 하나의 지하철역 정보를 반환합니다. 인증된 세션이 필요합니다.

### 📝 요청 (HTTP Request)
```http
GET /v1/stations/random HTTP/1.1
Host: localhost:8080
```

### ✅ 응답 (HTTP Response)
```http
HTTP/1.1 200 OK
Content-Type: application/json

{"result":"SUCCESS","data":{"lineNumbers":["2호선"],"stationName":"강남역","stationAddress":"서울특별시 강남구 강남대로 396"},"error":null}
```

### 📋 응답 필드 (Response Fields)
| 경로 | 타입 | 설명 |
| :--- | :--- | :--- |
| `result` | `String` | 결과 상태 (SUCCESS/ERROR) |
| `data` | `Object` | 역 정보 데이터 |
| `data.lineNumbers` | `Array` | 호선 정보 리스트 |
| `data.stationName` | `String` | 지하철역 이름 |
| `data.stationAddress` | `String` | 상세 주소 |
| `error` | `Null` | 에러 발생 시 상세 정보 (또는 null) |
