package com.xs.carappinterviewtask.ui.screens.detailScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.xs.carappinterviewtask.R
import com.xs.carappinterviewtask.data.model.responses.GetCarsResponseItem
import com.xs.carappinterviewtask.databinding.FragmentDetailsBinding
import com.xs.carappinterviewtask.ui.viewmodels.SharedViewModel
import com.xs.carappinterviewtask.utils.base.BaseFragment
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.load
import com.xs.carappinterviewtask.utils.objects.ExtensionUtils.safeRun
import com.xs.carappinterviewtask.utils.objects.SharedTransitions
import es.dmoral.toasty.Toasty

class DetailsFragment : BaseFragment<FragmentDetailsBinding>(true) {
    private val viewModel:SharedViewModel by activityViewModels()
    override fun layoutResource(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDetailsBinding {
        return FragmentDetailsBinding.inflate(inflater,container,false)
    }

    override fun onBackPressedCallback() {
        popFrom(R.id.detailsFragment)
    }

    private fun handleTransition(){
        safeRun{
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handleTransition()
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.selectedItem != null){
            bindData(viewModel.selectedItem!!)
        }else{
            Toasty.error(globalContext,"No data found", Toast.LENGTH_SHORT).show()
            popFrom(R.id.detailsFragment)
        }
        startPostponedEnterTransition()
    }

    fun bindData(car: GetCarsResponseItem){
        with(binding){
            carImage.load(car.image)
            makeTv.text = car.make
            modelTv.text = car.model
            yearTv.text = car.year.toString()
            priceTv.text = "$${car.price}"
            colorTv.text = "Color: ${car.color}"
            engineTv.text = "Engine: ${car.engine}"
            transmissionTv.text = "Transmission: ${car.transmission}"
            fuelTypeTv.text = "Fuel: ${car.fuelType}"
            horsepowerTv.text = "Horsepower: ${car.horsepower} HP"
            mileageTv.text = "Mileage: ${car.mileage} km"
            ownersTv.text = "Owners: ${car.owners}"
            featuresTv.text = "Features: ${car.features.joinToString(", ")}"
        }
    }

}