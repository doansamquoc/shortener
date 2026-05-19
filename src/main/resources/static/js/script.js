console.log("Hello World!");

const BASE_URL = "https://shortener-wz64.onrender.com";

const url = document.getElementById("url");
const btn = document.getElementById("btnSubmit");
const spinner = document.getElementById("btnSpinner");
const icon = document.getElementById("btnIcon");
const urlFeedBack = document.getElementById("urlFeedBack");

const copiedToast = document.getElementById("copiedToast");

document.getElementById("shortenForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    btn.disabled = true;
    spinner.classList.remove("d-none");
    icon.classList.add("d-none");

    url.classList.remove("is-invalid");

    const value = url.value.trim();

    try {
        const res = await shortenUrl(value);
        const json = await res.json();

        if (res.ok) {
            const shortenedUrl = BASE_URL + "/" + json.data.shortenedUrl;
            document.getElementById("shortenedUrl").value = shortenedUrl;

            const shortenedRedirect = document.getElementById("shortenedRedirect");
            shortenedRedirect.href = shortenedUrl;
            shortenedRedirect.textContent = shortenedUrl;

            document.getElementById("btnCopy").addEventListener("click", async () => {
                await navigator.clipboard.writeText(shortenedUrl);
                const toastBootstrap = bootstrap.Toast.getOrCreateInstance(copiedToast);
                toastBootstrap.show();
            });

            const mySuccessModal = new bootstrap.Modal(document.getElementById("successModal"));
            mySuccessModal.show();
        } else {
            url.classList.add("is-invalid");
            urlFeedBack.textContent = json.error.message;
        }
    } catch (err) {
        console.error(err);
        url.classList.add("is-invalid");
        urlFeedBack.textContent = err.message;
    } finally {
        btn.disabled = false;
        spinner.classList.add("d-none");
        icon.classList.remove("d-none");
    }
});

async function shortenUrl(url) {
    return await fetch("/api/v1/urls", {
        method: "POST",
        headers: new Headers({"Content-Type": "application/json"}),
        body: JSON.stringify({actualUrl: url}),
    });
}
