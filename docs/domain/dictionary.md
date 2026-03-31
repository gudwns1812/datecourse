## 유비쿼터스 용어 사전

| 용어                   | 의미                                         | 관련 위치                                                                                      |
|----------------------|--------------------------------------------|--------------------------------------------------------------------------------------------|
| Datecourse           | 랜덤 역과 주변 장소를 조합해 데이트 결정을 돕는 서비스 전체         | `README.md`, `frontend/src/app/page.tsx`                                                   |
| Member               | 서비스에 가입한 사용자                               | `backend/src/main/java/com/datecourse/storage/entity/Member.java`                          |
| Guest                | 카카오 OAuth는 끝났지만 로컬 회원 정보 입력이 끝나지 않은 사용자 상태 | `backend/src/main/java/com/datecourse/support/auth/oauth2/KakaoOAuth2UserService.java`     |
| User                 | 서비스 기능을 사용할 수 있는 정회원 권한                    | `backend/src/main/java/com/datecourse/support/auth/UserRole.java`                          |
| LoginId              | 일반 로그인 식별자                                 | `backend/src/main/java/com/datecourse/domain/dto/LoginForm.java`                           |
| Station              | 랜덤 추첨 결과로 노출되는 역 도메인 객체                    | `backend/src/main/java/com/datecourse/domain/station/Station.java`                         |
| SubwayStation        | DB에 저장된 지하철역 영속 엔티티                        | `backend/src/main/java/com/datecourse/storage/entity/SubwayStation.java`                   |
| Lucky Draw           | 메인 화면과 랜덤 역 화면에서 사용하는 추첨 경험 자체             | `frontend/src/components/features/LuckyDraw.tsx`                                           |
| Place                | 역/현재 위치 주변에서 추천되는 장소                       | `backend/src/main/java/com/datecourse/storage/entity/Place.java`                           |
| Tag                  | 장소 성격을 표현하는 표준화 키워드                        | `backend/src/main/java/com/datecourse/storage/entity/Tag.java`                             |
| PlaceTag             | 장소와 태그의 연결 관계                              | `backend/src/main/java/com/datecourse/storage/entity/PlaceTag.java`                        |
| BaseAddress          | 시/도, 구/군만 담는 기본 주소 값 객체                    | `backend/src/main/java/com/datecourse/storage/entity/BaseAddress.java`                     |
| FullAddress          | 기본 주소와 상세 주소를 함께 담는 값 객체                   | `backend/src/main/java/com/datecourse/storage/entity/FullAddress.java`                     |
| PlaceSearchBounds    | 지도에 보이는 영역을 표현하는 검색 범위                     | `backend/src/main/java/com/datecourse/domain/place/PlaceSearchBounds.java`                 |
| Recommendation Score | 거리 점수와 텍스트 점수를 합쳐 계산한 장소 추천 점수             | `backend/src/main/java/com/datecourse/domain/place/PlaceRecommendationScoreProcessor.java` |
| AuthSync             | 프론트에서 서버 세션 상태를 지속 동기화하는 컴포넌트              | `frontend/src/components/common/AuthSync.tsx`                                              |
