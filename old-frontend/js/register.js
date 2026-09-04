document.querySelector("#registerForm").addEventListener("submit", async (event) => {
    event.preventDefault();

    const user = {
        username: document.querySelector("#username").value,
        password: document.querySelector("#password").value,
        firstName: document.querySelector("#firstName").value,
        lastName: document.querySelector("#lastName").value,
        departmentId: Number(document.querySelector("#department").value),
    };

    const response = await fetch("http://localhost:8081/api/users", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(user)
    });

    if (response.ok) {
        window.location.href = "index.html";
    } else {
        const error = await response.text();

        const errSelected = document.querySelector("#error"); 
        errSelected.textContent = error || "Registration failed"; 
    }
});

async function loadDepartments() {

    const departmentSelect = document.getElementById("department");

    const response = await fetch("http://localhost:8081/api/departments");

    if (!response.ok) {
        console.error("Failed to load department");
        return;
    }

    const departments = await response.json(); 

    departments.forEach(department => {
        const option = document.createElement("option"); 
        option.value = department.departmentId;
        option.textContent = `${department.departmentName} (ID: ${department.departmentId})`; 

        departmentSelect.appendChild(option);
    })
}

loadDepartments();