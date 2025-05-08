


import React, { useState } from 'react';
import './App.css' 
import Home from './components/home/Home';
import Products from './components/products/Products'
import { BrowserRouter as Router,Routes,Route } from 'react-router-dom'
import Navbar from './components/sharedfiles/Navbar';
import About from './components/About';
import Contact from './components/Contact';
import { Toaster } from 'react-hot-toast';
import Cart from './components/cart/Cart';
import LogIn from './components/auth/Login';
import PrivateRoute from './components/PrivateRoute';
import Register from './components/auth/Register';
import Checkout from './components/checkout/Checkout';

function App() {
  
  
  return (

    <React.Fragment>
        <Router>
            <Navbar/>
            <Routes>
                <Route path='/' element={<Home/>}></Route>
                <Route path='/products' element={<Products/>}></Route>
                <Route path='/about' element={<About/>}></Route>
                <Route path='/contact' element={<Contact/>}></Route>
                <Route path='/cart' element={<Cart/>}></Route>
                {/* <Route path='/checkout' element={<Checkout/>}></Route> */}
               
                <Route path='/' element={<PrivateRoute publicPage/>}>
                    <Route path='/login' element={<LogIn/>}/>
                    <Route path='/register' element={<Register/>}/>    
                </Route> 
                <Route path='/' element={<PrivateRoute/>}>
                    <Route path='/checkout' element={<Checkout/>}/>
                </Route>       
            </Routes>
        </Router>
        <Toaster position='bottom-center'/>
   </React.Fragment>
  )
}

export default App;
