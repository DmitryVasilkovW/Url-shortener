document.addEventListener("DOMContentLoaded", () => {
    const vipButton = document.getElementById("vip-link");
    const regularButton = document.getElementById("regular-link");
    const modal = document.getElementById("modal");
    const closeButton = document.getElementById("close-btn");
    const submitButton = document.getElementById("submit-btn");

    const openModal = () => {
        modal?.classList.remove("hidden");
    };

    const closeModal = () => {
        modal?.classList.add("hidden");
    };

    vipButton?.addEventListener("click", openModal);
    regularButton?.addEventListener("click", openModal);
    closeButton?.addEventListener("click", closeModal);

    submitButton?.addEventListener("click", () => {
        const input = modal?.querySelector("input");
        if (input?.value) {
            alert(`Вы ввели: ${input.value}`);
            input.value = "";
            closeModal();
        } else {
            alert("Введите URL!");
        }
    });
});
