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
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import pedro.projeto.organclone.R
import pedro.projeto.organclone.config.ConfiguracaoFirebase
import pedro.projeto.organclone.databinding.ActivityPrincipalBinding


class PrincipalActivity : AppCompatActivity() {
    private lateinit var calendarView : MaterialCalendarView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPrincipalBinding
private lateinit var textoSaldo:TextView
private lateinit var textoSaldacao:TextView
private lateinit var toolbar: androidx.appcompat.widget.Toolbar
private lateinit var autenticacao : FirebaseAuth
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


    /*
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        */
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
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
                autenticacao.signOut()
                var intent = Intent(this,IntroActivity::class.java)
                startActivity(intent)
                finish()

            }

        }
        return super.onOptionsItemSelected(item)
    }

}