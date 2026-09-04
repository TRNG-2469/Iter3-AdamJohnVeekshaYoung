const API_URL = "http://localhost:8081/api";

function getToken() {
    return localStorage.getItem("token");
}

function authHeaders() {
    return {
        "Authorization": `Bearer ${getToken()}`,
        "Content-Type": "application/json"
    };
}

export function logout() {
    localStorage.removeItem("token");
    window.location.href = "index.html";
}

async function apiFetch(endpoint, options = {}) {
    const response = await fetch(`${API_URL}${endpoint}`, {
        ...options,
        headers: {
            ...authHeaders(),
            ...options.headers
        }
    });

    if (response.status === 401) {
        logout();
        return null;
    }

    return response;
}

export function getCurrentUser() {
    return apiFetch("/users/me");
}

export function getReimbursements(endpoint = "/reimbursements") {
    return apiFetch(endpoint);
}

export function createReimbursement(data) {
    return apiFetch("/reimbursements", {
        method: "POST",
        body: JSON.stringify(data)
    });
}

export function updateReimbursement(id, data) {
    return apiFetch(`/reimbursements/${id}`, {
        method: "PATCH",
        body: JSON.stringify(data)
    });
}

export function deleteReimbursement(id) {
    return apiFetch(`/reimbursements/${id}`, {
        method: "DELETE"
    });
}

export function resolveReimbursement(id, status) {
    return apiFetch(`/reimbursements/${id}/status`, {
        method: "PATCH",
        body: JSON.stringify({
            status: status
        })
    });
}

export function editReimbursement(id, amount, description, type) {
    return apiFetch(`/reimbursements/${id}`, {
        method: "PATCH",
        body: JSON.stringify({
            amount: amount,
            description: description,
            type: type
        })
    });
}

export function getDepartments() {
    return apiFetch(`/departments`);
}