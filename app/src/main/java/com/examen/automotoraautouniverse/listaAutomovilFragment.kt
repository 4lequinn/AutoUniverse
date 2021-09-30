package com.examen.automotoraautouniverse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examen.automotoraautouniverse.models.Automovil
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_lista_automovil.*

class AutomovilViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class listaAutomovilFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_automovil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val query = db.collection("automovil")
        val options = FirestoreRecyclerOptions.Builder<Automovil>().setQuery(query, Automovil::class.java)
            .setLifecycleOwner(this).build()
        val adapter = object : FirestoreRecyclerAdapter<Automovil, AutomovilViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutomovilViewHolder {
                val view = LayoutInflater.from(context).inflate(R.layout.row_automovil,parent,false)
                return AutomovilViewHolder(view)
            }

            override fun onBindViewHolder(holder: AutomovilViewHolder, position: Int, model: Automovil) {
                val codigo: TextView = holder.itemView.findViewById(R.id.tvCodigo)
                val marca: TextView = holder.itemView.findViewById(R.id.tvMarca)
                val modelo: TextView = holder.itemView.findViewById(R.id.tvModelo)
                val color: TextView = holder.itemView.findViewById(R.id.tvColor)
                val precio: TextView = holder.itemView.findViewById(R.id.tvPrecio)
                codigo.text = model.codigo
                marca.text = model.marca
                modelo.text = model.modelo
                color.text = model.color
                precio.text = model.precio
            }

        }

        recycleViewAuto.adapter = adapter
        recycleViewAuto.layoutManager = LinearLayoutManager(context)
    }


}