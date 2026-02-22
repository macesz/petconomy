import Fieldset from 'daisyui/components/fieldset'
import React from 'react'

function RegistrationForm({handleChange, handleSubmit, formData, switchForm, validation, handleCheckboxChange, isTermsAccepted}) {

  return (
    <>
    <div className="text-center">
      <div>
        <h2 className='font-bold text-2xl'>Registration</h2>
      </div>
    </div>
    <div className='flex items-center justify-center'>
      <div>
        <form className="p-5" onSubmit={handleSubmit}>
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
            </div>
          </fieldset>

          {/* Email */}
          <fieldset className='fieldset'>
            <label htmlFor="email" className="font-medium text-[1rem]">
              Email
            </label>
            <input
              type="email"
              className={`input ${
                validation.email
                  ? validation.email === "Correct"
                    ? "input-success"
                    : "input-error"
                  : ""
              }`}
              id="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
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
          </fieldset>

          {/* Password */}
          <fieldset className='fieldset'>
            <label htmlFor="password" className="font-medium text-[1rem]">
              Password
            </label>
            <input
              type="password"
              className={`input ${
                validation.password
                  ? validation.password === "Correct"
                    ? "input-success"
                    : "input-error"
                  : ""
              }`}
              id="password"
              value={formData.password}
              onChange={handleChange}
              required
            />
            <small
              className={`text-[14px]
                ${validation.password === "Correct"
                  ? "text-success"
                  : "text-error"}
                `
              }
            >
              {validation.password}
            </small>
          </fieldset>

          {/* Terms Checkbox */}
          
            <div className="m-2">
              <input
                className="checkbox-sm"
                type="checkbox"
                id="termsAccepted"
                checked={isTermsAccepted}
                onChange={handleCheckboxChange}
              />
                Agree to terms and conditions
            </div>

          {/* Submit Button */}
          <div className="text-center">
            <button
              className="btn btn-primary rounded-full"
              type="submit"
              disabled={!isTermsAccepted}
            >
              Registration
            </button>
          </div>
        </form>
        <div className="text-center">
          <a
          className='text-blue-600'
            href="#"
            onClick={switchForm}
          >
            Already have an account?
          </a>
        </div>
      </div>
    </div>
  </>
  )
}

export default RegistrationForm