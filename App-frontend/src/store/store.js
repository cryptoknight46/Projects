import { configureStore } from "@reduxjs/toolkit";
import { productReducer } from "./reducers/ProductReducer";
import { errorReducer } from "./reducers/errorReducer";
import { cartReducer } from "./reducers/cartReducer.js";
import { authReducer } from "./reducers/authReducer.js";

const user=localStorage.getItem("auth")
                ?JSON.parse(localStorage.getItem("auth"))
                :null;

const cartItems=localStorage.getItem("cartItems")
                ?JSON.parse(localStorage.getItem("cartItems"))
                :[];
const initialState={
    auth:{user:user},
    carts:{cart:cartItems},
};
export const store=configureStore({
    reducer:{
        products:productReducer,
        errors:errorReducer,
        carts:cartReducer,
        auth:authReducer

    },
    preloadedState:initialState,  
    
});

export default store;