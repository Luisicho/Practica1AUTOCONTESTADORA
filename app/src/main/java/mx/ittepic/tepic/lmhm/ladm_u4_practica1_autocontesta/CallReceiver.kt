package mx.ittepic.tepic.lmhm.ladm_u4_practica1_autocontesta

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore

class CallReceiver: BroadcastReceiver() {
    var numerotelefonico: String ? = null
    override fun onReceive(p0: Context, p1: Intent) {
        var estadollamada = false

        if (p1.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
            //Llamada entrante
                //asigna numero telefonico que entro
            numerotelefonico  = p1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).toString()
        }

        if (p1.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            //Inicio llamada
            estadollamada = true
        }

        if (p1.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)){
            //Llamada terminada
                //Pregunta si no contesto
            if (!estadollamada){
                //Crea instancia de firestore
                var baseRemota = FirebaseFirestore.getInstance()
                //Consigue la lista negra
                var listaNegra = ArrayList<llamada>()
                baseRemota.collection("LISTANEGRA")
                    .addSnapshotListener { querySnapshot, error ->
                        if (error != null) {
                            AlertDialog.Builder(p0).setTitle("ATENCION")
                                .setMessage(error.message!!)
                                .setPositiveButton("OK") { s, i -> }
                                .show()
                            return@addSnapshotListener
                        }//if END
                        listaNegra.clear()
                        //consigue lista negra
                        for (document in querySnapshot!!) {
                            val phone = llamada(p0)
                            phone.nombre = document.getString("NOMBRE")!!
                            phone.telefono = document.getString("TELEFONO")!!
                            listaNegra.add(phone)
                        }//for END

                        listaNegra.forEach {
                            //Verifica si esta en lista negra
                            if (it.telefono==numerotelefonico){
                                //Envia mensaje a numero de lista negra
                                envioSMS(true,it.telefono)
                            }
                        }
                    }
                //Consigue la lista blanca
                var listaBlanca = ArrayList<llamada>()
                baseRemota.collection("LISTABLANCA")
                    .addSnapshotListener { querySnapshot, error ->
                        if (error != null) {
                            AlertDialog.Builder(p0).setTitle("ATENCION")
                                .setMessage(error.message!!)
                                .setPositiveButton("OK") { s, i -> }
                                .show()
                            return@addSnapshotListener
                        }//if END
                        listaBlanca.clear()
                        //consigue lista blanca
                        for (document in querySnapshot!!) {
                            val phone = llamada(p0)
                            phone.nombre = document.getString("NOMBRE")!!
                            phone.telefono = document.getString("TELEFONO")!!
                            listaNegra.add(phone)
                        }//for END
                        listaBlanca.forEach {
                            //Verifica si esta en lista negra
                            if (it.telefono==numerotelefonico){
                                //Envia mensaje a numero de lista blanca
                                envioSMS(false,it.telefono)
                            }
                        }
                    }
            }
        }

    }
    private fun envioSMS(lista:Boolean,telefono:String) {
        if (lista){
            //Lista negra mensaje
            SmsManager.getDefault().sendTextMessage(telefono,null,
                "DEJE DE LLAMAR",null,null)
        }else{
            //Lista blanca mensaje
            SmsManager.getDefault().sendTextMessage(telefono,null,
                "LLAME MAS TARDE PORFAVOR",null,null)
        }

    }
}