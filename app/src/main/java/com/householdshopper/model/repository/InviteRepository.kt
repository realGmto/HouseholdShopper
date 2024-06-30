package com.householdshopper.model.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.householdshopper.model.Invite
import com.householdshopper.model.ShoppingList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class InviteRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val collectionPath = "Invites"

    fun getSendInvitesUpdates(userID:String): Flow<List<Invite>> = callbackFlow{
        val listenerRegistration: ListenerRegistration = db.collection(collectionPath)
            .whereEqualTo("from",userID)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }

                val invites = snapshots?.documents?.mapNotNull { doc->
                    doc.toObject(Invite::class.java)?.apply { documentId = doc.id }
                }?: emptyList()

                trySend(invites).isSuccess
            }
        awaitClose{ listenerRegistration.remove() }
    }

    fun getReceivedInvitesUpdates(userID:String): Flow<List<Invite>> = callbackFlow{
        val listenerRegistration: ListenerRegistration = db.collection(collectionPath)
            .whereEqualTo("to",userID)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }

                val requests = snapshots?.documents?.mapNotNull { doc->
                    doc.toObject(Invite::class.java)?.apply { documentId = doc.id }
                }?: emptyList()

                trySend(requests).isSuccess
            }
        awaitClose{ listenerRegistration.remove() }
    }
}