package pedro.projeto.organclone.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import pedro.projeto.organclone.R
import pedro.projeto.organclone.adapter.AdapterMovimentacao
import pedro.projeto.organclone.config.ConfiguracaoFirebase
import pedro.projeto.organclone.databinding.ActivityPrincipalBinding
import pedro.projeto.organclone.helper.Base64Custom
import pedro.projeto.organclone.model.Movimentacao
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
    private lateinit var movimentacao: Movimentacao

    private lateinit var mesAnoSelecionado:String

    private lateinit var adapterMovimentacao: AdapterMovimentacao

    private lateinit var recyclerView: RecyclerView

    private lateinit var usuarioRef:DatabaseReference

    private lateinit var valueEventListenerUser: ValueEventListener
    private lateinit var valueEventListenerMovientacoes: ValueEventListener

    private var listaMovimentacao: MutableList<Movimentacao> = ArrayList()
    private lateinit var movimentacaoRef:DatabaseReference

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

        recyclerView = findViewById(R.id.recicleMovimentos)

        swipe()

        //configurar adapter
        adapterMovimentacao = AdapterMovimentacao(listaMovimentacao,this)

        //configurar recycleview
        var layoutmanager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutmanager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapterMovimentacao


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
        var dataAtual:CalendarDay = calendarView.currentDate
        var mesSelecionado:String = String.format("%02d", (dataAtual.month))

        mesAnoSelecionado = mesSelecionado + dataAtual.year.toString()
        calendarView.setOnMonthChangedListener { widget, date ->
            var mesSelecionado:String = String.format("%02d", (date.month))
            mesAnoSelecionado = mesSelecionado + date.year.toString()

            movimentacaoRef.removeEventListener(valueEventListenerMovientacoes)
            recuperarMovimentacao()
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

    fun recuperarMovimentacao(){

        var emailUsuario = autenticacao.currentUser?.email
        var idUsuario = Base64Custom.codificarBase64(emailUsuario.toString())

        movimentacaoRef = firebaseRef.child("movimentacao")
                    .child(idUsuario)
                    .child(mesAnoSelecionado)

        valueEventListenerMovientacoes = movimentacaoRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot){

                listaMovimentacao.clear()

                for (dado in dataSnapshot.children){
                    var moviment = dado.getValue<Movimentacao>()
                    moviment?.key = dado.key.toString()

                    listaMovimentacao.add(moviment!!)

                }


                adapterMovimentacao.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun recuperarResumo(){

        var emailUsuario = autenticacao.currentUser?.email
        var idUsuario = Base64Custom.codificarBase64(emailUsuario.toString())

        usuarioRef = firebaseRef.child("usuarios")
            .child(idUsuario)


        valueEventListenerUser = usuarioRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot){
                var usuario  =  dataSnapshot.getValue<Usuario>()

                despesaTotal = usuario!!.despesaTotal
                receitaTotal = usuario!!.receitaTotal
                resumoUsuario = receitaTotal - despesaTotal

                textoSaldacao.text = "Olá, " + usuario.nome

                var decimalFormat : DecimalFormat = DecimalFormat("0.##")
                var resultadoFormatado = decimalFormat.format(resumoUsuario)

                textoSaldo.text = "R$ " +resultadoFormatado

            }
            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun onStart() {
        super.onStart()
        recuperarResumo()
        recuperarMovimentacao()
    }

    override fun onStop() {
        super.onStop()
        usuarioRef.removeEventListener(valueEventListenerUser)
        movimentacaoRef.removeEventListener(valueEventListenerMovientacoes)

    }

    fun swipe(){

        val itemTouch: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    var dragFlags:Int = ItemTouchHelper.ACTION_STATE_IDLE
                    var swipesFlags :Int = ItemTouchHelper.END
                    return makeMovementFlags(dragFlags,swipesFlags)
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    excluirMovimentacao(viewHolder)
                }

            }

        ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView)

    }


    fun excluirMovimentacao(viewHolder: RecyclerView.ViewHolder){
        var alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)

        alertDialog.setTitle("Excluir Movimentação da conta")
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir")
        alertDialog.setCancelable(false)

        alertDialog.setPositiveButton("Confirmar",DialogInterface.OnClickListener { dialog, which ->
            var posicao = viewHolder.adapterPosition
            movimentacao = listaMovimentacao.get(posicao)

            var emailUsuario = autenticacao.currentUser?.email
            var idUsuario = Base64Custom.codificarBase64(emailUsuario.toString())

            movimentacaoRef = firebaseRef.child("movimentacao")
                .child(idUsuario)
                .child(mesAnoSelecionado)

            movimentacaoRef.child(movimentacao.key).removeValue()
            Log.e("dados",listaMovimentacao.size.toString())
            recuperarMovimentacao()
            Log.e("dados",listaMovimentacao.size.toString())

           // adapterMovimentacao.notifyDataSetChanged()


            atualizarSaldo(movimentacao)
            Log.e("dados",listaMovimentacao.size.toString())


        })

        alertDialog.setNegativeButton("Cancelar",DialogInterface.OnClickListener { dialog, which ->
            Toast.makeText(this,"Cancelado",Toast.LENGTH_SHORT).show()
            adapterMovimentacao.notifyDataSetChanged()

        })

        var alert :AlertDialog = alertDialog.create()
        alert.show()

    }

    fun atualizarSaldo(movimentacao:Movimentacao){
        var emailUsuario = autenticacao.currentUser?.email
        var idUsuario = Base64Custom.codificarBase64(emailUsuario.toString())

        usuarioRef = firebaseRef.child("usuarios")
            .child(idUsuario)

        if (movimentacao.tipo.equals("R")){
            receitaTotal = receitaTotal - movimentacao.valor
            usuarioRef.child("receitaTotal").setValue(receitaTotal)

        }
        if (movimentacao.tipo.equals("D")){
            despesaTotal = despesaTotal - movimentacao.valor
            usuarioRef.child("despesaTotal").setValue(receitaTotal)
        }

    }




}