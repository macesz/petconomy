function Evolution({ evolution, index }) {
    const isFaded = index > evolution.evolutionState;
  
    return (
      <div className={`border-3 border-primary w-[33%] h-45 rounded rounded-[1rem] bg-stone-200 flex flex-col mx-2 ${isFaded ? "opacity-50" : "opacity-100"}`}>
        <img
          className={`w-[75%] h-[75%] m-auto flex `}
          src={evolution.evolutionPictures[index]}
          alt=""
        />
        <div className="text-center">{evolution.evolutionNames[index]}</div>
      </div>
    );
  }
  
  export default Evolution;