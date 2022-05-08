package pedro.projeto.organclone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ViewAnimator
import com.google.android.material.textfield.TextInputEditText
import pedro.projeto.organclone.R
import pedro.projeto.organclone.helper.DateCustom
import pedro.projeto.organclone.model.Movimentacao

class DespesasActivity : AppCompatActivity() {

    private lateinit var campoData : TextInputEditText
    private lateinit var campoCategoria : TextInputEditText
    private lateinit var campoDescricao : TextInputEditText
    private lateinit var campoValor : EditText
    private lateinit var movimentacao: Movimentacao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_despesas)

        campoData = findViewById(R.id.edtData)
        campoCategoria = findViewById(R.id.edtCategoria)
        campoDescricao = findViewById(R.id.edtDescicao)
        campoValor = findViewById(R.id.edtValor)

        //Preencher uma data padrão
        campoData.setText(DateCustom.dataAtual())


    }

    fun salvarDespesa(view:View){
        var data:String = campoData.text.toString()
        val movimentacao = Movimentacao()
        movimentacao.valor = campoValor.text.toString().toDouble()
        movimentacao.categoria = campoCategoria.text.toString()
        movimentacao.descricao = campoDescricao.text.toString()
        movimentacao.data = campoData.text.toString()
        movimentacao.tipo = "D"
        movimentacao.salvar(data)

    }
}