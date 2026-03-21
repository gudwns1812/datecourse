# Entity Design Pattern

## Principles

1. **Rich Domain Model**: Put business logic inside the entity when it belongs there.
2. **Encapsulation**: Never use `@Setter`. Use meaningful method names for state changes.
3. **Controlled Creation**: Use static factory methods for object creation.

## Implementation Example

```java
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // For JPA
@Entity
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String loginId;
    private String password;

    /**
     * Static Factory Method
     */
    public static Member createMember(String username, String loginId, String password) {
        return Member.builder()
                .username(username)
                .loginId(loginId)
                .password(password)
                .build();
    }

    /**
     * Domain Logic
     */
    public boolean isValidPassword(String password) {
        return this.password.equals(password);
    }
}
```

## Guidelines

- **Annotations**: Always use `@Getter`, `@NoArgsConstructor(access = AccessLevel.PROTECTED)`.
- **Primary Key**: Use `GenerationType.IDENTITY` for MySQL compatibility.
- **Fields**: Keep them private.
