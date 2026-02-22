import React from 'react'

function LoginForm({handleChange, handleSubmit, formData, switchForm}) {
  return (
    <>
    <div className="text-center">
      <div>
        <h2 className='font-bold text-2xl'>Login</h2>
      </div>
    </div>
    <div className='flex items-center justify-center'>
      <div>
        <form className="p-5" onSubmit={handleSubmit}>
          <fieldset className='fieldset'>
            <label htmlFor="email" className="font-medium text-[1rem]">
              Email
            </label>
            <input
              type="email"
              className="input"
              id="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </fieldset>
          <div>
            <label htmlFor="password" className="font-medium text-[1rem]">
              Password
            </label>
            <input
              type="password"
              className="input"
              id="password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>
          <div className="text-center mt-5">
            <button className="btn btn-primary rounded-full" type="submit">
              Login
            </button>
          </div>
        </form>
        <div className='text-center'>
          <a
            className="text-blue-600"
            href="#"
            onClick={switchForm}
          >
            Don't have an account? Register
          </a>
        </div>
      </div>
    </div>
  </>
  )
}

export default LoginForm