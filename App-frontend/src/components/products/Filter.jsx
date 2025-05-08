import { Button, FormControl, IconButton, InputLabel, MenuItem, Select, Tooltip } from "@mui/material";
import { useEffect, useState } from "react";
import {FiArrowDown, FiArrowUp, FiRefreshCw, FiSearch} from 'react-icons/fi'
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";

// http://localhost:xxxxx?keyword=test&sortby=desc
//
//1.Make sure url is updated with filter values
//2.use this filter values for getting data from backend

const Filter =({categories})=>{
   
    //use search params are used to read and update query parameters
    //anything after the ?
    const [searchParams]=useSearchParams();
    //creates the copy object similar to search params 
    const params=new URLSearchParams(searchParams);
    
    //provides the pathname ex:/products
    const pathname=useLocation().pathname; 
    //console.log(pathname);
    //user should be navigated to changed page from parameters
    const navigate=useNavigate();



    const [category,setCategory]=useState("all");
    const [sortOrder,setSortOrder]=useState("asc");
    const [searchTerm,setSearchTerm]=useState("");

    useEffect(()=>{
        const currentCategory=searchParams.get("catogery")||"all";
        const currentSortOrder=searchParams.get("sortby")||"asc";
        const currentSearchTerm=searchParams.get("keyword")||"";

        setCategory(currentCategory);
        setSortOrder(currentSortOrder);
        setSearchTerm(currentSearchTerm);
    },
    [searchParams]);

    useEffect(()=>{
        const handler=setTimeout(()=>{
            if(searchTerm){
                searchParams.set("keyword",searchTerm);
            }else{
                searchParams.delete("keyword");
            }
            navigate(`${pathname}?${searchParams.toString()}`);
        },700);
        return ()=>{
            clearTimeout(handler);
        };
    },[searchParams,searchTerm,navigate,pathname])

    const handleChangeCategory=(event)=>{
        //console.log("handlecatogery change called");
        const selectedCategory=event.target.value;
        //console.log(selectedCategory);
        if(selectedCategory==="all"){
            params.delete("catogery");
        }else{
            params.set("catogery",selectedCategory);
        }  
        //console.log(selectedcategory);
        navigate(`${pathname}?${params.toString()}`);
        setCategory(event.target.value);
    };
    //console.log(pathname);  
    const toggleSortOrder = () => {
        const newOrder = sortOrder === "asc" ? "desc" : "asc";
        setSortOrder(newOrder);
        params.set("sortby", newOrder);
        navigate(`${pathname}?${params.toString()}`);
      };


    const handleClearFilter=()=>{
        navigate({pathname:window.location.pathname});
    };
//
    return(
        <div className="flex lg:flex-row flex-col-reverse  lg:justify-between justify-center items-center gap-4  ">
            {/*SEARCH BAR*/}
            <div className="relative flex items-center 2xl:w-[450px] sm:w-[420px] w-full">
                <input type="text"
                placeholder="searchproducts" 
                onChange={(e)=>setSearchTerm(e.target.value)}
                className="border border-gray-400 text-slate-800 rounded-md py-2 pl-10 pr-4 w-full focus:outline-none focus:ring-2 focus:ring-[#1976d2]"/>
                <FiSearch className="absolute left-3 text-slate-800 size={20}"/>
            </div>
            {/*CATEGORY SELECTION*/}
            <div className="flex sm:flex-row flex-col gap-4 items-center ">
                <FormControl
                className="text-slate-800 border-slate-700"
                variant="outlined"
                size="small">
                    <InputLabel id="category-select-label">Category</InputLabel>
                    <Select labelId="category-select-label"
                    value={category}
                    onChange={handleChangeCategory}
                    label="category"
                    className="min-w-[120px] text-slate-800 border-slate-700">
                        <MenuItem value="all">All</MenuItem>
                        {categories.map((item)=>(
                             <MenuItem key={item.catogeryId}value={item.catogeryName}>
                                {item.catogeryName}
                             </MenuItem>
                        ))}
                    </Select>

                    </FormControl>

                    {/*SORT BUTTON & CLEAR FILTER  */}
                    <Tooltip title="sorted by price:asc">
                        <Button
                        onClick={toggleSortOrder}
                        variant="contained"
                        color="primary" className="flex items-center gap-2 h-10"
                        >Sort By
                        {sortOrder==="asc"?(
                            <FiArrowUp size={20}/>
                        ):(
                            <FiArrowDown size={20}/>
                        )}
                        </Button>
                        
                    </Tooltip>
                    <button className="flex items-center gap-2 bg-rose-900 text-white px-3 py-2 rounded-md transition duration-300 ease-in shadow-md focus:outline-non "
                     onClick={handleClearFilter}>
                        <FiRefreshCw className="font-semibold" size={16}></FiRefreshCw>
                        <span className="font-semibold">Clear Filter</span>
                    </button>
            </div>
        </div>
    )
}

export default Filter;