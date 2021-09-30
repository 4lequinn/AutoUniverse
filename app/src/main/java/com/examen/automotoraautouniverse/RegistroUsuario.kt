package com.examen.automotoraautouniverse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registro_usuario.*

class RegistroUsuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)
        clickBtnSalir()
        crearCuenta()
        title = "Regístrate"
    }

    private fun clickBtnSalir(){
        btnVolver.setOnClickListener(){
            msgAcceso("Regresando al login")
            val ventanaLogin = Intent(this,Login::class.java)
            startActivity(ventanaLogin)
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }
    }

    private fun msgAcceso(msg: String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT ).show()
    }

    private fun crearCuenta(){
        btnRegistrarse.setOnClickListener(){

            if(txtPass.text.toString().equals(txtPass2.text.toString()) && txtCorreo.text.isNotEmpty()){
                if(txtPass.text.isNotEmpty()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtCorreo.text.toString(),txtPass.text.toString()).addOnCompleteListener(){
                        if(it.isSuccessful){
                            //Levantar la ventana
                            limpiarDatos()
                            msgAcceso("Cuenta Creada")
                        }else{
                            mostrarMensaje("Se produjo un error al momento de registrarse")
                        }
                    }
                }
            }else if(txtCorreo.text.isEmpty()){
                mostrarMensaje("Ingrese un correo electrónico")
            }else{
                mostrarMensaje("Las contraseñas no coinciden, vuelva a intentarlo.")
            }
        }
    }

    private fun mostrarMensaje(mensaje: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ERROR")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar",null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun limpiarDatos(){
        txtCorreo.setText("")
        txtPass.setText("")
        txtPass2.setText("")
    }

}