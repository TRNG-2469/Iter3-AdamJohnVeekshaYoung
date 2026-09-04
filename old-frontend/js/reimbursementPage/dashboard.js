import {
    getCurrentUser,
    getReimbursements,
    logout,
    resolveReimbursement,
    deleteReimbursement,
    createReimbursement,
    editReimbursement,
    getDepartments
} from "./api.js";

import {
    renderReimbursements
} from "./reimbursements.js";

const logoutBtn = document.getElementById("logout");
const historyBtn = document.getElementById("historyButton");
const filterForm = document.getElementById("filterForm");
const errorElement = document.getElementById("error");
const reimbursementList = document.getElementById("reimbursements");
const createBtn = document.getElementById("createReimbursementButton");
const createForm = document.getElementById("createReimbursementForm");
const cancelCreateBtn = document.getElementById("cancelCreateReimbursement");
const viewOwnReimbursementsBtn = document.getElementById("ownReimbursementsButton");

let currentUser;


async function handleResolve(id, status) {
    const response = await resolveReimbursement(id, status);

    if (!response) {
        return;
    }

    if (!response.ok) {
        showError("Could not resolve reimbursement");
        return;
    }

    await loadReimbursements();
}

async function handleDelete(id) {
    const response = await deleteReimbursement(id);

    if (!response) {
        return;
    }

    if (!response.ok) {
        showError("Could not delete reimbursement");
        return;
    }

    await loadReimbursements();
}
async function handleCreateReimbursement(event) {
    event.preventDefault();

    const data = {
        amount: Number(document.getElementById("amount").value),
        type: document.getElementById("type").value,
        description: document.getElementById("description").value
    };
    const response = await createReimbursement(data);

    if (!response) { //request failed auth
        return;
    }
    if (!response.ok) { //unknown error
        const error = await response.json();
        showError(error.error || "Could not create reimbursement");
        return;
    }

    createForm.reset();
    createForm.hidden = true;
    await loadReimbursements();

}

async function handleEdit(button) {
    const reimbursementElement = button.closest(".rcontainer");
    const editContainer =
        reimbursementElement.querySelector(".edit-container");

    const id = button.dataset.id;

    const amountInput =
        editContainer.querySelector(".new-amount");

    const descriptionInput =
        editContainer.querySelector(".new-description");

    const typeSelect =
        editContainer.querySelector(".new-type");

    // Validate the inputs before making the API request
    if (!amountInput.checkValidity()) {
        amountInput.reportValidity();
        return;
    }

    if (!descriptionInput.checkValidity()) {
        descriptionInput.reportValidity();
        return;
    }

    const amount = amountInput.value;
    const description = descriptionInput.value;
    const type = typeSelect.value;

    const response = await editReimbursement(
        id,
        amount,
        description,
        type
    );

    if (!response) {
        return;
    }

    if (!response.ok) {
        showError("Could not save edits");
        return;
    }

    // Update the displayed reimbursement values
    reimbursementElement.querySelector(
        ".reimbursement-amount"
    ).textContent = `$${amount}`;

    reimbursementElement.querySelector(
        ".reimbursement-description"
    ).textContent = description;

    reimbursementElement.querySelector(
        ".reimbursement-type"
    ).textContent = type;

    // Remove the edit form
    editContainer.remove();

    await loadReimbursements();
}

function showEditOptions(button) {
    const reimbursementElement = button.closest(".rcontainer");

    // Don't create another edit form if one already exists
    if (reimbursementElement.querySelector(".edit-container")) {
        return;
    }

    // Get existing values
    const currentAmount =
        reimbursementElement.querySelector(".reimbursement-amount").textContent;
    const currentDescription =
        reimbursementElement.querySelector(".reimbursement-description").textContent;
    const currentType =
        reimbursementElement.querySelector(".reimbursement-type").textContent;

    // Remove "$" from the displayed amount
    const amountValue = currentAmount.replace("$", "");

    const editContainer = document.createElement("div");
    editContainer.classList.add("edit-container");

    // New amount
    const amountLabel = document.createElement("label");
    amountLabel.classList.add("amount-label");
    amountLabel.textContent = "New amount: ";

    const amountInput = document.createElement("input");
    amountInput.type = "number";
    amountInput.classList.add("new-amount");
    amountInput.name = "new-amount";
    amountInput.min = "0.01";
    amountInput.max = "999999";
    amountInput.step = "0.01";
    amountInput.value = amountValue;
    amountInput.required = true;

    amountLabel.appendChild(amountInput);

    // New description
    const descriptionLabel = document.createElement("label");
    descriptionLabel.classList.add("description-label");
    descriptionLabel.textContent = "New description: ";

    const descriptionInput = document.createElement("input");
    descriptionInput.type = "text";
    descriptionInput.classList.add("new-description");
    descriptionInput.name = "new-description";
    descriptionInput.maxLength = 255;
    descriptionInput.value = currentDescription;

    descriptionLabel.appendChild(descriptionInput);

    // New type
    const typeLabel = document.createElement("label");
    typeLabel.classList.add("type-label");
    typeLabel.textContent = "New type: ";

    const typeSelect = document.createElement("select");
    typeSelect.classList.add("new-type");
    typeSelect.name = "new-type";

    const types = [
        "TRAVEL",
        "FOOD",
        "LODGING",
        "MEDICAL",
        "TRANSPORTATION",
        "OTHER"
    ];

    for (const type of types) {
        const option = document.createElement("option");
        option.value = type;
        option.textContent = type;

        if (type === currentType) {
            option.selected = true;
        }

        typeSelect.appendChild(option);
    }

    typeLabel.appendChild(typeSelect);

    const buttonContainer = document.createElement("div");

    // Save button
    const saveButton = document.createElement("button");
    saveButton.type = "button";
    saveButton.textContent = "Save";
    saveButton.classList.add("saveButton");
    buttonContainer.appendChild(saveButton);

    saveButton.addEventListener("click", async () => {
        await handleEdit(button);
    });

    // Cancel button
    const cancelButton = document.createElement("button");
    cancelButton.type = "button";
    cancelButton.textContent = "Cancel";
    cancelButton.classList.add("cancelButton");
    buttonContainer.appendChild(cancelButton);

    cancelButton.addEventListener("click", () => {
        editContainer.remove();
    });

    // Add elements to edit container
    editContainer.append(
        amountLabel,
        descriptionLabel,
        typeLabel,
        buttonContainer
    );

    reimbursementElement.appendChild(editContainer);
}

reimbursementList.addEventListener("click", async (event) => {
    const action = event.target.dataset.action;
    const id = event.target.dataset.id;

    if (action === "approve") {
        await handleResolve(id, "APPROVED");
    }

    if (action === "deny") {
        await handleResolve(id, "DENIED");
    }

    if (action == 'delete') {
        await handleDelete(id);
    }

    if (action === "edit") {
        showEditOptions(event.target);
    }
});

logoutBtn.addEventListener("click", logout);

historyBtn.addEventListener("click", async () => {
    await loadReimbursements("/reimbursements/history");
});

viewOwnReimbursementsBtn.addEventListener("click", async () => {
    await loadReimbursements(`/reimbursements/own`);
})

createBtn.addEventListener("click", () => {
    createForm.hidden = !createForm.hidden;
});

cancelCreateBtn.addEventListener("click", () => {
    createForm.reset();
    createForm.hidden = true;
});

createForm.addEventListener("submit", handleCreateReimbursement);

filterForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const status = document.getElementById("status").value;
    const departmentId = document.getElementById("department").value;

    const params = new URLSearchParams();

    if (status) {
        params.append("status", status);
    }

    if (departmentId) {
        params.append("departmentId", departmentId);
    }

    let endpoint = "/reimbursements";

    if (params.toString()) {
        endpoint += `?${params.toString()}`;
    }

    await loadReimbursements(endpoint);
});

async function loadCurrentUser() {
    const response = await getCurrentUser();

    if (!response) {
        return false;
    }

    if (!response.ok) {
        showError("Could not load current user");
        return false;
    }

    currentUser = await response.json();

    document.getElementById("username").textContent =
        currentUser.username;

    document.getElementById("role").textContent =
        currentUser.role;

    configureDashboard();

    return true;
}


//LOADING FUNCTIONS
async function loadDepartments() {
    const response = await getDepartments();

    if (!response) {
        return;
    }

    if (!response.ok) {
        showError("Could not load departments");
        return;
    }

    const departments = await response.json();

    const departmentSelect = document.getElementById("department");

    for (const department of departments) {
        const option = document.createElement("option");

        option.value = department.departmentId;
        option.textContent = `${department.departmentName} (ID: ${department.departmentId})`;

        departmentSelect.appendChild(option);
    }
}

async function loadReimbursements(endpoint = "/reimbursements") {
    const response = await getReimbursements(endpoint);

    if (!response) {
        return;
    }

    if (!response.ok) {
        showError("Could not load reimbursements");
        return;
    }

    const reimbursements = await response.json();

    renderReimbursements(
        reimbursements,
        currentUser
    );
}

function configureDashboard() {
    const isManager = currentUser.role === "MANAGER";

    historyBtn.hidden = !isManager;
    viewOwnReimbursementsBtn.hidden = !isManager;

    document.getElementById("departmentFilter").hidden =
        !isManager;
}

function showError(message) {
    errorElement.textContent = message;
}

async function initializePage() {
    const loggedIn = await loadCurrentUser();

    if (!loggedIn) {
        return;
    }

    //changed hardcoded departments to ones we have seen
    if (currentUser.role === "MANAGER") {
        await loadDepartments();
    }

    await loadReimbursements();
}

initializePage();