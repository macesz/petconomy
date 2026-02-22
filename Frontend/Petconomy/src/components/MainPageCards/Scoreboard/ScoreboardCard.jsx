import React from 'react'
import charizard from "../../../assets/charizard.png";
import "./ScoreboardCard.css"

function ScoreboardCard({cardClasses, navigate}) {
      return (
        <div
        onClick={() => navigate("/main/mypokemon")}
        className={
          cardClasses + " bg-error/50 fire col-span-2 chari"
        }
        style={{ backgroundImage: `url(${charizard})` }}
      >
        <div className="absolute inset-0  opacity-0"></div>
        <div className="text-black bg-white/80 font-[1000] text-center p-2 w-[50%] h-[25%] m-auto z-1 rounded-full gyara flex items-center justify-center overflow-hidden">
        <h1 className="whitespace-nowrap text-xl sm:text-lg xs:text-base">
          Scoreboard
        </h1>
      </div>
      </div>
      )
}

export default ScoreboardCard