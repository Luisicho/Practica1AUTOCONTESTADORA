package mx.ittepic.tepic.lmhm.ladm_u4_practica1_autocontesta

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    val siPermisoEnvioSMS = 1
    val siPermisoLeerSMSLlamadaEstado = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.activar).setOnClickListener {
            //validar permisos para enviar SMS
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS),siPermisoEnvioSMS)
            }
            //validar permisos para leer SMS,estado LLAMADAS,leer numero llamadas
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_PHONE_NUMBERS)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS,
                    android.Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.READ_PHONE_NUMBERS),siPermisoLeerSMSLlamadaEstado)
            }
        }
        findViewById<Button>(R.id.insertar).setOnClickListener{
            var telefono = llamada(this)
            AlertDialog.Builder(this)
                .setTitle("ATENCION")
                .setMessage("¿DONDE INSERTAR?")
                .setPositiveButton("LISTA NEGRA"){d,i->
                    //toma los datos de los Edit text y los inserta a lista negra
                    telefono.nombre = findViewById<EditText>(R.id.NOMBRE).text.toString()
                    telefono.telefono = findViewById<EditText>(R.id.TELEFONO).text.toString()
                    telefono.insertaFirestoreListaNegra()
                    d.dismiss()
                }
                .setNegativeButton("LISTA BLANCA"){d,i->
                    //toma los datos de los Edit text y los inserta a lista negra
                    telefono.nombre = findViewById<EditText>(R.id.NOMBRE).text.toString()
                    telefono.telefono = findViewById<EditText>(R.id.TELEFONO).text.toString()
                    telefono.insertaFirestoreListaBlanca()
                    d.dismiss()
                }
                .setNeutralButton("CANCELAR"){d,i->
                    d.cancel()
                }
                .show()
        }
    }
}