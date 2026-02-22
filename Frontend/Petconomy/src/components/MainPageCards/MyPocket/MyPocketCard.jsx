import React from 'react'
import pikachu from "../../../assets/pikachu.png";
import "./MyPocketCard.css"

function MyPocketCard({cardClasses, navigate}) {
  return (
        <div
        onClick={() => navigate("/main/mypocket")}
        className={
          cardClasses + " bg-warning/50 lightning col-span-2 xl:col-span-1"
        }
        style={{ backgroundImage: `url(${pikachu})` }}
      >
        <div className="absolute inset-0  opacity-0"></div>
        <div className="text-black bg-white/80 font-[1000] text-center p-2 w-[50%] h-[25%] m-auto z-1 rounded-full gyara flex items-center justify-center overflow-hidden">
        <h1 className="whitespace-nowrap text-xl sm:text-lg xs:text-base">
          My pocket
        </h1>
      </div>
      </div>
  )
}

export default MyPocketCard