---
name: backend-dev-style
description: Provides core backend development patterns for the Datecourse project, including layered architecture, TDD, Spring REST Docs, and rich domain modeling. Use this skill when implementing new backend features, designing entities, or creating API controllers.
---

# Backend Development Style Skill

This skill guides the implementation of backend features in the Datecourse project, ensuring consistency with existing architectural principles and coding standards.

## Core Principles

- **Layered Architecture**: Controller -> Service -> Repository. No reverse dependencies.
- **TDD (Test Driven Development)**: Write failing tests before implementation.
- **Rich Domain Model**: Entities encapsulate state and business logic.
- **Standardized API**: Consistent `ApiResponse` and versioned paths.
- **Automated Documentation**: Spring REST Docs for all API endpoints.

## Workflow & Patterns

### 1. Entity Design
Follow the **Rich Domain Model** approach. Encapsulate data and provide business logic methods within the entity.
- See [entity-pattern.md](references/entity-pattern.md) for implementation details and `@Setter` restrictions.

### 2. API Implementation
Use the standardized `ApiResponse` wrapper for all controller responses. Ensure proper transaction management in services.
- See [api-pattern.md](references/api-pattern.md) for response structures, controller/service layer guidelines, and error handling.

### 3. Testing & Documentation
Every new API endpoint MUST have a corresponding test that generates REST Docs snippets.
- See [test-docs-pattern.md](references/test-docs-pattern.md) for TDD steps, MockMvc setup, and documentation field mappings.

## Quick Start for AI

When asked to implement a new backend feature:
1. **Research** the requirements and identify necessary entity changes or additions.
2. **Strategy**: Plan the failing test cases first.
3. **Execution**:
   - Create/Update Entity using the [entity-pattern](references/entity-pattern.md).
   - Create/Update Service with `@Transactional`.
   - Implement Controller using the [api-pattern](references/api-pattern.md).
   - Generate REST Docs using the [test-docs-pattern](references/test-docs-pattern.md).
