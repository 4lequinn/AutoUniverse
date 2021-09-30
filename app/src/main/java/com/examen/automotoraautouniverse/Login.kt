package com.examen.automotoraautouniverse

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
   //Acceder con google
    private val RC_SIGN_IN = 100
    private val callbackManager = CallbackManager.Factory.create()
    lateinit var switch1 :Switch
    var valida = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        switch1 = findViewById(R.id.switchRecordarUser)
        config()
        session()
        clickBtnRegistro()
    }

    //Consultando usuarios al auth
    private fun config(){
        title = "Autenticación"
        btnIniciarSesion.setOnClickListener() {
            if (txtCorreo.text.isEmpty()) {
                mostrarMensaje("Porfavor ingrese su correo electrónico","ERROR")
            } else if (txtPass.text.isEmpty()) {
                mostrarMensaje("Porfavor ingrese su contraseña","ERROR")
            } else if (txtCorreo.text.isNotEmpty() && txtPass.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(txtCorreo.text.toString(), txtPass.text.toString()).addOnCompleteListener() {
                        if (it.isSuccessful) {
                            //Levantar la ventana
                            mostrarVentana()
                        } else {
                            mostrarMensaje("El correo o la contraseña no coinciden con niguna cuenta, porfavor vuelva a intentarlo","ERROR")
                        }
                }
            }
        }

        btnGoogle.setOnClickListener(){
            val config = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            val cliente = GoogleSignIn.getClient(this,config)
            cliente.signOut()
            startActivityForResult(cliente.signInIntent,RC_SIGN_IN)
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }

        btnFacebook.setOnClickListener(){
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
            LoginManager.getInstance().registerCallback(callbackManager,
            object  : FacebookCallback<LoginResult>{
                override fun onSuccess(result: LoginResult?) {
                    result?.let {
                        val token = it.accessToken
                        val credential = FacebookAuthProvider.getCredential(token.token)

                        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(){
                            if (it.isSuccessful) {
                                //Levantar la ventana
                                mostrarVentana()
                            } else {
                                mostrarMensaje("Error face","ERROR")
                            }
                        }
                    }
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {
                   mostrarMensaje("Error al conectar con Facebook", "ERROR" )
                }
            })
        }
    }

    private fun mostrarMensaje(mensaje: String, titulo: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val dialog = builder.create()
        dialog.show()
    }
    private fun clickBtnRegistro(){
        btnRegistrarse.setOnClickListener(){
            msgAcceso("Accediendo")
            val ventanaRegistro = Intent(this,RegistroUsuario::class.java)
            startActivity(ventanaRegistro)
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }
    }

    private fun msgAcceso(msg: String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT ).show()
    }

    private fun mostrarVentana() {
        val ventanaMain = Intent(this, ActivityPrincipal::class.java).apply {
            //Enviamos la info del switch
            putExtra("RECORDAR", valida)
        }
        startActivity(ventanaMain)
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
    }


    private fun session(){
        val pref = getSharedPreferences("com.examen.automotoraautouniverse.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val correo = pref.getString("CORREO",null)
        val proveedor = pref.getString("PROVEEDOR", null)
        if(correo!=null && proveedor!=null){
            authLogin.visibility = View.INVISIBLE
            mostrarVentana()
        }
    }

    override fun onStart() {
        super.onStart()
        authLogin.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val cuenta = task.getResult(ApiException::class.java)
                val credenciales = GoogleAuthProvider.getCredential(cuenta?.idToken,null)
                FirebaseAuth.getInstance().signInWithCredential(credenciales).addOnCompleteListener(){
                    if(it.isSuccessful){
                        //Levantar ventana
                        mostrarVentana()
                    }else{
                        mostrarMensaje("Error con las credenciales","ERROR")
                    }
                }
            }catch (e: ApiException){
                mostrarMensaje("Debes seleccionar una cuenta","ADVERTENCIA")
            }
        }
    }

    fun onclick(view: View) {
        if(view.id == R.id.switchRecordarUser){
            if(switch1.isChecked){
                valida = true
            }else{
                valida = false
            }
        }
    }
}