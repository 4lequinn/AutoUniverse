package com.examen.automotoraautouniverse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_crud_automovil.*

class crudAutomovilFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crud_automovil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setup()

    }

    private fun setup(){
        btnGuardar.setOnClickListener(){
            db.collection("automovil").document(txtCodigo.text.toString()).set(
                hashMapOf(
                    "codigo" to txtCodigo.text.toString(),
                    "marca" to txtMarca.text.toString(),
                    "modelo" to txtModelo.text.toString(),
                    "color" to txtColor.text.toString(),
                    "precio" to txtPrecio.text.toString()
                )
            )
            limpiar()
            msj("Datos Guardados")
        }

        btnEliminar.setOnClickListener(){
            db.collection("automovil").document(txtCodigo.text.toString()).delete(
            )
            limpiar()
            msj("Automóvil eliminado")
        }
        btnBuscar.setOnClickListener(){
            db.collection("automovil").document(txtCodigo.text.toString()).get().addOnSuccessListener {
                txtCodigo.setText(it.get ("codigo") as String?)
                txtMarca.setText(it.get ("marca") as String?)
                txtModelo.setText(it.get ("modelo") as String?)
                txtColor.setText(it.get ("color") as String?)
                txtPrecio.setText(it.get("precio") as String?)
            }
        }
    }

    private fun limpiar(){
        txtCodigo.setText("")
        txtMarca.setText("")
        txtModelo.setText("")
        txtColor.setText("")
        txtPrecio.setText("")
    }

    private fun msj(mensaje: String){
        Toast.makeText(context,mensaje,Toast.LENGTH_SHORT).show()
    }
}