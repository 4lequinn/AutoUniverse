package com.examen.automotoraautouniverse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_crud_automovil.*
import kotlinx.android.synthetic.main.fragment_crud_empleados.*
import kotlinx.android.synthetic.main.fragment_crud_empleados.btnBuscar
import kotlinx.android.synthetic.main.fragment_crud_empleados.btnEliminar
import kotlinx.android.synthetic.main.fragment_crud_empleados.btnGuardar

class crudEmpleadosFragment : Fragment() {
//LLAMADA A LA BD
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crud_empleados, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()
    }

    private fun setup(){
        btnGuardar.setOnClickListener(){
            db.collection("empleado").document(txtCorreo.text.toString()).set(
                hashMapOf(
                    "correo" to txtCorreo.text.toString(),
                    "nombre" to txtNombre.text.toString(),
                    "contrasenia" to txtPass.text.toString(),
                    "telefono" to txtTelefono.text.toString(),
                    "direccion" to txtDireccion.text.toString()
                )
            )
            limpiar()
            msj("Datos Guardados")
        }

        btnEliminar.setOnClickListener(){
            db.collection("empleado").document(txtCorreo.text.toString()).delete(
            )
            limpiar()
            msj("Empleado eliminado")
        }
        btnBuscar.setOnClickListener(){
            db.collection("empleado").document(txtCorreo.text.toString()).get().addOnSuccessListener {
                txtCorreo.setText(it.get ("correo") as String?)
                txtNombre.setText(it.get ("nombre") as String?)
                txtPass.setText(it.get ("contrasenia") as String?)
                txtTelefono.setText(it.get ("telefono") as String?)
                txtDireccion.setText(it.get("direccion") as String?)
            }
        }
    }

    private fun limpiar(){
        txtCorreo.setText("")
        txtNombre.setText("")
        txtPass.setText("")
        txtTelefono.setText("")
        txtDireccion.setText("")
    }

    private fun msj(mensaje: String){
        Toast.makeText(context,mensaje, Toast.LENGTH_SHORT).show()
    }
}