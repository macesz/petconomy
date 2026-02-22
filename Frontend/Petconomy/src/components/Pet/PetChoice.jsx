import React, { useEffect, useState } from "react";
import Pokemon from "./Pokemon";
import { fetchData } from "../../utils";


function fetchPokemons(urls, onSetPokemons){
  urls.map((url) => {
    const response = fetchData(url)
    onSetPokemons((prev) => [...prev, response])
    });
}

function handleClick(event){
  const path = "";
  const method = "POST";
  const body = {
    evolutionId: event.evId
  };
  fetchData(path, method, body)
}

function PetChoice(){
  const [pokemons, setPokemons] = useState([]);
  const [loading, setLoading] = useState(true);
  // const [] = useState();
  // const [] = useState();

  const pokemonsUrls = [
    "https://pokeapi.co/api/v2/pokemon/bulbasaur",
    "https://pokeapi.co/api/v2/pokemon/charizard",
    "https://pokeapi.co/api/v2/pokemon/poliwhirl"
  ];

  useEffect(() => {
    if (pokemons.length === 0){
      fetchPokemons(pokemonsUrls, setPokemons)
    };

    if (pokemons.length >> 2){
      setLoading(false)
    };

    
  }, [pokemons]);


  return loading ? (
        <div className="">
            <h2> {"loading"}</h2>
        </div>
      ) : (
        <div className="">
          <div className="">
            <div className="">

            <div className="carousel carousel-center rounded-box">
            {
              pokemons.map((pokemon, index) => {
                return (
                  <Pokemon pokemon={pokemon} onHandleClick={handleClick} key={index} />
                )
              })
            }
            </div>
            </div>
          </div>
        </div>
      )
}

export default PetChoice