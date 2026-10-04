# Case Study 01 — Spring Boot LazyInitializationException

## Context

This independent reproduction models a small backend issue that can block a developer: a REST endpoint fails while serializing a lazy JPA association.

## Investigation

1. Reproduce the endpoint with Open Session in View disabled.
2. Observe the server error during serialization.
3. Trace the failure to access of a lazy association after the repository transaction has ended.
4. Move the required read into an explicit transaction.
5. Map to a DTO inside that transaction.
6. Add an integration test for the fixed endpoint.

## Root cause

`PurchaseOrder.customer` is `LAZY`. The broken service returns the entity after `findById()` completes, so the persistence context is closed. The serializer later calls `getCustomer()` and Hibernate cannot initialize the association.

## Fix strategy

Use a read-only service transaction and map only the data needed by the API into `OrderResponse` before leaving the persistence context.

## Verification

The test suite contains two assertions:

- The broken route fails with a `LazyInitializationException`.
- The corrected route returns HTTP 200 and the expected DTO fields.

## Customer-facing takeaway

For a scoped backend debugging engagement, the deliverable is not just a code edit. It is a reproducible diagnosis, the targeted fix, and evidence that the failure mode is covered by a regression test.
