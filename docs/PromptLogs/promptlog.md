*Prompt 1*  
**Initial Prompt**:
I am building the JavaScript frontend for an Employee Reimbursement System that communicates with a Spring Boot REST API. Users authenticate using JWT tokens, and the application supports both EMPLOYEE and MANAGER roles.
Specifications and user stories: frontend needs to allow users to log in, view reimbursements, create reimbursements, view reimbursement history, and log out. Managers should additionally be able to approve or deny pending reimbursements.  

You are a senior system designer of JavaScript that focuses on extendibility of design, as well as following every requirement to the very end. Break the frontend implementation into a step-by-step plan. Organize the JavaScript so API requests, DOM rendering, and event handling are separated. Order the tasks based on dependencies so each part can be implemented and tested before moving to the next.  

**AI Response**:
Create api.js to handle communication with the Spring Boot backend. Add functions for authentication headers, retrieving the current user, retrieving reimbursements, creating reimbursements, resolving reimbursements, and logging out.  

Create reimbursements.js to handle displaying reimbursement information on the page. Create a function that accepts reimbursement data and generates the appropriate HTML for each reimbursement.  

Create dashboard.js to control the main page. Retrieve the authenticated user first so the application knows whether the user is an EMPLOYEE or MANAGER. Then retrieve reimbursements and pass them to the rendering function.
Add manager-specific functionality. Pending reimbursements should display Approve and Deny buttons when the current user is a manager. Clicking these buttons should call the appropriate backend endpoint and refresh the reimbursement list.  

Add a form for creating reimbursements. Read the user's form values, construct the request, send it to the API, and refresh the page data after a successful request.  

Add reimbursement history and error handling. Failed API requests should display an appropriate error rather than attempting to render invalid data.  

**Human Audit**:
Reviewing the plan, the overall separation of the frontend into multiple JavaScript files was useful, but some changes I wanted to make based on techniques covered during training.  

After conversation, the Ai initially suggested generating reimbursement cards using template strings and innerHTML. I would prefer to use DOM manipulation techniques covered in class, so I asked the AI to revise the plan to use document.getElementById(), document.createElement(), textContent, and appendChild() for creating and inserting elements.  

I also wanted a clearer separation between API communication and event handling. The API module should not decide what happens when a user clicks a button. Its responsibility should ONLY be sending requests to /api endpoints and returning the results.  

*Re-Prompt*:
Revise the plan so reimbursement elements are created primarily using document.getElementById(), document.createElement(), textContent, and appendChild() instead of generating the interface with innerHTML.
also separate the responsibilities of the JavaScript files. api.js shosuld only handle communication with /api  endpoints. reimbursements.js should handle rendering reimbursement data. dashboard.js should contain page logic and event handlers…  

**Revised AI Plan**:
The revised plan divided the frontend into three main layers.
api.js handles communication with the backend. Functions such as getCurrentUser(), getReimbursements(), createReimbursement(), and resolveReimbursement() send requests to the appropriate /api endpoints, it also handles adding the JWT Bearer token to authenticated requests.
reimbursements.js handles DOM rendering. It receives reimbursement data and creates the necessary DOM elements using createElement(), textContent, and appendChild(). It can also create Approve and Deny buttons when the current user is a manager and the reimbursement is pending.
dashboard.js coordinates the page. It retrieves the current user, loads reimbursement data, passes that data to the rendering functions, and registers event listeners for actions such as logout, history, approval, denial, and reimbursement creation.  

**Result**:
AI Task Planning gave me an initial dependency-based structure instead of implementing the entire frontend at once. I then audited the generated plan and changed portions that did not match the techniques or architecture I wanted to use.  

The final approach separated backend communication, DOM rendering, and event handling into different modules. This allowed each portion of the frontend to be implemented and tested individually before connecting the complete application.


*Prompt 2* 

**Prompt**:
Act as a senior Spring Data JPA developer with a key eye for debugging and reasoning through problems. I have these relationships:
Reimbursement -> User author
User -> Department department
Department -> Integer departmentId
I want a repository method that filters reimbursements by both status and the author's department ID.
I wrote:
List<Reimbursement> findByStatusAndDepartmentId(
        ReimbursementStatus status,
        Integer departmentId);
Spring fails to create the repository query at startup.  

Please identify the naming bug using Spring Data's property traversal rules, explain how Spring interprets derived-query method names, and give me the correct method signature. Keep the explanation focused on why the method name must follow the actual entity relationship path.  

**AI Response**:
The bug is that Reimbursement does not have a direct field named departmentId.
Spring Data parses derived query names by following Java entity properties from left to right. Your object path is:
Reimbursement
    ↓ author
User
    ↓ department
Department
    ↓ departmentId
Integer
So the repository method name must describe that full path:
```
List<Reimbursement> findByStatusAndAuthorDepartmentDepartmentId(
    ReimbursementStatus status,
    Integer departmentId);
```

Spring interprets the important section as:
Author → Department → DepartmentId
Your original method:
findByStatusAndDepartmentId(...)
effectively asks Spring to find:
reimbursement.getDepartmentId()
but that property does not exist.   

The useful debugging rule is: when a derived query fails, start from the repository's entity and trace the exact Java field names needed to reach the property you want to filter on.  

**Follow-up**:
So if I understand correctly, I should start at the repository entity and follow its Java fields until I reach the property I want to query. For example, if Reimbursement has a User author and User has an Integer id, would findByAuthorId(Integer authorId) work because Spring traverses Reimbursement , author, id? I would only need something like findByAuthorUserId if the property on User were actually named userId, correct?  

**AI Response**:
Correct. Spring Data builds the property path using the actual Java field names. If Reimbursement has an author field and User has an id field, findByAuthorId(...) resolves to Reimbursement.author.id. If the User field were instead named userId, then findByAuthorUserId(...) would represent Reimbursement.author.userId.
