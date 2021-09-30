package com.examen.automotoraautouniverse

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class ActivityPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        //Generar notificaciones
        setupActionBarWithNavController(findNavController(R.id.main_fragment))

        //Recibir info
        val aux = intent.extras
        val correo = aux?.getString("CORREO")
        val proveedor = aux?.getString("PROVEEDOR")
        val recordar = aux?.getBoolean("RECORDAR")
        preferencias(correo ?: "", proveedor ?:"", recordar ?: false)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.main_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home,R.id.navigation_dashboard,R.id.navigation_crud_automovil,R.id.navigation_settings,R.id.navigation_lista_automovil))
        setupActionBarWithNavController(navController,appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> {
                    navController.navigate(R.id.principalFragment)
                    true
                }
                R.id.navigation_settings ->{
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                R.id.navigation_dashboard ->{
                    navController.navigate(R.id.crudEmpleadosFragment)
                    true
                }
                R.id.navigation_crud_automovil ->{
                     navController.navigate(R.id.crudAutomovilFragment)
                     true
                }
                R.id.navigation_lista_automovil ->{
                     navController.navigate(R.id.listaAutomovilFragment)
                    true
                }else ->{
                    false
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_fragment)
        return navController.navigateUp() || return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_cerrar_sesion,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.item_cerrar_sesion -> {
                Toast.makeText(applicationContext,"Sesión Cerrada",Toast.LENGTH_SHORT).show()
                val pref = getSharedPreferences("com.examen.automotoraautouniverse.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE).edit()
                pref.clear()
                pref.apply()
                FirebaseAuth.getInstance().signOut()
                val ventanaLogin: Intent = Intent(this,Login::class.java)
                startActivity(ventanaLogin)
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
                true
            }else -> {
                true
            }
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