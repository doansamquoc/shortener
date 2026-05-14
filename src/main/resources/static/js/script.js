console.log("Hello World!");

const shortenForm = document.getElementById("shorten-form");
const actualUrlInput = shortenForm.querySelector("input");
const submitButton = shortenForm.querySelector("button");

let isCreating = false;
async function handleSubmit(e) {
  e.preventDefault();
  const value = actualUrlInput.value.trim();
  if (value === "") return;
  try {
    isCreating = true;
    const res = await fetch("http://localhost:8080/api/v1/urls", {
      method: "POST",
      headers: new Headers({ "Content-Type": "application/json" }),
      body: JSON.stringify({ actualUrl: value }),
    });
    if (res.status === 200 || res.status === 201) {
      const json = await res.json();
      console.log("Kết quả từ API:", json);
    } else {
      console.log("API trả về lỗi với mã trạng thái:", res.status);
    }
  } catch (error) {
    console.log(error.message);
  } finally {
    isCreating = false;
  }
}

shortenForm.addEventListener("submit", handleSubmit);
submitButton.disabled = isCreating;
