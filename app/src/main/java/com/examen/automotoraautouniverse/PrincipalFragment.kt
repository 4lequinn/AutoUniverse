package com.examen.automotoraautouniverse

import android.content.Context
import android.os.Bundle
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examen.automotoraautouniverse.models.Empleado
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_principal.*

class EmpleadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class PrincipalFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_principal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Añadir un evento de boton
        val query = db.collection("empleado")
        val options = FirestoreRecyclerOptions.Builder<Empleado>().setQuery(query,Empleado::class.java)
            .setLifecycleOwner(this).build()
        val adapter = object : FirestoreRecyclerAdapter<Empleado, EmpleadoViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpleadoViewHolder {
                val view = LayoutInflater.from(context).inflate(R.layout.row_empleados,parent,false)
                return EmpleadoViewHolder(view)
            }

            override fun onBindViewHolder(holder: EmpleadoViewHolder, position: Int, model: Empleado) {
                val correo: TextView = holder.itemView.findViewById(R.id.tvCorreo)
                val nombre: TextView = holder.itemView.findViewById(R.id.tvNombre)
                val contrasenia: TextView = holder.itemView.findViewById(R.id.tvPass)
                val telefono: TextView = holder.itemView.findViewById(R.id.tvTelefono)
                val direccion: TextView = holder.itemView.findViewById(R.id.tvDireccion)
                correo.text = model.correo
                nombre.text = model.nombre
                contrasenia.text = model.contrasenia
                telefono.text = model.telefono
                direccion.text = model.direccion
            }

        }

        recycleViewEmp.adapter = adapter
        recycleViewEmp.layoutManager = LinearLayoutManager(context)
    }
}