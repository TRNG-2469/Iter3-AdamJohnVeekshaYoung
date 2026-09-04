export function renderReimbursements(reimbursements, currentUser) {
    const list = document.getElementById("reimbursements");

    list.innerHTML = "";

    for (const reimbursement of reimbursements) {
        const item = createReimbursementElement(
            reimbursement,
            currentUser
        );

        list.appendChild(item);
    }
}
function createReimbursementElement(r, currentUser) {

    const item = document.createElement("li");
    item.classList.add("rcontainer");

    const id = document.createElement("div");
    id.classList.add("reimbursement-id");
    id.textContent = `RID: ${r.id}`;

    const status = document.createElement("div");
    status.textContent = r.status;

    status.classList.add(
        "reimbursement-status",
        `status-${r.status.toLowerCase()}`
    );

    const amount = document.createElement("div");
    amount.classList.add("reimbursement-amount");
    amount.textContent = Number(r.amount).toLocaleString("en-US", {
    style: "currency",
    currency: "USD"
    });

    const type = document.createElement("div");
    type.classList.add("reimbursement-type");
    type.textContent = r.type;

    const description = document.createElement("div");
    description.classList.add("reimbursement-description");
    description.textContent = r.description;

    item.append(
        id,
        status,
        amount,
        type,
        description,
        createActionButtons(r, currentUser)
    );

    return item;
}

function createActionButton(text, action, id) {
    const button = document.createElement("button");

    button.textContent = text;
    button.dataset.action = action;
    button.dataset.id = id;

    return button;
}

function createActionButtons(r, currentUser) {
    const actions = document.createElement("div");
    actions.classList.add("reimbursement-actions");

    if (
        r.authorId === currentUser.id &&
        r.status === "PENDING"
    ) {
        actions.appendChild(
            createActionButton("Edit", "edit", r.id)
        );

        actions.appendChild(
            createActionButton("Delete", "delete", r.id)
        );
    }

    if (
        currentUser.role === "MANAGER" &&
        r.status === "PENDING" &&
        r.authorId !== currentUser.id

    ) {
        actions.appendChild(
            createActionButton("Approve", "approve", r.id)
        );

        actions.appendChild(
            createActionButton("Deny", "deny", r.id)
        );
    }

    return actions;
}