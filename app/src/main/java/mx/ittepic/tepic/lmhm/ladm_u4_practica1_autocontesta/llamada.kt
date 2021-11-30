package mx.ittepic.tepic.lmhm.ladm_u4_practica1_autocontesta

import android.content.ContentValues
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
        baseRemota.collection("LISTANEGRA").document(tel).addSnapshotListener { value, error ->
            if (!value!!.getString("TELEFONO").equals("null")){
                val datos = ContentValues()
                datos.put("TEL",tel)
                BaseDatos(pointer,"TELEFONOS",null,1).writableDatabase.insert("NUMEROS",null,datos)
            }//end if
        }//addSnap

        val cursor = BaseDatos(pointer,"TELEFONOS",null,1).readableDatabase.rawQuery("SELECT * FROM NUMEROS",null)
        if (cursor.moveToFirst()){
            BaseDatos(pointer,"TELEFONOS",null,1).writableDatabase.delete("NUMEROS","TEL=?",arrayOf(tel))
            return true
        }
        return false
    }//select

    fun selectLB(tel: String): Boolean{
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