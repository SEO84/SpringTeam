// src/main/resources/static/js/create.js

// 추가 펫 선택 필드를 동적으로 생성하는 함수
function addPetSelection() {
    const container = document.getElementById('additionalPetsContainer');

    // 이미 선택된 펫들을 가져와 중복 선택 방지
    const selectedPetIds = Array.from(document.querySelectorAll("select[name='additionalPetIds']")).map(select => select.value);
    const availablePets = userPets.filter(pet => !selectedPetIds.includes(pet.petId.toString()));

    if (availablePets.length === 0) {
        alert("더 이상 선택할 수 있는 반려동물이 없습니다.");
        return;
    }

    // 새로운 펫 선택 필드 생성
    const petDiv = document.createElement('div');
    petDiv.className = 'form-group d-flex align-items-center';

    const select = document.createElement('select');
    select.name = 'additionalPetIds';
    select.className = 'form-control mr-2';
    select.required = true;

    const defaultOption = document.createElement('option');
    defaultOption.value = '';
    defaultOption.textContent = '-- 반려동물 선택 --';
    defaultOption.disabled = true; // 선택 불가능하게 설정
    defaultOption.selected = true; // 기본적으로 선택된 상태로 설정
    select.appendChild(defaultOption);

    availablePets.forEach(pet => {
        const option = document.createElement('option');
        option.value = pet.petId;
        option.textContent = pet.name;
        select.appendChild(option);
    });

    const removeBtn = document.createElement('button');
    removeBtn.type = 'button';
    removeBtn.textContent = '삭제';
    removeBtn.className = 'btn btn-danger';
    removeBtn.onclick = function () {
        container.removeChild(petDiv);
    };

    petDiv.appendChild(select);
    petDiv.appendChild(removeBtn);
    container.appendChild(petDiv);
}
