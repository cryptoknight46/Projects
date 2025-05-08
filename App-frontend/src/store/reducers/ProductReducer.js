

const initialState={
    products:null,
    catogeries:null,
    pagination:{},
};

export const productReducer=(state=initialState,action)=>{
        switch(action.type){
            case "FETCH_PRODUCTS":
                return {
                    ...state,
                    products:action.payload,
                    pagination:{
                        ...state.pagination,
                        pageNumber:action.pageNumber,
                        pageSize:action.pageSize,
                        totalElements:action.totalElements,
                        totalPages:action.totalPages,
                        lastPage:action.lastpage,
                    },
                };

                case "FETCH_CATEGORIES":
                    return {
                        ...state,
                        catogeries:action.payload,
                        
                        pagination:{
                            ...state.pagination,
                            pageNumber:action.pageNumber,
                            pageSize:action.pageSize,
                            totalElements:action.totalElements,
                            totalPages:action.totalPages,
                            lastPage:action.lastpage,
                        },
                    };
            default:
                return state;    
        }
};