import React, { useState } from "react";

function Pet(props){
  return (
    <div className="carousel-item glass">
        <button className="btn btn-accent btn-outline hover-light">{props.pokemon.forms[0].name}</button>
      <div className="">
        <img src={props.pokemon.sprites.other["dream_world"]["front_default"]}></img>
      </div>
      
    </div>
  )
}

export default Pet