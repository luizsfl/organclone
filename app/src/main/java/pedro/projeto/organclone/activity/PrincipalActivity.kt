package pedro.projeto.organclone.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import pedro.projeto.organclone.R
import pedro.projeto.organclone.config.ConfiguracaoFirebase
import pedro.projeto.organclone.databinding.ActivityPrincipalBinding
import pedro.projeto.organclone.helper.Base64Custom
import pedro.projeto.organclone.model.Usuario
import java.text.DecimalFormat


class PrincipalActivity : AppCompatActivity() {
    private lateinit var calendarView : MaterialCalendarView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPrincipalBinding
    private lateinit var textoSaldo:TextView
    private lateinit var textoSaldacao:TextView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private var autenticacao : FirebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao()
    private var firebaseRef : DatabaseReference = ConfiguracaoFirebase.getFirebaseDataBse()
    private  var despesaTotal: Double = 0.0
    private  var receitaTotal: Double = 0.0
    private  var resumoUsuario: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendarView= findViewById(R.id.calendarView)
        configuraCalendarView()

        toolbar = binding.toolbar
        toolbar.setTitle("Organize")
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_principal)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        textoSaldo = findViewById(R.id.txtSaldo)
        textoSaldacao = findViewById(R.id.txtSaldacao)

        recuperarResumo()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_principal)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun adicionarReceita(view:View){
        startActivity(Intent(this,ReceitasActivity::class.java))

    }

    fun adicionarDespesa(view:View){
        startActivity(Intent(this,DespesasActivity::class.java))

    }

    fun configuraCalendarView(){
        calendarView.setOnMonthChangedListener { widget, date ->

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menuprincipal,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menusair -> {
                autenticacao.signOut()
                var intent = Intent(this,IntroActivity::class.java)
                startActivity(intent)
                finish()

            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun recuperarResumo(){

        var emailUsuario = autenticacao.currentUser?.email
        var idUsuario = Base64Custom.codificarBase64(emailUsuario.toString())

        var usuarioRef:DatabaseReference = firebaseRef.child("usuarios")
            .child(idUsuario)


        usuarioRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot){
                var usuario  =  dataSnapshot.getValue<Usuario>()

                despesaTotal = usuario!!.despesaTotal
                receitaTotal = usuario!!.receitaTotal
                resumoUsuario = receitaTotal - despesaTotal

                textoSaldacao.text = "Olá" + usuario.nome

                var decimalFormat : DecimalFormat = DecimalFormat("0.##")
                var resultadoFormatado = decimalFormat.format(resumoUsuario)

                textoSaldo.text = "R$ " +resultadoFormatado

            }
            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}