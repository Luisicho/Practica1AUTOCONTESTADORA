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
                    //Busca el numero en la lista Negra
                if (!numerotelefonico.equals("null")){
                    if(llamada(p0).selectLN(numerotelefonico.toString())){
                        envioSMS(true,numerotelefonico.toString())
                    }else
                        if(llamada(p0).selectLB(numerotelefonico.toString()))
                            envioSMS(false,numerotelefonico.toString())
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