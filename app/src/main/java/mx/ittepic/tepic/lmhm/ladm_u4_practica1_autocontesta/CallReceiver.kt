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

        if (p1.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            //Inicio llamada
            estadollamada = true
        }

        if (p1.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)){
            numerotelefonico  = p1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).toString()
            //Pregunta si no contesto
            if (!estadollamada) {
                var baseRemota = FirebaseFirestore.getInstance()
                    //Busca el numero en la lista Negra
                if (!numerotelefonico.equals("null")){
                    baseRemota.collection("LISTANEGRA").document(numerotelefonico.toString())
                        .addSnapshotListener { value, error ->

                        if (value!!.getString("TELEFONO") != null) {
                            Toast.makeText(p0,
                                "se envio lista negra ${value!!.getString("TELEFONO")}",
                                Toast.LENGTH_LONG).show()
                            envioSMS(true,numerotelefonico.toString())
                        }else{
                            Toast.makeText(p0,
                                "No se encontro ${numerotelefonico.toString()} en lista negra",
                                Toast.LENGTH_LONG).show()
                        }
                    }
                    baseRemota.collection("LISTABLANCA").document(numerotelefonico.toString())
                        .addSnapshotListener { value, error ->

                            if (value!!.getString("TELEFONO") != null) {
                                Toast.makeText(p0,
                                    "se envio lista blanca ${value!!.getString("TELEFONO")}",
                                    Toast.LENGTH_LONG).show()
                                envioSMS(false,numerotelefonico.toString())
                            }else{
                                Toast.makeText(p0,
                                    "No se encontro ${numerotelefonico.toString()} en lista blanca",
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }//if !estadollamada
        }// end if
    }//end onReciver
    
    private fun envioSMS(lista:Boolean,telefono:String) {
        if (lista){
            //Lista negra mensaje
            SmsManager.getDefault().sendTextMessage(telefono,null,
                "DEJE DE LLAMAR",null,null)
        }else{
            //Lista blanca mensaje
            SmsManager.getDefault().sendTextMessage(telefono,null,
                "LLAME MAS TARDE PORFAVOR",null,null)
        }//end else
    }//end envioSMS

}//end class