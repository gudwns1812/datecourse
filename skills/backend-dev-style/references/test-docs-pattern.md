# Testing & Documentation Pattern

## TDD (Test Driven Development)

1. Write a failing test for the new feature.
2. Implement the minimum code to pass the test.
3. Refactor.

## Spring REST Docs

All Controller tests must generate documentation snippets.

### Setup

```java
@WebMvcTest(TargetController.class)
@ExtendWith(RestDocumentationExtension.class)
class TargetControllerTest {
    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }
}
```

### Documentation Example

```java
mockMvc.perform(post("/api/v1/...")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
    .andExpect(status().isOk())
    .andDo(document("doc-id",
        requestFields(
            fieldWithPath("field1").description("Description")
        ),
        responseFields(
            fieldWithPath("result").description("SUCCESS/FAIL"),
            fieldWithPath("data").description("Returned data"),
            fieldWithPath("error").description("Error details")
        )
    ));
```

## Guidelines

- **Mocking**: Use `@MockitoBean` for services.
- **Exceptions**: Document error responses as well.
- **Build**: Ensure `copyDocument` task runs during build.
