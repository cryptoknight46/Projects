import React from 'react'
import InputField from '../sharedfiles/InputField'
import Spinners from '../sharedfiles/Spinners'
import { Link } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { FaAddressCard } from 'react-icons/fa'
import { useDispatch, useSelector } from 'react-redux'
import toast from 'react-hot-toast'
import { addUpdateUserAddress } from '../../store/actions'

const AddAddressForm = ({address,setOpenAddressModal}) => {
    const dispatch=useDispatch();
    const { btnLoader } = useSelector((state)=>state.errors);
       const{
            register,
            handleSubmit,
            reset,
            formState:{errors},
        }=useForm({
            mode:"onTouched",
        });

        const onSaveAddressHandler=async(data)=>{
            dispatch(addUpdateUserAddress(
                data,
                toast,
                address?.addressId,
                setOpenAddressModal
            ));
            
        };


  return (
    <div>
    <form onSubmit={handleSubmit(onSaveAddressHandler)}
    >
            <div className="flex justify-center items-center mb-4 font-semibold text-2xl text-slate-800 px-4 py-2">
                <FaAddressCard className="mr-2 text-2xl"/>
                Add Address
            </div>
       
        <div className="flex flex-col gap-4">
            <InputField
            label="ApartmentName"
            required
            id="buildingName"
            type="text"
            message="*BuildingName is required"
            placeholder="Enter Building Name"
            register={register}
            errors={errors}/>

            <InputField
            label="City"
            required
            id="city"
            type="text"
            message="*City is required"
            placeholder="Enter your City"
            register={register}
            errors={errors}/>

            <InputField
            label="State"
            required
            id="state"
            type="text"
            message="*State is required"
            placeholder="Enter State"
            register={register}
            errors={errors}/>

            <InputField
            label="pincode"
            required
            id="pincode"
            type="text"
            message="*Pincode is required"
            placeholder="Enter Pincode"
            register={register}
            errors={errors}/>

            <InputField
            label="Street"
            required
            id="street"
            type="text"
            message="*Street is required"
            placeholder="Enter street"
            register={register}
            errors={errors}/>

            <InputField
            label="Country"
            required
            id="country"
            type="text"
            message="*Country is required"
            placeholder="Enter Country"
            register={register}
            errors={errors}/>

        </div>
        <button
        disabled={btnLoader}
        className="text-white bg-custom-blue px-4 py-2 rounded-md mt-4"
        type="submit">
            
            {btnLoader?(
               <> 
                <Spinners/>Loading...
               </>
            ):(
                <>Save</>
            )}
            
             </button>
            
    </form>
</div>
  )
}

export default AddAddressForm