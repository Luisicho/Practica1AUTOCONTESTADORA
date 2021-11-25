package mx.ittepic.tepic.lmhm.ladm_u4_practica1_autocontesta

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore

class llamada(p: Context) {
    var nombre = ""
    var telefono = ""
    var baseRemota = FirebaseFirestore.getInstance()
    var pointer = p

    fun insertaFirestoreListaNegra() {
        var datosInsertar = hashMapOf(
            "NOMBRE" to nombre,
            "TELEFONO" to telefono
        )
        baseRemota.collection("LISTANEGRA")
            .add(datosInsertar as Any)
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
            .add(datosInsertar as Any)
            .addOnSuccessListener {
                alerta("Se inserto correctamente en la nube lista blanca")
            }
            .addOnFailureListener {
                mensaje("Error: ${it.message!!}")
            }
    }
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