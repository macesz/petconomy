import React from "react";
import PokeBall from "../../assets/pokeball.png";
import "./CustomNavbar.css";
import { useNavigate, Link } from "react-router-dom";

function CustomNavbar() {
  const navigate = useNavigate();

  function onCLickLogout() {
    localStorage.setItem("pokePocketJwt", "null");

    navigate("/");
  }

  function navigateToMain() {
    navigate("/main");
  }

  return (
    <>
      <div className="flex">
        <div className="navbar bg-white text-black shadow-sm flex basis-8/12 mx-auto mt-2 rounded-full align-items-center justify-center">
          <div className="navbar-start align-items-center">
            <div className="h-10">
              <button onClick={navigateToMain} className="cursor-pointer hover:opacity-80 transition-opacity">
                <img src={PokeBall} alt="Home" className="w-10 m-auto" />
              </button>
            </div>
          </div>
          <div className="navbar-end avatar">
            {localStorage.getItem("username")}
            <div className="dropdown dropdown-end m-2">
              <button
                className="w-10 h-10 rounded-full overflow-hidden"
                popoverTarget="popover-1"
                style={{ anchorName: "--anchor-1" } }
              >
                  <img src="https://img.daisyui.com/images/stock/photo-1534528741775-53994a69daeb.webp" />
                
              </button>

              <ul
                className="dropdown menu rounded-box bg-base-100 shadow-sm"
                popover="auto"
                id="popover-1"
                style={
                  { positionAnchor: "--anchor-1" }
                }
              >
                <li>
                  <Link to={"/main/profile/edit"}>
                    <a>Edit Profile</a>
                  </Link>
                </li>
                <li>
                  <a onClick={() => onCLickLogout()}>Log out</a>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default CustomNavbar;
