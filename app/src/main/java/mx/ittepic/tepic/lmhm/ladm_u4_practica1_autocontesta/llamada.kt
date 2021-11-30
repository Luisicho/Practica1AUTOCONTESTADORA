package mx.ittepic.tepic.lmhm.ladm_u4_practica1_autocontesta

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class llamada(p: Context) {
    var nombre = ""
    var telefono = ""
    var baseRemota = FirebaseFirestore.getInstance()
    private lateinit var database: DatabaseReference
    var pointer = p

    fun insertaFirestoreListaNegra() {
        var datosInsertar = hashMapOf(
            "NOMBRE" to nombre,
            "TELEFONO" to telefono
        )
        baseRemota.collection("LISTANEGRA")
            .document(telefono)
                .set(datosInsertar)
            .addOnSuccessListener {
                alerta("Se inserto correctamente en la nube lista negra")
            }
            .addOnFailureListener {
                mensaje("Error: ${it.message!!}")
            }
    }
    fun insertaFirestoreListaBlanca() {
        var datosInsertar = hashMapOf(
            "NOMBRE" to nombre,
            "TELEFONO" to telefono
        )
        baseRemota.collection("LISTABLANCA")
            .document(telefono)
                .set(datosInsertar)
            .addOnSuccessListener {
                alerta("Se inserto correctamente en la nube lista blanca")
            }
            .addOnFailureListener {
                mensaje("Error: ${it.message!!}")
            }
    }

    fun selectLN(tel: String): Boolean{
        database = Firebase.database.reference
        if(database.child("LISTANEGRA").child(tel).get() != null){
            Toast.makeText(p0,)
            return true
        }
        return false
    }//select

    fun selectLB(tel: String): Boolean{
        database = Firebase.database.reference
        if(database.child("LISTABLANCA").child(tel).get() != null){
            return true
        }
        return false
    }//select


    private fun mensaje(s: String) {
        AlertDialog.Builder(pointer).setTitle("ATENCION")
            .setMessage(s)
            .setPositiveButton("OK"){s,i->}
            .show()
    }

    private fun alerta(s: String) {
        Toast.makeText(pointer,s, Toast.LENGTH_LONG).show()
    }
}