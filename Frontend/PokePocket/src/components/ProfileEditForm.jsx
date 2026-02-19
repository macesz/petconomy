
function ProfileEditForm({ handleChange, handleSubmit, formData, validation, navigate, emailReadOnly }) {
    return (
        <form className="p-5" onSubmit={handleSubmit}>
            <div className="flex flex-col md:flex-row gap-6">
                {/* Left Column */}
                <div className="flex-1">
                    {/* Username */}
                    <fieldset className='fieldset'>
                        <div className='flex-row'>
                            <label htmlFor="username" className="font-medium text-[1rem] flex">
                                Username
                            </label>
                            <input
                                type="text"
                                className={`input ${
                                    validation.username
                                        ? validation.username === "Correct"
                                            ? "input-success"
                                            : "input-error"
                                        : ""
                                }`}
                                id="username"
                                value={formData.username}
                                onChange={handleChange}
                                required
                            />
                            {validation.username && (
                                <small
                                    className={`text-[14px] flex  
                        ${validation.username === "Correct"
                                        ? "text-success"
                                        : "text-error"}  
                        `
                                    }
                                >
                                    {validation.username}
                                </small>
                            )}
                        </div>
                    </fieldset>

                    {/* Target amount */}
                    <div className="form-control">
                        <label htmlFor="newTargetAmount" className="label">
                            <span className="label-text font-semibold">Target amount to save this month</span>
                        </label>
                        <input
                            type="text"
                            id="newTargetAmount"
                            className={`input input-bordered w-full ${
                                validation.newTargetAmount === "Correct"
                                    ? "input-success"
                                    : validation.newTargetAmount
                                        ? "input-error"
                                        : ""
                            }`}
                            value={formData.newTargetAmount}
                            onChange={handleChange}
                            required
                        />
                        {validation.newTargetAmount && validation.newTargetAmount !== "Correct" && (
                            <small className="text-error text-[14px]">
                                {validation.newTargetAmount}
                            </small>
                        )}
                    </div>

                    {/* Email */}
                    <fieldset className='fieldset'>
                        <label htmlFor="email" className="font-medium text-[1rem]">
                            Email {emailReadOnly && "(Read-only)"}
                        </label>
                        <input
                            type="email"
                            className={`input ${
                                validation.email
                                    ? validation.email === "Correct"
                                        ? "input-success"
                                        : "input-error"
                                    : ""
                            } ${emailReadOnly ? "bg-gray-100" : ""}`}
                            id="email"
                            value={formData.email}
                            onChange={handleChange}
                            readOnly={emailReadOnly}
                            disabled={emailReadOnly}
                            required
                        />
                        {validation.email && (
                            <small
                                className={`text-[14px]  
                      ${validation.email === "Correct"
                                    ? "text-success"
                                    : "text-error"}  
                      `
                                }
                            >
                                {validation.email}
                            </small>
                        )}
                    </fieldset>
                </div>

                {/* Right Column */}
                <div className="flex-1">
                    {/* Password Change Section */}
                    <div className="md:border-l md:pl-6 md:border-gray-200">
                        <h3 className="font-semibold text-lg mb-3">Change Password (Optional)</h3>

                        {/* Current Password */}
                        <fieldset className='fieldset'>
                            <label htmlFor="password" className="font-medium text-[1rem]">
                                Current Password
                            </label>
                            <input
                                type="password"
                                className="input"
                                id="password"
                                value={formData.password}
                                onChange={handleChange}
                            />
                        </fieldset>

                        {/* New Password */}
                        <fieldset className='fieldset'>
                            <label htmlFor="newPassword" className="font-medium text-[1rem]">
                                New Password
                            </label>
                            <input
                                type="password"
                                className={`input ${
                                    validation.newPassword
                                        ? validation.newPassword === "Correct"
                                            ? "input-success"
                                            : "input-error"
                                        : ""
                                }`}
                                id="newPassword"
                                value={formData.newPassword}
                                onChange={handleChange}
                            />
                            {validation.newPassword && (
                                <small
                                    className={`text-[14px]  
                        ${validation.newPassword === "Correct"
                                        ? "text-success"
                                        : "text-error"}  
                        `
                                    }
                                >
                                    {validation.newPassword}
                                </small>
                            )}
                        </fieldset>

                        {/* Confirm New Password */}
                        <fieldset className='fieldset'>
                            <label htmlFor="confirmPassword" className="font-medium text-[1rem]">
                                Confirm New Password
                            </label>
                            <input
                                type="password"
                                className={`input ${
                                    validation.confirmPassword
                                        ? validation.confirmPassword === "Correct"
                                            ? "input-success"
                                            : "input-error"
                                        : ""
                                }`}
                                id="confirmPassword"
                                value={formData.confirmPassword}
                                onChange={handleChange}
                            />
                            {validation.confirmPassword && (
                                <small
                                    className={`text-[14px]  
                        ${validation.confirmPassword === "Correct"
                                        ? "text-success"
                                        : "text-error"}  
                        `
                                    }
                                >
                                    {validation.confirmPassword}
                                </small>
                            )}
                        </fieldset>
                    </div>
                </div>
            </div>

            <div className="flex justify-between mt-6">
                <button
                    type="button"
                    className="btn btn-outline"
                    onClick={() => navigate("/main")}
                >
                    Cancel
                </button>
                <button
                    className="btn btn-primary rounded-full"
                    type="submit"
                >
                    Save Changes
                </button>
            </div>
        </form>
    );
}

export default ProfileEditForm;
