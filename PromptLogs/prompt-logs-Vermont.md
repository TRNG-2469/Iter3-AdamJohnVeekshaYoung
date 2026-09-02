# ERS Iteration 2 — Prompt Log

Literal prompts from this conversation, in order, with a brief note on the outcome of each.
 
---

## Prompt 1

> I just created 'createUserDto.java' I want to make sure that when the create user function is called, it uses this file so that it doesn't try to implement 'role' in the model, instead takes the default value of 'EMPLOYEE' like in the schema. Is there any additional code that needs to be added for this functionality to run as intended?

**Outcome:** Explained that Hibernate always sends an explicit value per mapped column on INSERT, so the SQL-level `DEFAULT 'EMPLOYEE'` won't apply unless the column is omitted entirely — which Spring Data JPA won't do. Updated `UserController` to accept `createUserDto`, updated `UserService`/`UserServiceImpl` to map DTO → entity and explicitly set `Role.EMPLOYEE`. Flagged that the password was being passed through unhashed.
 
---

## Prompt 2

> The flag about hashing the password has been noted. For now I want to test out some other functionality. I need a user in the database so I can authenticate my HTTP requests but I have no data in my db currently. I want to use an SQL script ran in dbeaver but I've heard from one of my partners on this project that the application needs the password to be hashed in the db. How should I go about inserting user data with a hashed password into my database?

**Outcome:** Generated a real BCrypt hash for the plaintext password `password123` and provided an `INSERT` script for `departments` and `users` using that hash. Explained why a real BCrypt hash (not a fabricated string) is required for `BCryptPasswordEncoder.matches()` to succeed.
 
---

## Prompt 3

> Im using postman to try and get my token, its giving me "403 forbidden" so the authentication is not working for me. heres my http request "GET http://localhost:8081/api/auth/login" Body: [deleted for password security]


**Outcome:** Identified that `/api/auth/login` is mapped with `@PostMapping`, but the request was sent as `GET`. Instructed to change the method to `POST` and confirm the body is raw/JSON.
 
---

## Prompt 4

> ok that worked. I sent a new get request (getAllUsers) after that using the token, and it worked. I tried to make a new request with getUsersByID using the same token but Im getting 403 again. do I need a new token for every request, or is it something else?

**Outcome:** Clarified tokens are reusable for their full lifetime (1 hour). Suggested the new Postman tab may not carry over the `Authorization` header, and flagged that `getUserById(int id)` in `UserController` was missing `@PathVariable`.
 
---

## Prompt 5

> good, I added @PathVariable and it worked. lets implement the password encoder, the password is currently saved as plaintext in the database

**Outcome:** Wired `PasswordEncoder` into `UserServiceImpl` via constructor injection, updated `createUser` to call `passwordEncoder.encode(...)`, and updated `UserController` to use `@Valid createUserDto dto`.
 
---

## Prompt 6

> Im back to getting a 403 error. I changed nothing about the post http request that just worked (it added an unhashed password in the db) I restarted the application after I changed the file to include the field declaration

**Outcome:** Suggested the most likely causes were token invalidation via an unstable `JWT_SECRET` across restarts, or token expiration, and recommended logging in again for a fresh token as the fastest diagnostic.
 
---

## Prompt 7

> that was because the username was no longer unique. all I had to do was change the body of the http request. the password is hashed now

**Outcome:** Clarified the actual cause was a `UNIQUE` constraint violation on `username` (a `DataIntegrityViolationException`), not an auth failure — and that it surfaced as a `403` rather than a clear error because `GlobalExceptionHandler` has no handler for that exception type. Offered to add a `409 Conflict` handler for it.
 
---

## Open follow-ups
- [ ] Add a `GlobalExceptionHandler` entry for `DataIntegrityViolationException` → return `409 Conflict` with a clear "username already taken" message.
- [*] Confirm no stale plaintext-password user rows remain from pre-encoder testing.