let skillCount = 1;

function addSkillField() {
    const container = document.getElementById('skillsContainer');
    const newSkill = document.createElement('div');
    newSkill.className = 'skill-item card mb-2';
    newSkill.innerHTML = `
        <div class="card-body">
            <div class="row">
                <div class="col-md-4">
                    <select class="form-control" name="requiredSkills[${skillCount}].skillId" required>
                        <option value="">Select Skill</option>
                        ${Array.from(document.querySelector('select[name="requiredSkills[0].skillId"]').options)
        .map(opt => `<option value="${opt.value}">${opt.text}</option>`).join('')}
                    </select>
                </div>
                <div class="col-md-4">
                    <select class="form-select" name="requiredSkills[${skillCount}].skillLevel" required>
                        <option value="">Select Level</option>
                        ${Array.from(document.querySelector('select[name="requiredSkills[0].skillLevel"]').options)
        .map(opt => `<option value="${opt.value}">${opt.text}</option>`).join('')}
                    </select>
                </div>
                <div class="col-md-3">
                    <textarea class="form-control"
                              name="requiredSkills[${skillCount}].moreInfo"
                              placeholder="More info"></textarea>
                </div>
                <div class="col-md-1">
                    <button type="button" class="btn btn-danger btn-sm delete-skill" 
                            onclick="deleteSkill(this)">
                        <i class="bi bi-x-lg"></i>
                    </button>
                </div>
            </div>
        </div>
    `;
    container.appendChild(newSkill);
    skillCount++;
    updateSkillIndexes();
}

function deleteSkill(button) {
    const skillItem = button.closest('.skill-item');
    if (document.querySelectorAll('.skill-item').length > 1) {
        skillItem.remove();
        updateSkillIndexes();
    } else {
        alert('At least one skill is required');
    }
}

function updateSkillIndexes() {
    const skillItems = document.querySelectorAll('.skill-item');
    skillItems.forEach((item, index) => {
        item.querySelectorAll('select, textarea').forEach(element => {
            const name = element.getAttribute('name');
            if (name) {
                const newName = name.replace(/\[\d+\]/, `[${index}]`);
                element.setAttribute('name', newName);
            }
        });
    });
}

// Form validation
function setupFormValidation() {
    document.querySelector('input[name="startDate"]').addEventListener('change', function() {
        document.querySelector('input[name="endDate"]').min = this.value;
    });

    document.querySelector('input[name="salaryMin"]').addEventListener('change', function() {
        document.querySelector('input[name="salaryMax"]').min = this.value;
    });
}