package com.householdshopper.model.repository

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.householdshopper.model.Household
import com.householdshopper.model.HouseholdResult
import com.householdshopper.model.RegisterResult
import com.householdshopper.model.ResultMessage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HouseholdRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val collectionPath = "Households"
    suspend fun getSpecificHousehold(householdID: String):Household{
        return try {
            val document = db.collection(collectionPath).document(householdID)
                .get()
                .await()

            document.toObject(Household::class.java)?.apply {
                householdId = document.id
            } ?: Household()
        } catch (e: Exception){
            Household()
        }
    }

    suspend fun getAllHouseholds(): List<Household>{
        return try {
            val document = db.collection(collectionPath)
                .get()
                .await()
            document.documents.mapNotNull { doc ->
                doc.toObject(Household::class.java)?.apply { householdId = doc.id }
            }
        }catch (e:Exception){
            println(e.message)
            emptyList()
        }
    }

    suspend fun addNewHousehold(name: String): HouseholdResult {
        return try {
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            val household = hashMapOf(
                "name" to name,
                "members" to arrayOf(userId)
            )

            if (userId != null) {
                val document = db.collection(collectionPath)
                    .add(household)
                    .await()

                db.collection("Users").document(userId)
                    .update("householdID",document.id)
                    .await()

                HouseholdResult(success = true, message = "Successfully created new household")
            }
            else{
                HouseholdResult(success = false, message = "Failed to create new household")
            }
        }catch (e:Exception){
            println(e.message)
            HouseholdResult(success = false, message = "Error has occurred")
        }
    }

    suspend fun removeUserFromHousehold(userID: String,householdID: String): ResultMessage{
        return try {
            db.collection(collectionPath)
                .document(householdID)
                .update("members",FieldValue.arrayRemove(userID))
                .await()

            db.collection("Users")
                .document(userID)
                .update("householdID","")
                .await()

            ResultMessage(success = true, message = "Successfully kicked user out of the household")
        }catch (e:Exception){
            println(e.message)
            ResultMessage(success = false, message = "There was an error while trying to kick user out of the Household")
        }
    }
}