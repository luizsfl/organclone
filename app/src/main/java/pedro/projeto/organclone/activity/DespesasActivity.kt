package pedro.projeto.organclone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import pedro.projeto.organclone.R
import pedro.projeto.organclone.helper.DateCustom

class DespesasActivity : AppCompatActivity() {

    private lateinit var campoData : TextInputEditText
    private lateinit var campoCategoria : TextInputEditText
    private lateinit var campoDescricao : TextInputEditText
    private lateinit var campoValor : EditText


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
}