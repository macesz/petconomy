import React, { useEffect, useState } from "react";
import { fetchData } from "../../utils";
import { picture } from "framer-motion/client";
import Evolution from "../../components/MyPokemon/Evolution";
import RazBerry from "../../assets/razberry.webp"

function MyPet() {
  const [myPokemon, setMyPokemon] = useState({
    experience: 0,
    happiness: 70,
    evolution: {
      evolutionState: 1,
      evolutionPictures: {
        1: "https://www.pngmart.com/files/22/Charmander-Pokemon-PNG-Clipart.png",
        2: "https://www.pngmart.com/files/22/Charmeleon-Pokemon-PNG-Photos.png",
        3: "https://www.pngmart.com/files/12/Charizard-PNG-Free-Download.png",
      },
      evolutionGifs: {
        1: "https://media.tenor.com/4K2_dLLq-pwAAAAj/charmander-chases-tail.gif",
        2: "https://media.tenor.com/fk9-MPwwo60AAAAi/pok%C3%A9mon-charmeleongif.gif",
        3: "https://media.tenor.com/63A8n2i1E3YAAAAi/charizard.gif"
      },
      evolutionNames: {
        1: "Charmander",
        2: "Charmeleon",
        3: "Charizard",
      },
    },
  });
  const [saving, setSaving] = useState(null);
  const [canYouFeed, setCanYouFeed] = useState(true);


  function feed() {
    setMyPokemon((prev) => {
        let newExperience = prev.experience + 25;
        const newHappiness = prev.happiness + 10;
        let newEvolutionState = prev.evolution.evolutionState;
    
        if (newExperience >= 100 && newEvolutionState === 1) {
          newEvolutionState = 2;
          newExperience = 0;
        } else if (newExperience >= 100 && newEvolutionState === 2) {
          newEvolutionState = 3; // Charmeleon -> Charizard
          newExperience = 0;
        }
    
        return {
          ...prev,
          happiness: newHappiness,
          experience: newExperience,
          evolution: {
            ...prev.evolution,
            evolutionState: newEvolutionState,
          },
        };
      });
  }

  useEffect(() => {
    const jwt = localStorage.getItem("pokePocketJwt");
    // fetchData("user/currentPokemon", "GET", null, jwt).then(response => {
    //   setMyPokemon(response);
    // })
    fetchData("user/savings", "GET", null, jwt).then(response => {
        setSaving(response);
        if(response <= 0){
            setCanYouFeed(false);
        }
    })
  }, []);

  return (
    <div className="bg-white basis-6/12 m-auto relative h-152 grid grid-cols-1 rounded rounded-[1rem] overflow-hidden">
      {/* Háttérkép a felső felén */}
      <div className="z-3 absolute w-full h-55 flex bg-[url('https://previews.123rf.com/images/flukesamed/flukesamed1501/flukesamed150100001/35253868-blaze-fire-flame-texture-background.jpg')]">
            <div className="bg-white/50 m-auto h-50 w-50 my-2 rounded rounded-full">
                <img className="h-full w-full" src={myPokemon.evolution.evolutionGifs[myPokemon.evolution.evolutionState]} alt="" />
            </div>
      </div>
      {/* Alsó rész a tartalommal */}
      <div className="relative z-10 bg-white p-4 mt-[30%] ">
        <p className="text-center mx-auto bold text-3xl">{myPokemon.evolution.evolutionNames[myPokemon.evolution.evolutionState]}</p>
        <div className="grid grid-cols-2 gap-2">
          <div>
            <div className="mt-4 flex">
              <label className="my-auto mx-2 bold" htmlFor="">
                Experience:
              </label>
              <progress
                className="progress progress-accent w-56 my-auto mt-[0.6rem]"
                value={myPokemon.experience}
                max="100"
              ></progress>
            </div>
            <div className="mt-4 flex">
              <label className="my-auto mx-2 bold" htmlFor="">
                Happiness:
              </label>
              <progress
                className="progress progress-accent w-56 my-auto mt-[0.6rem]"
                value={myPokemon.happiness}
                max="100"
              ></progress>
            </div>
          </div>
          <div className="flex mt-2">
            <div className={`btn h-20 w-30 border-2 border-black mx-auto drop-shadow-xl ${canYouFeed ? "": "btn-disabled opacity-50"}`} onClick={() => feed()}>
              <img className="h-full w-full" src={RazBerry} alt="Feed" title="Feed" /></div>
          </div>
        </div>
      </div>
      <h2 className="text-center">Evolution progress:</h2>
      <div className="flex p-5">
        {Array.from({ length: 3 }, (_, index) => index + 1).map((index) => (
          <Evolution
            key={index}
            evolution={myPokemon.evolution}
            index={index}
          />
        ))}
      </div>
    </div>
  );
}

export default MyPet;
