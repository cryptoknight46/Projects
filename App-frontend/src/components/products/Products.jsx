/* eslint-disable no-unused-vars */

import { FaExclamationTriangle } from "react-icons/fa";

import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { fetchCategories, fetchProducts } from "../../store/actions";

import { RotatingLines } from "react-loader-spinner";

import ProductCard from "../sharedfiles/ProductCard";

import Filter from "./Filter";
import Loader from "../sharedfiles/Loader";
import useProductFilter from "../../hooks/useProductFilter";
import Paginations from "../sharedfiles/Pagination";

const Products=()=>{

    const {isLoading,errorMessage}=useSelector(
        (state)=>state.errors
    );
    

    
    const {products,catogeries,pagination}=useSelector(
        (state)=>state.products
    )
    console.log(pagination);
    const dispatch=useDispatch();
    useProductFilter();
    

    useEffect(()=>{
        dispatch(fetchCategories()) 
    },[dispatch]);

    
    
   
    return(
        <div className="lg:px-14 sm:px-4 py-14 2xl:w-[90%] 2xl:mx-auto">
            <Filter categories={catogeries ? catogeries:[]}/>
           
            {   
                isLoading ? (
                    <Loader text={"Product is Loading..."}/>
                      
                    ) : errorMessage ? (
                        <div className="flex justify-center items-center h-[200px]">
                            <FaExclamationTriangle className="text-slate-800 text-3xl mr-2"/>
                            <span className="text-slate-800 text-lg font-medium">
                                {errorMessage}
                            </span>
                        </div>
                ) : (
                    <div className="min-h-[700px]">
                     
                        <div className="pb-6 pt-14 grid 2xl:grid-cols-4 lg:grid-cols-3 sm:grid-cols-2 gap-y-6 gap-x-6">
                            {products && 
                             products.map((item,i)=><ProductCard key={i} {...item}/>)}
                       
                        </div>
                        <div className="flex justify-center pt-10">
                            <Paginations
                            numberOfPage={pagination?.totalPages}
                            totalProducts={pagination?.totalElements}/>
                        </div>
                    </div>
                )
            }
        </div>
    )
}

export default Products