import React, { useEffect, useState } from "react";
import "./Registration.css";
import { fetchData } from "../../utils";
import LoginForm from "../../components/LoginForm";
import RegistrationForm from "../../components/RegistrationForm";
import { useNavigate } from "react-router-dom";

function Registration() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
  });

  const [validation, setValidation] = useState({
    username: "",
    email: "",
    password: "",
  });

  const [isTermsAccepted, setIsTermsAccepted] = useState(false);
  const [isRegistering, setIsRegistering] = useState(false);
  const [isFadingOut, setIsFadingOut] = useState(false);

  useEffect(() => {
    localStorage.getItem("pokePocketJwt") == "null" || navigate("/main");
  }, []);

  const validateUsername = (value) => {
    if (value.length < 4) {
      return "Username must be at least 4 characters";
    }
    return "Correct";
  };

  const validateEmail = (value) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(value)) {
      return "Invalid email format";
    }
    return "Correct";
  };

  const validatePassword = (value) => {
    const hasUpperCase = /[A-Z]/.test(value);
    const hasLowerCase = /[a-z]/.test(value);
    const hasNumber = /\d/.test(value);
    const isLongEnough = value.length >= 8;

    if (!hasUpperCase || !hasLowerCase || !hasNumber || !isLongEnough) {
      return "Password must be at least 8 characters, include upper/lowercase and a number";
    }
    return "Correct";
  };

  const handleChange = (e) => {
    const { id, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [id]: value,
    }));

    let validationMessage = "";
    if (id === "username") validationMessage = validateUsername(value);
    if (id === "email") validationMessage = validateEmail(value);
    if (id === "password") validationMessage = validatePassword(value);

    setValidation((prev) => ({
      ...prev,
      [id]: validationMessage,
    }));
  };

  const handleCheckboxChange = (e) => {
    setIsTermsAccepted(e.target.checked);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (
      isRegistering &&
      (validation.username !== "Correct" ||
        validation.email !== "Correct" ||
        validation.password !== "Correct")
    ) {
      alert("Please correct the errors before submitting.");
      return;
    }

    console.log("Submitted data:", formData);
    const responseBody = await fetchData(isRegistering ? "user/register" : "user/login", "POST", formData, null, !isRegistering);
    if (!isRegistering) {
      if (responseBody.status === 401) {
        setFormData({
          username: "",
          email: "",
          password: "",
        });
      } else {
        localStorage.setItem("pokePocketJwt", responseBody.jwt);
        localStorage.setItem("username", responseBody.userName);
        navigate("/main");
      }
    } else {
      if (responseBody.status === 409) {
        setFormData({
          ...formData,
          email: ""
        });
        setValidation({
          ...validation,
          email: "User with this email is already exist!"
        })
      } else {
        const responseBody = await fetchData("user/login", "POST", formData);
        localStorage.setItem("pokePocketJwt", responseBody.jwt);
        navigate("/main");
      }
    }

  };

  const switchForm = () => {
    setIsFadingOut(true);
    setTimeout(() => {
      setIsRegistering((prev) => !prev);
      setIsFadingOut(false);
    }, 500);
  };

  return (
    <>
    <div className="flex justify-center items-center w-full h-screen">
      <div className="bg-white m-auto whitebox flex justify-between h-[600px] basis-8/12">
        <div
          className={`basis-8/12 my-auto text-dark form-container ${isFadingOut ? "fade-out" : "fade-in"}`}>
          {isRegistering ? (
            <>
              <RegistrationForm
                handleChange={handleChange}
                handleSubmit={handleSubmit}
                formData={formData}
                switchForm={switchForm}
                validation= {validation}
                handleCheckboxChange={handleCheckboxChange}
                isTermsAccepted={isTermsAccepted}
              ></RegistrationForm>
            </>
          ) : (
            <>
              <LoginForm
                handleChange={handleChange}
                handleSubmit={handleSubmit}
                formData={formData}
                switchForm={switchForm}
              ></LoginForm>
            </>
          )}
        </div>
        <div className="justify-items-end basis-5/12">
          <img
            className=" regImgDiv me-0"
            src={
              new URL("../../assets/registrationImg.jpg", import.meta.url).href
            }
            alt="regImg"
            style={{ height: "100%" }}
          />
        </div>
      </div>
      </div>
      
    </>
  );
}

export default Registration;
