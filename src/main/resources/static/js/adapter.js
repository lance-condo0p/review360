function questionRow(question, marks) {
    return tr([
        td(label(question)),
        td(select(question.code, marks)),
    ]);
}

function label(question) {
    const node = document.createElement("label");
    const att = document.createAttribute("for");
    const textAttr = document.createTextNode(question.description);

    att.value = question.code;
    node.setAttributeNode(att)
    node.appendChild(textAttr);

    return node;
}

function select(questionCode, marks) {
    const node = document.createElement("select");

    const attId = document.createAttribute("id");
    const attName = document.createAttribute("name");
    const attRequired = document.createAttribute("required");
    const attClass = document.createAttribute("class");
    attId.value = questionCode;
    attName.value = questionCode;
    attClass.value = "minimal";

    node.setAttributeNode(attId);
    node.setAttributeNode(attName);
    node.setAttributeNode(attRequired);
    node.setAttributeNode(attClass);

    addEmptyOption(node);
    marks.forEach(mark => addOption(node, mark));

    return node;
}

function addOption(parent, mark) {
    const node = document.createElement("option");
    const att = document.createAttribute("value");
    const textAttr = document.createTextNode(mark.second);

    att.value = mark.first;
    node.setAttributeNode(att)
    node.appendChild(textAttr);

    parent.appendChild(node);
}

function tr(children) {
    const node = document.createElement("tr");
    children.forEach(child => node.appendChild(child));
    return node;
}

function td(children) {
    const node = document.createElement("td");
    node.appendChild(children);
    return node;
}

function addEmptyOption(selectId) {
    const opt = document.createElement("option");
    const att = document.createAttribute("value");
    const att2 = document.createAttribute("selected");

    const textAttr = document.createTextNode("Please make your choice...");
    att.value = "";
    att2.value = "selected";
    opt.setAttributeNode(att);
    opt.setAttributeNode(att2);

    opt.appendChild(textAttr);
    selectId.appendChild(opt);
}

async function loadQuestions(marks) {
    let response = await fetch(
        "/api/v1/forms/questions",
        {
            headers: { 'Accept': 'application/json' }
        }
    );
    let questionsList = await response.json();

    const questionsSelectId = document.getElementById("questionsTableBody");

    questionsList.forEach(question => {

        let qRow = questionRow(question, marks);

        questionsSelectId.appendChild(qRow);
    });
}

async function loadEmployees() {
    let response = await fetch(
        "/api/v1/forms/employees",
        {
            headers: { 'Accept': 'application/json' }
        }
    );
    let employeesList = await response.json();

    const employeesSelectId = document.getElementById("employee");

    addEmptyOption(employeesSelectId);

    employeesList.forEach(employee => {
        const opt = document.createElement("option");
        const att = document.createAttribute("value");

        const textAttr = document.createTextNode(employee.name + ' (' + employee.email + ')');
        att.value = employee.email;
        opt.setAttributeNode(att);

        opt.appendChild(textAttr);
        employeesSelectId.appendChild(opt);
    });
}

async function loadMarks() {
    let response = await fetch(
        "/api/v1/forms/marks",
        {
            headers: { 'Accept': 'application/json' }
        }
    );
    const marks = await response.json();
    return marks;
}

function buildJsonFormData(form) {
//    console.log(form);
    const jsonFormData = { };
    let jsonRowData = { };
    let filledForm = new FormData(form);
    jsonFormData["reviewer_email"] = filledForm.get("reviewerEmail");
    filledForm.delete("reviewerEmail");
    jsonFormData["employee_email"] = filledForm.get("employeeEmail");
    filledForm.delete("employeeEmail");
    let jsonArray = [];
    for(const pair of filledForm) {
        jsonRowData["skill"] = pair[0];
        jsonRowData["points"] = pair[1];
        jsonArray.push(jsonRowData);
        jsonRowData = { };
    }
    jsonFormData["answers"] = jsonArray;
//    console.log(jsonFormData);
    return jsonFormData;
}

function isValidForm() {
    const answers = document.getElementsByTagName("select");
    for (let i = 0; i < answers.length; i++) {
      if(answers[i].options[answers[i].selectedIndex].value == "") {
        return false;
      };
    }
    return true;
}

function resetForm() {
    const optionsToReset = document.querySelectorAll("option");
    for (let i = 0; i < optionsToReset.length; i++) {
        optionsToReset[i].selected = optionsToReset[i].defaultSelected;
    }
}

function logout() {
    window.location.href = "/";
}

window.onload = function() {
    (async() => {
        loadEmployees();
        loadMarks().then(marks => { loadQuestions(marks); });
    })();

    let reviewForm = document.getElementById("reviewForm");
    reviewForm.addEventListener("submit", (e) => {
        e.preventDefault();

        if(!isValidForm()) {
            alert("Please answer all the questions!");
            return;
        }

        const jsonFormData = buildJsonFormData(e.target);

        (async() => {
            const response = await fetch("/api/v1/forms", {
              method: "POST",
              body: JSON.stringify(jsonFormData),
              headers: {
                "Content-type": "application/json; charset=UTF-8"
              }
            });
            const rspMessage = await response.json();
            if(response.status == 201) {
                alert(rspMessage.message);
                resetForm();
            } else {
                alert(rspMessage.message);
            }
        })();
     });
};