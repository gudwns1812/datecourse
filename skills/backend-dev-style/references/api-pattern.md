# API Design Pattern

## Standard Response Structure

All API responses must use the `ApiResponse` record.

```java
public record ApiResponse<T>(
    ResultType result,
    T data,
    ErrorMessage error
) {
    public static <S> ApiResponse<S> success(S data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }
    // ...
}
```

## Controller Layer

- **RequestMapping**: Use versioned paths (e.g., `api/v1/...`).
- **Dependencies**: Depend only on Service layer.
- **DTOs**: Use specific DTOs for request and response.

## Service Layer

- **Transaction**: Use `@Transactional(readOnly = true)` at class level. Mark modifying methods with `@Transactional`.
- **Validation**: Perform business validation in services and throw `CoreException`.

## Error Handling

- Use `ErrorType` enum and `CoreException`.
- Handled globally by `ApiControllerAdvice`.
