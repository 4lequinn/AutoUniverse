package com.examen.automotoraautouniverse

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import kotlinx.coroutines.delay

class SplashScreen : AppCompatActivity() {
    // VARIABLE GLOBAL
    lateinit var handler: Handler
    lateinit var timer: CountDownTimer
    val CHANNELID = "Canal1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        crearChannelNotificacion()

        handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, Login::class.java))
            finish()
        }, 3000)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val notification = prefs.getBoolean("notification" , false)

        if ( notification ) {
            notificacionTimer()
            timer.start()
        }
    }

    private fun crearChannelNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nombre = "Titulo"
            val descripcion = "Mensaje"
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val canal = NotificationChannel(CHANNELID,nombre,importancia).apply {
                description = descripcion
            }

            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
    }

    private fun notificacionTimer() {

        timer = object: CountDownTimer(3000,1000) {
            override fun onFinish() {
                enviarNotificacion(200,"Recordatorio","Recuerda revisar la lista de vehículos!, Saludos.",R.drawable.ic_baseline_calendar_today_24)
            }

            override fun onTick(millisUntilFinished: Long) {

            }
        }
    }

    private fun enviarNotificacion(notificacionID:Int,titulo:String, texto:String, icono:Int) {
        val intent = Intent(this,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0, intent,0)
        val builder = NotificationCompat.Builder(this, CHANNELID)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(icono)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            notify(notificacionID, builder.build())
        }
    }
}