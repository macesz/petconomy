import React from "react";
import ReactDOM from "react-dom/client";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Registration from './pages/Registration/Registration.jsx';
import "./main.css"
import ErrorPage from './pages/Errorpage/ErrorPage.jsx';
import MainPage from './pages/MainPage/MainPage.jsx';
import MyPokemon from './pages/MyPokemon/MyPokemon.jsx';
import MyPocket from './pages/MyPocket/MyPocket.jsx';
import Layout from './pages/Layout.jsx';
import ProfileEdit from "./pages/EditPage/ProfileEdit.jsx";

const router = createBrowserRouter([
    {
        path: "/",
        element: <Registration/>
    },
    {
        path: "/main",
        element: <Layout/>,
        errorElement: <ErrorPage/>,
        children: [
            {
                path: "/main",
                element: <MainPage/>
            },
            {
                path: "/main/mypokemon",
                element: <MyPokemon/>
            },
            {
                path: "/main/mypocket",
                element: <MyPocket/>
            },
            {
                path: "/main/profile/edit",
                element: <ProfileEdit/>
            }

        ],
    },
]);

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
    <React.StrictMode>
        <RouterProvider router={router}/>
    </React.StrictMode>
);
