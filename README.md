# Spring Boot Backend Fix — LazyInitializationException

Independent technical case study demonstrating a common, reproducible Spring Boot/JPA debugging problem.

> **Disclosure:** This is an independent technical demonstration. It is not a client project and does not claim paid customer results.

## Problem

A REST endpoint returns a JPA entity with a `LAZY` relationship. The controller/Jackson layer accesses that relationship after the repository transaction has completed, causing `LazyInitializationException` when Open Session in View is disabled.

## Reproduce

```bash
./gradlew test
```

The integration tests demonstrate both sides of the problem:

- `/api/orders/broken/{id}` — reproduces the failure.
- `/api/orders/{id}` — demonstrates the fixed approach.

## Root cause

The `Customer` association is configured as `FetchType.LAZY`. The broken service returns the entity after the repository call has completed. The persistence context is therefore closed before the web layer tries to serialize the lazy association.

## Fix

The fixed service uses an explicit read-only transaction and maps the entity to a response DTO while the persistence context is active.

This keeps the persistence model behind the API boundary instead of relying on the controller/serializer to initialize lazy relationships.

## Verification

The integration test suite verifies that the broken endpoint returns a server error containing `LazyInitializationException` and that the fixed endpoint returns the expected JSON DTO.

## Why this matters for debugging

The visible failure is a serialization/API error. The real cause is the transaction and persistence-context boundary. A useful backend fix therefore requires reproducing the issue, identifying the lifecycle boundary, applying a targeted change, and adding a regression test.

## Stack

- Java 17
- Spring Boot 3.5.16
- Spring Web
- Spring Data JPA
- Hibernate
- H2
- JUnit 5 / Spring Test

Spring Boot 3.5.16 is a current stable line and requires Java 17+. See the official Spring Boot documentation for version/system requirements.
