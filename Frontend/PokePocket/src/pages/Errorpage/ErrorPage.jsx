import React from "react";

const PokeBall = new URL("../../assets/pokeball.png", import.meta.url).href;

function ErrorPage() {
  return (
    <div className="flex h-[80vh]">
      <div className="flex justify-center basis-6/12 mx-auto bg-white rounded-full p-6 my-auto">
        <div className="text-pokeball my-auto text-9xl">4</div>
        <img src={PokeBall} alt="PokeBall" className="max-h-28 my-auto" />
        <div className="text-pokeball my-auto text-9xl">4</div>
      </div>
    </div>
  );
}

export default ErrorPage;
