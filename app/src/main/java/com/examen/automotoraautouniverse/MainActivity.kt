package com.examen.automotoraautouniverse

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.facebook.login.LoginManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //TESTEANDO ANALYTICS
        val analylitics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("Mensaje","Funcionando correctamente con Firebase")
        analylitics.logEvent("MainActivity",bundle)
        //Recibir info
        val aux = intent.extras
        val correo = aux?.getString("CORREO")
        val proveedor = aux?.getString("PROVEEDOR")
        val recordar = aux?.getBoolean("RECORDAR")
        preferencias(correo ?: "", proveedor ?:"", recordar ?: false)
        config(correo ?:"",proveedor ?:"")
    }

    private fun config(correo: String, proveedor: String){
        title = "Bienvenido"
        btnCerrarSesion.setOnClickListener(){
            val pref = getSharedPreferences("com.examen.automotoraautouniverse.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit()
            pref.clear()
            pref.apply()
            FirebaseAuth.getInstance().signOut()
            //Facebook
            LoginManager.getInstance().logOut()

            onBackPressed()
        }

        btnHome.setOnClickListener(){
            startActivity(Intent(this,ActivityPrincipal::class.java))
        }

    }

    private fun preferencias(correo: String, proveedor: String, recordar: Boolean){
        //Guardar datos en preferencias
        if(recordar == true){
            val pref = getSharedPreferences("com.examen.automotoraautouniverse.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit()
            pref.putString("CORREO",correo)
            pref.putString("PROVEEDOR",proveedor)
            pref.apply()
        }
    }


}