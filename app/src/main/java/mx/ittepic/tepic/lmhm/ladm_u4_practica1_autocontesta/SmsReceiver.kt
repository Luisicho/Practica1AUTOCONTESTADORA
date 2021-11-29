package mx.ittepic.tepic.lmhm.ladm_u4_practica1_autocontesta

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Build
import android.telephony.SmsMessage
import android.widget.Toast

class SmsReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras

        if (extras != null){
            var sms = extras.get("pdus") as Array<Any>
            for (indice in sms.indices){
                val formato = extras.getString("format")

                var smsMensaje = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    SmsMessage.createFromPdu(sms[indice] as ByteArray,formato)
                }else{
                    SmsMessage.createFromPdu(sms[indice] as ByteArray)
                }
                var celularOrigen = smsMensaje.originatingAddress
                var contenidoSMS = smsMensaje.messageBody.toString()

                //GUARDAR EN TABLA SQLITE
                try {
                    var baseDatos = BaseDatos(context, "-",null,1)
                    var insertar = baseDatos.writableDatabase
                    var SQL = "INSERT INTO LISTANEGRA VALUES ('${celularOrigen}','${contenidoSMS}')"
                    insertar.execSQL(SQL)
                    baseDatos.close()
                }catch (err: SQLiteException){
                    Toast.makeText(context,"Error ${err.message}", Toast.LENGTH_LONG)
                }

            }
        }
    }
}