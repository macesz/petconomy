
import React, {useState, useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {fetchData} from "../../utils.js";
import "./ProfileEdit.css";
import ProfileEditForm from "../../components/ProfileEditForm.jsx";

function ProfileEdit() {
    const navigate = useNavigate();
    const [jwt, setJwt] = useState(localStorage.getItem("pokePocketJwt"));
    const [isLoading, setIsLoading] = useState(true);

    const [formData, setFormData] = useState({
        username: "",
        email: "",
        password: "",
        newPassword: "",
        confirmPassword: "",
        newTargetAmount: "0",
    });

    const [validation, setValidation] = useState({
        username: "",
        email: "",
        newPassword: "",
        confirmPassword: "",
        newTargetAmount: "",
    });

    useEffect(() => {
        if (jwt === "null") {
            navigate("/");
            return;
        }

        const fetchUserProfile = async () => {
            try {
                const userData = await fetchData("user/profile", "GET", null, jwt);
                setFormData(prevData => ({
                    ...prevData,
                    username: userData.username || "",
                    email: userData.email || "",
                    newTargetAmount: userData.targetAmount?.toString() || "0",
                }));
                setIsLoading(false);
            } catch (error) {
                console.error("Error fetching profile:", error);
                setIsLoading(false);
            }
        };

        fetchUserProfile();
    }, [jwt, navigate]);

    const validateUsername = (value) => {
        if (value.length < 4) {
            return "Username must be at least 4 characters";
        }
        return "Correct";
    };

    const validateNewTargetAmount = (value) => {
        if (!/^\d{1,}$/.test(value)) {
            return "Amount must be a positive number";
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
        if (!value) return ""; // Empty is valid for unchanged password

        const hasUpperCase = /[A-Z]/.test(value);
        const hasLowerCase = /[a-z]/.test(value);
        const hasNumber = /\d/.test(value);
        const isLongEnough = value.length >= 8;

        if (!hasUpperCase || !hasLowerCase || !hasNumber || !isLongEnough) {
            return "Password must be at least 8 characters, include upper/lowercase and a number";
        }
        return "Correct";
    };

    const validateConfirmPassword = (value) => {
        if (formData.newPassword && value !== formData.newPassword) {
            return "Passwords do not match";
        }
        return formData.newPassword ? "Correct" : "";
    };

    const handleChange = (e) => {
        const {id, value} = e.target;

        if (id === "email") return; // Skip changes if email is read-only

        setFormData(prev => ({
            ...prev,
            [id]: value,
        }));

        let validationMessage = "";
        if (id === "username") validationMessage = validateUsername(value);
        if (id === "email") validationMessage = validateEmail(value);
        if (id === "newPassword") {
            validationMessage = validatePassword(value);
            // Also validate confirm password when password changes
            const confirmValidation = validateConfirmPassword(formData.confirmPassword);
            setValidation(prev => ({
                ...prev,
                confirmPassword: confirmValidation,
            }));
        }
        if (id === "confirmPassword") validationMessage = validateConfirmPassword(value);
        if (id === "newTargetAmount") validationMessage = validateNewTargetAmount(value);

        setValidation(prev => ({
            ...prev,
            [id]: validationMessage,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Validate form before submission
        if (
            (validation.username && validation.username !== "Correct") ||
            (validation.email && validation.email !== "Correct") ||
            (validation.newPassword && validation.newPassword !== "Correct") ||
            (validation.confirmPassword && validation.confirmPassword !== "Correct")||
            (validation.newTargetAmount && validation.newTargetAmount !== "Correct")
        ) {
            alert("Please correct the errors before submitting.");
            return;
        }

        // Match the DTO structure expected by the backend
        const updateData = {
            username: formData.username,
            email: formData.email, // Include email even if it's not changed
        };

        if (formData.newPassword) {
            updateData.currentPassword = formData.password;
            updateData.newPassword = formData.newPassword;
        }

        if(formData.newTargetAmount) {
            updateData.newTargetAmount = parseInt(formData.newTargetAmount, 10);
        }

        try {
            console.log("Sending update data:", updateData);

            // Your fetchData utility function needs to handle 204/200 with empty body correctly
            const response = await fetch("/api/user/profile/update", {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${jwt}`
                },
                body: JSON.stringify(updateData)
            });

            if (response.ok) {
                // Success is indicated by HTTP 200/204, not by response body
                alert("Profile updated successfully!");
                navigate("/main");
            } else {
                // Try to parse error message from response
                try {
                    const errorText = await response.text();
                    alert(errorText || "Failed to update profile");
                } catch (err) {
                    alert("Failed to update profile");
                }
            }
        } catch (error) {
            console.error("Error updating profile:", error);
            alert("An error occurred while updating your profile.");
        }
    };

    if (isLoading) {
        return (
            <div className="flex justify-center items-center h-[80vh]">
                <span className="loading loading-spinner loading-lg text-pokeball"></span>
            </div>
        );
    }

    return (
        <div className="bg-white mx-auto m-5 rounded-[15px] shadow-md p-5 w-full max-w-4xl">
            <h2 className="text-3xl font-bold text-center mb-6 text-pokeball">
                Edit Profile
            </h2>
            <div className="form-container fade-in">
                <ProfileEditForm
                    handleChange={handleChange}
                    handleSubmit={handleSubmit}
                    formData={formData}
                    validation={validation}
                    navigate={navigate}
                    emailReadOnly={true}
                />
            </div>
        </div>
    );
}

export default ProfileEdit;
