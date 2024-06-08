package com.householdshopper.model.repository

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.householdshopper.model.Household
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HouseholdRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    suspend fun getSpecificHousehold(householdID: String):Household{
        return try {
            val document = db.collection("Households").document(householdID)
                .get()
                .await()

            document.toObject(Household::class.java)?.apply {
                householdId = document.id
            } ?: Household()
        } catch (e: Exception){
            Household()
        }
    }
}