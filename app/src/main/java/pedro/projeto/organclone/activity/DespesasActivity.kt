package pedro.projeto.organclone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.ViewAnimator
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import pedro.projeto.organclone.R
import pedro.projeto.organclone.config.ConfiguracaoFirebase
import pedro.projeto.organclone.helper.Base64Custom
import pedro.projeto.organclone.helper.DateCustom
import pedro.projeto.organclone.model.Movimentacao
import pedro.projeto.organclone.model.Usuario

class DespesasActivity : AppCompatActivity() {

    private lateinit var campoData : TextInputEditText
    private lateinit var campoCategoria : TextInputEditText
    private lateinit var campoDescricao : TextInputEditText
    private lateinit var campoValor : EditText
    private lateinit var movimentacao: Movimentacao

    private var despesaTotal : Double = 0.0
    private var despesaGerada : Double = 0.0
    private var despesaAtualizada : Double = 0.0


    private var firebaseRef :DatabaseReference = ConfiguracaoFirebase.getFirebaseDataBse()
    private var autenticacao: FirebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_despesas)

        campoData = findViewById(R.id.edtData)
        campoCategoria = findViewById(R.id.edtCategoria)
        campoDescricao = findViewById(R.id.edtDescicao)
        campoValor = findViewById(R.id.edtValor)

        //Preencher uma data padrão
        campoData.setText(DateCustom.dataAtual())

        //prenchee a variavel despesa total
        recuperarDespesaTotal()



    }

    fun salvarDespesa(view:View){
       if (validarCamposDespesa()){
           var data:String = campoData.text.toString()
           val movimentacao = Movimentacao()
           movimentacao.valor = campoValor.text.toString().toDouble()
           movimentacao.categoria = campoCategoria.text.toString()
           movimentacao.descricao = campoDescricao.text.toString()
           movimentacao.data = campoData.text.toString()
           movimentacao.tipo = "D"

           despesaGerada = movimentacao.valor
           despesaAtualizada = despesaTotal + despesaGerada

           atualizarDespesa(despesaAtualizada)

           movimentacao.salvar(data)
       }

    }

    fun validarCamposDespesa():Boolean {
        var textValor = campoValor.text.toString()
        var textData = campoData.text.toString()
        var textCategoria = campoCategoria.text.toString()
        var textDescricao = campoDescricao.text.toString()

        if (textValor.isEmpty()){
            Toast.makeText( this,"Preencha o valor", Toast.LENGTH_SHORT).show()
            return false
        }else if (textData.isEmpty()){
            Toast.makeText( this,"Preencha a data", Toast.LENGTH_SHORT).show()
            return false
        }else if (textCategoria.isEmpty()){
            Toast.makeText( this,"Preencha a categoria", Toast.LENGTH_SHORT).show()
            return false
        }else if (textDescricao.isEmpty()){
            Toast.makeText( this,"Preencha a descriçao", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun recuperarDespesaTotal(){

        var emailUsuario = autenticacao.currentUser?.email
        var idUsuario = Base64Custom.codificarBase64(emailUsuario.toString())

        var usuarioRef:DatabaseReference = firebaseRef.child("usuarios")
            .child(idUsuario)


        usuarioRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot){
                var usuario  =  dataSnapshot.getValue<Usuario>()

                despesaTotal = usuario!!.despesaTotal

            }
            override fun onCancelled(error: DatabaseError) {

            }

        })



    }

    fun atualizarDespesa(despesaAtualizada : Double){
        var emailUsuario = autenticacao.currentUser?.email
        var idUsuario = Base64Custom.codificarBase64(emailUsuario.toString())

        var usuarioRef:DatabaseReference = firebaseRef.child("usuarios")
            .child(idUsuario)

        usuarioRef.child("despesaTotal").setValue(despesaAtualizada)

    }
}