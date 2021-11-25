package mx.ittepic.tepic.lmhm.ladm_u4_practica1_autocontesta

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE ENTRANTES(TELEFONO VARCHAR(200))")
        db.execSQL("CREATE TABLE LISTANEGRA(ID INTEGER PRIMARY KEY  AUTOINCREMENT,NOMBREN VARCHAR(200),TELEFONON VARCHAR(10))")
        db.execSQL("CREATE TABLE LISTABLANCA(ID INTEGER PRIMARY KEY  AUTOINCREMENT,NOMBREB VARCHAR(200),TELEFONOB VARCHAR(10))")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

}